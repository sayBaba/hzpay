package com.hz.pay.service;

import com.hz.pay.model.PayChannel;

/**
 * 支付渠道接口
 */
public interface IPayChannelService {

    /**
     * 根据渠道id和商户号查询支付渠道信息
     * @param channelId
     * @param mchId
     * @return
     */
    public PayChannel selectPayChannel(String channelId, String mchId);
}
