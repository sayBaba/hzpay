package com.hz.pay.service;

import com.hz.pay.model.PayOrder;

/**
 * 支付流水接口
 */
public interface IPayOrderService {

    /**
     * 创建支付流水
     * @param payOrder
     * @return
     */
    public int createPayOrder(PayOrder payOrder);

}
