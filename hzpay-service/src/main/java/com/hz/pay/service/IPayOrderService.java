package com.hz.pay.service;

import com.hz.pay.model.PayOrder;

/**
 * 支付订单接口
 */
public interface IPayOrderService {

    /**
     * 创建支付流水
     * @param payOrder
     * @return
     */
    public int createPayOrder(PayOrder payOrder);

    /**
     * 更新订单的状态
     * @param payOrder
     * @return
     */
    public int updatePayOrder(PayOrder payOrder);

    /**
     * 根据交易订单号查询
     * @param payOrderId
     * @return
     */
    public PayOrder selectPayOrder(String payOrderId);

    /**
     * 更新交易订单
     * @param payOrderId
     * @return
     */
    public int updateStatus4Success(String payOrderId);

}
