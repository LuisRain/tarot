package com.hy.threadpool.threadimpl;

import com.hy.entity.OrderGood;
import com.hy.mapper.AuditOrderMapper;
import com.hy.threadpool.ResultDto;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.threadpool
 * @Email 1023012015@qq.com
 * @date 2019/3/28 9:38
 */
public class SubInventoryThread implements Callable<ResultDto<OrderGood>> {

    private AuditOrderMapper auditOrderMapper;

    private OrderGood orderGood;

    private Map<String, Double> inventoryMap;

    public SubInventoryThread(AuditOrderMapper auditOrderMapper, OrderGood orderGood, Map<String, Double> inventoryMap) {
        this.auditOrderMapper = auditOrderMapper;
        this.orderGood = orderGood;
        this.inventoryMap = inventoryMap;
    }

    @Override
    public ResultDto<OrderGood> call() throws Exception {
        ResultDto<OrderGood> resultDto = null;
        try {
                Long key = orderGood.getGoodId();
                Double inventorys = inventoryMap.get(key + "") == null ? auditOrderMapper.selectGoodInventorys(orderGood.getGoodId()) : inventoryMap.get(key + "");
                inventorys = inventorys == null ? 0.0 : inventorys;
                double sumBuycouts = orderGood.getSumBuyCounts();
                double otherInventory = inventorys - sumBuycouts;
                if (otherInventory >= 0) {
                    //不用采购
                    resultDto = new ResultDto<>(false);
                    //剩余库存
                    inventoryMap.put(key + "", otherInventory);
                } else {
                    //需要采购
                    orderGood.setSumBuyCounts(-otherInventory);
                    resultDto = new ResultDto<>(true, orderGood);
                    //剩余库存为0
                    inventoryMap.put(key + "", 0.0);
                }
        } catch (Exception e) {
            resultDto = new ResultDto<>(false);
        }

        return resultDto;
    }
}
