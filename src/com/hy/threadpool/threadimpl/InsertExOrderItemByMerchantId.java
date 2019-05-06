package com.hy.threadpool.threadimpl;

import com.hy.entity.order.EXOrder;
import com.hy.entity.order.EXOrderItem;
import com.hy.entity.order.SellingOrderItem;
import com.hy.mapper.AuditOrderMapper;
import com.hy.threadpool.ResultDto;
import com.hy.util.LoginUtil;

import java.util.concurrent.Callable;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.threadpool.threadimpl
 * @Email 1023012015@qq.com
 * @date 2019/3/29 11:26
 */
public class InsertExOrderItemByMerchantId implements Callable<ResultDto> {
    private SellingOrderItem item;
    private AuditOrderMapper auditOrderMapper;

    public InsertExOrderItemByMerchantId(SellingOrderItem item, AuditOrderMapper auditOrderMapper) {
        this.item = item;
        this.auditOrderMapper = auditOrderMapper;
    }

    @Override
    public ResultDto call() throws Exception {
        ResultDto resultDto = new ResultDto(true);
        try {
            EXOrder exOrder = auditOrderMapper.getExorderByMerchantId(item.getMerchantId(), item.getGroupNum());
            if (exOrder == null) {
                throw new RuntimeException("出库单未创建！");
            }
            EXOrderItem exOrderItem = auditOrderMapper.getExorderItemByGoodId(item.getOrderNum(),item.getProductId());
            if (exOrderItem == null) {
                exOrderItem = createExOrderItem(exOrder);
                auditOrderMapper.insertExOrderItem(exOrderItem);
            } else {
                exOrderItem.setFinalQuantity(exOrderItem.getFinalQuantity()+item.getFinalQuantity());
                exOrderItem.setQuantity(exOrderItem.getQuantity()+item.getQuantity());
                exOrderItem.setGiftQuantity(exOrderItem.getGiftQuantity()+item.getGiftQuantity());
                auditOrderMapper.updateExOrederItemById(exOrderItem);
            }

        } catch (Exception e) {
            resultDto.setFlag(false);
        }
        return resultDto;
    }

    private EXOrderItem createExOrderItem(EXOrder exOrder) {
        EXOrderItem exOrderItem = new EXOrderItem();
        exOrderItem.setGroupNum(exOrder.getGroupNum());
        exOrderItem.setOrderNum(exOrder.getOrderNum());
        exOrderItem.setGoodId(item.getProductId());
        exOrderItem.setPurchasePrice(item.getPurchasePrice());
        exOrderItem.setSalePrice(item.getSalePrice());
        exOrderItem.setQuantity(item.getQuantity());
        exOrderItem.setFinalQuantity(item.getFinalQuantity());
        exOrderItem.setGiftQuantity(item.getGiftQuantity());
        exOrderItem.setCreator(LoginUtil.getLoginUser().getUSER_ID() + "");
        return exOrderItem;
    }
}
