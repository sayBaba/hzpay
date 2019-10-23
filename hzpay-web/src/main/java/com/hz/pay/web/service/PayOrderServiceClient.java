package com.hz.pay.web.service;

import com.hz.pay.web.service.impl.PayOrderServiceClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "HZPAY-SERVICE",fallback = PayOrderServiceClientImpl.class)
public interface PayOrderServiceClient {

    /**
     * 创建交易订单
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/create",method = RequestMethod.POST)
    public String createPayOrder(@RequestParam String jsonParam);


    /**
     * 查询商户支付渠道
     * @param jsonParam
     * @return
     */
    @RequestMapping(value ="/pay_channel/select",method = RequestMethod.POST)
    public String selectPayChannel(@RequestParam String jsonParam);



    /**
     * 调用支付宝wap支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value ="/pay/channel/ali_wap",method = RequestMethod.POST)
    public String alipayWapPayment(@RequestParam String jsonParam);

}