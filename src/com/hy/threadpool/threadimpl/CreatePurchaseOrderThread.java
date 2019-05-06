package com.hy.threadpool.threadimpl;

import com.hy.entity.order.PurchaseOrder;
import com.hy.entity.product.Supplier;
import com.hy.mapper.AuditOrderMapper;
import com.hy.threadpool.ResultDto;
import com.hy.util.DateUtil;
import com.hy.util.LoginUtil;
import com.hy.util.MathUtil;
import com.hy.util.StringUtil;

import java.util.concurrent.Callable;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.threadpool
 * @Email 1023012015@qq.com
 * @date 2019/3/28 10:04
 */
public class CreatePurchaseOrderThread implements Callable<ResultDto> {
    private long supplierId;
    private AuditOrderMapper auditOrderMapper;

    public CreatePurchaseOrderThread(long supplierId, AuditOrderMapper auditOrderMapper) {
        this.supplierId = supplierId;
        this.auditOrderMapper = auditOrderMapper;
    }


    @Override
    public ResultDto call() throws Exception {
        ResultDto resultDto = new ResultDto(true);
        try {
            String groupNum = "GP_" + DateUtil.group();
            PurchaseOrder purchaseOrder = auditOrderMapper.getPurchaseOrder(groupNum,supplierId);
            if (purchaseOrder != null) {
                return resultDto;
            }
            purchaseOrder = new PurchaseOrder();
            purchaseOrder.setGroupNum(groupNum);
            purchaseOrder.setOrderNum("CG_"+ StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
            purchaseOrder.setSupplierId(supplierId);
            purchaseOrder.setDeliverAddress("太原市清徐县赵家堡工业园（联坤仓储物流园）");
            purchaseOrder.setUserId(LoginUtil.getLoginUser().getUSER_ID());
            purchaseOrder.setCheckedState(1);
            Supplier supplier = auditOrderMapper.getSupplierById(supplierId);
            purchaseOrder.setManagerName(supplier.getContactPerson());
            purchaseOrder.setManagerTel(supplier.getContactPerson_mobile());
            if (!auditOrderMapper.insertPurchaseOrder(purchaseOrder)) {
                resultDto.setFlag(false);
            }
        } catch (Exception e) {
            resultDto.setFlag(false);
        }
        return resultDto;
    }
}
