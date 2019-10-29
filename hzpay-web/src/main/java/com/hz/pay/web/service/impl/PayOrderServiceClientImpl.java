package com.hz.pay.web.service.impl;

import com.hz.common.req.RefundOrderReq;
import com.hz.common.resp.PayQueryResp;
import com.hz.pay.web.service.PayOrderServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 服务容错
 */
@Service
public class PayOrderServiceClientImpl implements PayOrderServiceClient {


    @Override
    public String createPayOrder(String jsonParam) {
        return "error";
    }

    @Override
    public PayQueryResp queryPayOrder(String jsonParam) {
        return new PayQueryResp();
    }

    @Override
    public String createRefundOrder(RefundOrderReq refundOrderReq) {
        return "error";
    }

    @Override
    public String selectPayChannel(RefundOrderReq refundOrderReq) {
        return "error";
    }

    @Override
    public String alipayWapPayment(String jsonParam) {
        return "error";
    }

}
