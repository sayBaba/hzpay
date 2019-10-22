package com.hz.pay.web.service;

import com.hz.pay.web.service.impl.PayChannelServiceClientImpl;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 查询商户支付渠道
 */
@FeignClient(value = "HZPAY-SERVICE",fallback = PayChannelServiceClientImpl.class)
public interface PayChannelServiceClient {


    public String selectPayChannel(String jsonParam);

}
