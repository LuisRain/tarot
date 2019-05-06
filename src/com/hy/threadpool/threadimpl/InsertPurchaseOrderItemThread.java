package com.hy.threadpool.threadimpl;

import com.hy.entity.OrderGood;
import com.hy.entity.order.PurchaseOrderItem;
import com.hy.mapper.AuditOrderMapper;
import com.hy.threadpool.ResultDto;
import com.hy.util.LoginUtil;

import java.util.concurrent.Callable;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.threadpool
 * @Email 1023012015@qq.com
 * @date 2019/3/28 10:34
 */
public class InsertPurchaseOrderItemThread implements Callable<ResultDto> {
    private OrderGood orderGood;
    private AuditOrderMapper auditOrderMapper;

    public InsertPurchaseOrderItemThread(OrderGood orderGood, AuditOrderMapper auditOrderMapper) {
        this.orderGood = orderGood;
        this.auditOrderMapper = auditOrderMapper;
    }


    @Override
    public ResultDto call() throws Exception {
        ResultDto resultDto = new ResultDto(true);
        try {
            PurchaseOrderItem item = auditOrderMapper.getPurchaseOrderItem(orderGood);
            //存在修改，否则新增
            if (item != null) {
                item.setSuggestQuantity(orderGood.getSumBuyCounts() + item.getSuggestQuantity());
                item.setQuantity(orderGood.getSumBuyCounts() + item.getQuantity());
                item.setFinalQuantity(orderGood.getSumBuyCounts() + item.getFinalQuantity());
                item.setGiftQuantity(orderGood.getSumGiftQuantity() + item.getGiftQuantity());
                Boolean aBoolean = auditOrderMapper.updatePurOrderItem(item);
                System.out.println("修改----------"+aBoolean);
                resultDto.setFlag(aBoolean);
            } else {
                item = new PurchaseOrderItem();
                item.setGroupNum(orderGood.getGroupNum());
                item.setPurchasePrice(orderGood.getProductPrice());
                item.setSuggestQuantity(orderGood.getSumBuyCounts());
                item.setQuantity(orderGood.getSumBuyCounts());
                item.setFinalQuantity(orderGood.getSumBuyCounts());
                item.setGiftQuantity(orderGood.getSumGiftQuantity());
                item.setCreator(LoginUtil.getLoginUser().getUSER_ID() + "");
                item.setGoodId(orderGood.getGoodId());
                item.setSupplerId(orderGood.getSupplierId());
                boolean b = auditOrderMapper.insertPurOrderItem(item);
                System.out.println("新增----------------"+b);
                resultDto.setFlag(b);
            }
        } catch (Exception e) {
            resultDto.setFlag(false);
        }
        return resultDto;
    }
}
