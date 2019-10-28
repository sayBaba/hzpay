package com.hz.pay.service;

import com.hz.pay.model.PayOrder;

import java.util.List;

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



    /**
     * 根据商户订单号查询
     * @param mchOrderId
     * @return
     */
    public PayOrder getPayOrderBymchOrderId(String mchId,String mchOrderId);



    /**
     * 根据渠道和状态
     * @param channelId
     * @return
     */
    public List<PayOrder> getPayListByStatus(String channelId, String staus);


}
