package com.hz.pay.service;

import com.hz.common.req.RefundOrderReq;
import com.hz.pay.model.RefundOrder;

/**
 * 退款接口
 */
public interface IRefundService {

    /**
     * 创建退款流水
     * @param refundOrderReq
     * @return
     */
    public int createPayOrder(RefundOrderReq refundOrderReq);
}
