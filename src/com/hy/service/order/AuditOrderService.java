package com.hy.service.order;

import java.util.List;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.service.order
 * @Email 1023012015@qq.com
 * @date 2019/3/27 15:12
 */
public interface AuditOrderService {
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 省级审核订单
     * @params: []
     * @return: java.lang.String
     * @Date: 2019/3/27 17:16
     * @Modified By:
     * @param list
    */
    boolean provinceAuditOrder(List<Long> list);
}
