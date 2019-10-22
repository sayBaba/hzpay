package com.hz.pay.web.service;

import com.hz.pay.web.service.impl.PayChannelServiceClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 查询支付渠道接口
 */
@FeignClient(value = "HZPAY-SERVICE1",fallback = PayChannelServiceClientImpl.class)
public interface PayChannelServiceClient {

    /**
     * 查询商户支付渠道
     * @param jsonParam
     * @return
     */
    @RequestMapping(value ="/pay_channel/select",method = RequestMethod.POST)
    public String selectPayChannel(String jsonParam);

}
