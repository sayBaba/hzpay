package com.hz.pay.web.service;

import com.hz.common.req.RefundOrderReq;
import com.hz.common.resp.PayQueryResp;
import com.hz.pay.web.service.impl.PayOrderServiceClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Service
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
     * 查询交易订单
     * @param mchOrderId
     * @return
     */
    @RequestMapping(value = "/payRlt/select",method = RequestMethod.POST)
    public PayQueryResp queryPayOrder(@RequestParam String mchOrderId);

    /**
     * 创建退款流水
     * @param refundOrderReq
     * @return
     */
    @RequestMapping(value = "/refund/create",method = RequestMethod.POST)
    public String createRefundOrder(@RequestBody RefundOrderReq refundOrderReq);


    /**
     * 查询商户支付渠道
     * @param refundOrderReq
     * @return
     */
    @RequestMapping(value ="/pay_channel/select",method = RequestMethod.POST)
    public String selectPayChannel(@RequestBody RefundOrderReq refundOrderReq);



    /**
     * 调用支付宝wap支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value ="/pay/channel/ali_wap",method = RequestMethod.POST)
    public String alipayWapPayment(@RequestParam String jsonParam);



}
