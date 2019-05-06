package com.hy.threadpool.threadimpl;

import com.hy.entity.order.EXOrder;
import com.hy.entity.order.EXOrderItem;
import com.hy.entity.order.SellingOrderItem;
import com.hy.entity.product.Merchant;
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
 * @Package com.hy.threadpool.threadimpl
 * @Email 1023012015@qq.com
 * @date 2019/3/29 9:54
 */
public class InsertExOrderByMerchantId implements Callable<ResultDto> {
    private Long merchantId;
    private AuditOrderMapper auditOrderMapper;
    private String groupNum;
    public InsertExOrderByMerchantId(Long merchantId,String groupNum, AuditOrderMapper auditOrderMapper) {
        this.merchantId = merchantId;
        this.groupNum = groupNum;
        this.auditOrderMapper = auditOrderMapper;
    }

    @Override
    public ResultDto call() throws Exception {
        ResultDto resultDto = new ResultDto(true);
        try {
            EXOrder exOrder = auditOrderMapper.getExorderByMerchantId(merchantId, groupNum);
            if (exOrder != null) {
                return resultDto;
            }
            //生成出库单
            exOrder = createExOrder();
            resultDto.setFlag(auditOrderMapper.insertExOrder(exOrder));
        } catch (Exception e) {
            resultDto.setFlag(false);
        }
        return resultDto;
    }

    private EXOrder createExOrder() {
        EXOrder exOrder = new EXOrder();
        exOrder.setGroupNum(groupNum);
        exOrder.setOrderNum("CK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
        exOrder.setCheckedState(1);
        exOrder.setMerchantId(merchantId);
        exOrder.setUserId(LoginUtil.getLoginUser().getUSER_ID());
        exOrder.setIvt_state(1);
        Merchant merchant = auditOrderMapper.getMerchantById(merchantId);
        exOrder.setManagerName(merchant.getContactPerson());
        exOrder.setManagerTel(merchant.getMobile());
        exOrder.setDeliverAddress(merchant.getAddress());
        return exOrder;
    }
}
