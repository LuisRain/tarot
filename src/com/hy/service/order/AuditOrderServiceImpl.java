package com.hy.service.order;

import com.hy.entity.OrderGood;
import com.hy.entity.order.SellingOrder;
import com.hy.entity.order.SellingOrderItem;
import com.hy.mapper.AuditOrderMapper;
import com.hy.threadpool.threadimpl.*;
import com.hy.threadpool.ResultDto;
import com.hy.util.DateUtil;
import com.hy.util.ThreadPoolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.service.order
 * @Email 1023012015@qq.com
 * @date 2019/3/27 15:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditOrderServiceImpl implements AuditOrderService {
    @Autowired
    private AuditOrderMapper auditOrderMapper;
    private static Map<String, Double> inventoryMap = new HashMap<>();
    @Override
    public boolean provinceAuditOrder(List<Long> ids) {
        String groupNum = "GP_" + DateUtil.group();
        //根据批次号 查看是否有地市未审核的订单
        List<SellingOrder> sellingOrderList = auditOrderMapper.selectSellingByGroupNum(groupNum);
        if (sellingOrderList != null && sellingOrderList.size() > 0) {
            throw new RuntimeException("还有地市未审核的订单！例如，门店：" + sellingOrderList.get(0).getManagerName());
        }
        //查询所以商品的订货总数和相应的供应商
        List<OrderGood> goodList = auditOrderMapper.selectorderItemsByGroupNum(groupNum,ids);
        //查询对应的商品的库存 对比生成实际采购的商品信息
        goodList = subInventory(goodList,inventoryMap);
        //与所需供货商品相关的供应商
        Set<Long> supplierIds = goodList.stream().map(e -> e.getSupplierId()).collect(Collectors.toSet());
        //预生成对应的供应商的采购单主单
        if (!createPurchaseOrderBySupplierid(supplierIds)) {
            throw new RuntimeException("审核失败！");
        }
        //把实际采购的商品信息加入相应的供应商采购详单
        if (!insertPurchaseOrderItem(goodList)) {
            throw new RuntimeException("审核失败！");
        }
        //计算采购单总单信息
        if (!auditOrderMapper.updatePurchsaeOrederInfo(groupNum)) {
            throw new RuntimeException("审核失败！");
        }
        /***********************采购单生成完成***********************/
        //生成门店的出库单
        //采购商品总列表
        List<SellingOrderItem> items = auditOrderMapper.selectSellingItem(groupNum,ids);
        //根据门店id 生成相应出库单
        if (!insertExOrderByMerchantId(items,groupNum)) {
            throw new RuntimeException("审核失败！");
        }
        //将门店采购的商品加入相应的出库详单
        if (!insertExOrderItemByMerchantId(items)) {
            throw new RuntimeException("审核失败！");
        }
        //计算门店出库箱单的相应的信息
        if (!updateExOrderInfo(groupNum)) {
            throw new RuntimeException("审核失败！");
        }
        //修改采购单状态为2
        if (!auditOrderMapper.updateSellorderCheckState(ids)) {
            throw new RuntimeException("审核失败！");
        }

        //全部审核完成，清理map集合
        List<OrderGood> goods = auditOrderMapper.selectorderItemsByGroupNum(groupNum, null);
        if (goods == null || goods.size() == 0) {
            inventoryMap.clear();
        }
        return true;
    }

    private boolean updateExOrderInfo(String groupNum) {
        return auditOrderMapper.updateExOrderItemInfo(groupNum)
                && auditOrderMapper.updateExOrderInfo(groupNum);
    }

    private boolean insertExOrderItemByMerchantId(List<SellingOrderItem> items) {
        boolean result = true;
        //创建线程池对象
        ExecutorService executor = ThreadPoolFactory.getMaxThreadPool();
        try {
            //初始化线程池结果集
            List<Future<ResultDto>> futureList = new ArrayList<>();
            items.stream().forEach(e -> futureList.add(executor.submit(new InsertExOrderItemByMerchantId(e, auditOrderMapper))));
            executor.shutdown();
            //遍历结果集，查看是否试行完成
            for (Future<ResultDto> future : futureList) {
                if (!future.get().getFlag()) {
                    throw new RuntimeException("创建出库详单失败");
                }
            }
        } catch (Exception e) {
            result = false;
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
        return result;
    }

    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 生成出库单 根据门店id
     * @params: [items]
     * @return: boolean
     * @Date: 2019/3/29 11:04
     * @Modified By:
     */
    private boolean insertExOrderByMerchantId(List<SellingOrderItem> items,String groupNum) {
        boolean result = true;
        //创建线程池对象
        ExecutorService executor = ThreadPoolFactory.getThreadPool();
        try {
            Set<Long> merchantIds = items.stream().map(e -> e.getMerchantId()).collect(Collectors.toSet());
            //初始化线程池结果集
            List<Future<ResultDto>> futureList = new ArrayList<>();
            merchantIds.stream().map(e->new InsertExOrderByMerchantId(e,groupNum, auditOrderMapper)).forEach(e -> futureList.add(executor.submit(e)));
            executor.shutdown();
            //遍历结果集，查看是否试行完成
            for (Future<ResultDto> future : futureList) {
                if (!future.get().getFlag()) {
                    throw new RuntimeException("创建出库单失败");
                }
            }
        } catch (Exception e) {
            result = false;
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
        return result;
    }

    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 新增采购单详单
     * @params: [goodList]
     * @return: boolean
     * @Date: 2019/3/29 9:28
     * @Modified By:
     */
    private boolean insertPurchaseOrderItem(List<OrderGood> goodList) {
        boolean result = true;
        //创建线程池对象
        ExecutorService executor = ThreadPoolFactory.getThreadPool();
        try {
            //初始化线程池结果集
            List<Future<ResultDto>> futureList = new ArrayList<>();
            goodList.stream().forEach(e -> futureList.add(executor.submit(new InsertPurchaseOrderItemThread(e, auditOrderMapper))));
            executor.shutdown();
            //遍历结果集，查看是否试行完成
            for (Future<ResultDto> future : futureList) {
                if (!future.get().getFlag()) {
                    throw new RuntimeException("创建采购详单失败");
                }
            }
        } catch (Exception e) {
            result = false;
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
        return result;
    }

    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 新增采购单总单
     * @params: [supplierIds]
     * @return: boolean
     * @Date: 2019/3/29 9:28
     * @Modified By:
     */
    private boolean createPurchaseOrderBySupplierid(Set<Long> supplierIds) {
        boolean result = true;
        //创建线程池对象
        ExecutorService executor = ThreadPoolFactory.getThreadPool();
        try {
            //初始化线程池结果集
            List<Future<ResultDto>> futureList = new ArrayList<>();
            supplierIds.stream().forEach(e -> futureList.add(executor.submit(new CreatePurchaseOrderThread(e, auditOrderMapper))));
            executor.shutdown();
            //遍历结果集，查看是否试行完成
            for (Future<ResultDto> future : futureList) {
                if (!future.get().getFlag()) {
                    throw new RuntimeException("创建采购单失败");
                }
            }
        } catch (Exception e) {
            result = false;
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
        return result;
    }

    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 对比库存数据，重新生成应采购的商品列表
     * @params: [goodList]
     * @return: java.util.List<com.hy.entity.OrderGood>
     * @Date: 2019/3/29 9:29
     * @Modified By:
     */
    private List<OrderGood> subInventory(List<OrderGood> goodList, Map<String, Double> inventoryMap) {
        //初始化结果集
        List<OrderGood> newGoodList = new ArrayList<>();
        //创建线程池对象
        ExecutorService executor = ThreadPoolFactory.getThreadPool();
        try {
            //初始化线程池结果集
            List<Future<ResultDto<OrderGood>>> futureList = new ArrayList<>();
            //执行 并返回结果
            goodList.stream().map(e -> new SubInventoryThread(auditOrderMapper,e,inventoryMap)).forEach(e -> futureList.add(executor.submit(e)));
            executor.shutdown();
            //遍历结果集生成新的采购商品集合
            ResultDto<OrderGood> resultDto;
            for (Future<ResultDto<OrderGood>> future : futureList) {
                resultDto = future.get();
                if (resultDto.getFlag()) {
                    newGoodList.add(resultDto.getObj());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
        return newGoodList;
    }
}
