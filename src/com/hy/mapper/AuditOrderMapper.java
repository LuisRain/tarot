package com.hy.mapper;

import com.hy.entity.OrderGood;
import com.hy.entity.order.*;
import com.hy.entity.product.Merchant;
import com.hy.entity.product.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.mapper
 * @Email 1023012015@qq.com
 * @date 2019/3/27 17:18
 */
public interface AuditOrderMapper {
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 查看销售单中未审核的订单
     * @params: [groupNum]
     * @return: java.util.List<com.hy.entity.order.SellingOrder>
     * @Date: 2019/3/27 17:48
     * @Modified By:
    */
    List<SellingOrder> selectSellingByGroupNum(@Param("groupNum") String groupNum);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 查询所以商品的订货总数和相应的供应商
     * @params: [groupNum][ids]
     * @return: java.util.List<com.hy.entity.order.SellingOrderItem>
     * @Date: 2019/3/27 17:54
     * @Modified By:
    */
    List<OrderGood> selectorderItemsByGroupNum(@Param("groupNum") String groupNum, @Param("ids") List<Long> ids);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 根据商品id 查询库存数
     * @params: [goodId]
     * @return: double
     * @Date: 2019/3/28 10:41
     * @Modified By:
    */
    Double selectGoodInventorys(@Param("goodId")Long goodId);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 计算当前批次的采购详单相关信息
     * @params: [groupNum]
     * @return: boolean
     * @Date: 2019/3/28 10:41
     * @Modified By:
    */
    boolean updatePurchsaeOrederInfo(@Param("groupNum")String groupNum);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 根据供应商id查询一个供应商
     * @params: [id]
     * @return: com.hy.entity.product.Supplier
     * @Date: 2019/3/28 14:54
     * @Modified By:
    */
    Supplier getSupplierById(long id);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 插入采购单
     * @params: [purchaseOrder]
     * @return: boolean
     * @Date: 2019/3/28 15:02
     * @Modified By:
    */
    boolean insertPurchaseOrder(PurchaseOrder purchaseOrder);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 插入采购詳单
     * @params: [item]
     * @return: boolean
     * @Date: 2019/3/28 15:50
     * @Modified By:
    */
    boolean insertPurOrderItem(PurchaseOrderItem item);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 修改采购单状态为2
     * @params: [groupNum][ids]
     * @return: boolean
     * @Date: 2019/3/28 17:55
     * @Modified By:
    */
    boolean updateSellorderCheckState(@Param("ids") List<Long> ids);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 查询采购详单 的item 包含门店id
     * @params: [groupNum][ids]
     * @return: java.util.List<com.hy.entity.order.SellingOrderItem>
     * @Date: 2019/3/29 9:50
     * @Modified By:
    */
    List<SellingOrderItem> selectSellingItem(@Param("groupNum") String groupNum, @Param("ids") List<Long> ids);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO
     * @params: [merchantId] 根据门店id 和批次号查询 出库单总单 需要缓存
     * @return: com.hy.entity.order.EXOrder
     * @Date: 2019/3/29 10:19 
     * @Modified By:  
    */
    EXOrder getExorderByMerchantId(@Param("merchantId")Long merchantId,@Param("groupNum")String groupNum);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 新增出库单
     * @params: [exOrder]
     * @return: boolean
     * @Date: 2019/3/29 10:28
     * @Modified By:
    */
    boolean insertExOrder(EXOrder exOrder);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 新增出库详单
     * @params: [exOrderItem]
     * @return: boolean
     * @Date: 2019/3/29 10:29
     * @Modified By:
    */
    boolean insertExOrderItem(EXOrderItem exOrderItem);

    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 查询门店详情 by id
     * @params: [merchantId]
     * @return: com.hy.entity.product.Merchant
     * @Date: 2019/3/29 10:51
     * @Modified By:
    */
    Merchant getMerchantById(@Param("id") Long id);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 计算当前批次的出库单相关信息
     * @params: [groupNum]
     * @return: boolean
     * @Date: 2019/3/29 11:03
     * @Modified By:
    */
    boolean updateExOrderInfo(@Param("groupNum")String groupNum);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 根据 订单编号和商品id 查询改商品是否已经生成 需要缓存
     * @params: [orderNum, productId]
     * @return: com.hy.entity.order.EXOrderItem
     * @Date: 2019/3/29 11:33
     * @Modified By:
    */
    EXOrderItem getExorderItemByGoodId(@Param("orderNum")String orderNum,@Param("goodId")Long goodId);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 修改配送商品 数量
     * @params: [exOrderItem]
     * @return: boolean
     * @Date: 2019/3/29 14:12
     * @Modified By:
    */
    boolean updateExOrederItemById(EXOrderItem exOrderItem);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 修改出库详单相应信息
     * @params: [groupNum]
     * @return: boolean
     * @Date: 2019/3/29 15:46
     * @Modified By:
    */
    boolean updateExOrderItemInfo(@Param("groupNum")String groupNum);
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 根据 批次号 供应商id 查看是否已经生成 采购单
     * @params: [groupNum, supplierId]
     * @return: com.hy.entity.order.PurchaseOrder
     * @Date: 2019/4/2 10:00
     * @Modified By:
    */
    PurchaseOrder getPurchaseOrder(@Param("groupNum")String groupNum,@Param("supplierId") long supplierId);

    PurchaseOrderItem getPurchaseOrderItem(OrderGood orderGood);

    Boolean updatePurOrderItem(PurchaseOrderItem item);

}
