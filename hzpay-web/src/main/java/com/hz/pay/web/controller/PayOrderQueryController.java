package com.hz.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hz.pay.web.request.PayQueryReq;
import com.hz.pay.web.response.PayQueryResp;
import com.hz.pay.web.service.MchInfoServiceClient;
import com.hz.pay.web.service.PayOrderServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付结果查询
 */
@RestController
public class PayOrderQueryController {

    private Logger logger = LoggerFactory.getLogger(PayOrderController.class);

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;

    @Autowired
    private MchInfoServiceClient mchInfoServiceClient;

    /**
     * 查询支付结果
     * @param payQueryReq
     * @return
     */
    @RequestMapping("/payQuery")
    public PayQueryResp payQuery(PayQueryReq payQueryReq ) {
        logger.info("###### 接收到商户id：{}的查询支付结果的请求：{}", payQueryReq.getMchId(),payQueryReq);
        PayQueryResp payQueryResp = new PayQueryResp();
        String mchId = payQueryReq.getMchId();
        String channelId = payQueryReq.getChannelId();
        String mchOrderId = payQueryReq.getMchOrderId();
        if (StringUtils.isEmpty(mchId)){
            logger.info("商户订单号：{}商户id不能为空",mchOrderId);
            payQueryResp.setCode("9999");
            payQueryResp.setMsg("商户id不能为空");
            return payQueryResp;
        }

        if (StringUtils.isEmpty(channelId)){
            logger.info("商户订单号：{}支付渠道不能为空",mchOrderId);
            payQueryResp.setCode("9999");
            payQueryResp.setMsg("");
            return payQueryResp;
        }

        if (StringUtils.isEmpty(mchOrderId)){
            payQueryResp.setCode("9999");
            payQueryResp.setMsg("商户订单不能为空");
            return payQueryResp;
        }

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("mchId", mchId);
        String mchRlt = mchInfoServiceClient.selectMchInfo(jsonParam.toJSONString());
        if (StringUtils.isEmpty(mchRlt)){
            logger.info("商户订单号：{}商户id不存在",mchOrderId);
            payQueryResp.setCode("9999");
            payQueryResp.setMsg("商户id不存在");
            return payQueryResp;
        }

        String rlt = payOrderServiceClient.selectPayChannel(getJsonParam(new String[]{"channelId", "mchId"}, new String[]{channelId, mchId}));
        if (StringUtils.isEmpty(rlt)){
            logger.info("商户订单号：{}channelId不存在",mchOrderId);
            payQueryResp.setCode("9999");
            payQueryResp.setMsg("");
            return payQueryResp;
        }
        //从交易表中查出结果返回。



        logger.info("###### 接收到商户id：{}的查询支付请求返回对象：{}", payQueryReq.getMchId(),payQueryResp);
        return payQueryResp;
    }

    /**
     * 转成json字符串
     * @param names
     * @param values
     * @return
     */
    private String getJsonParam(String[] names, Object[] values) {
        JSONObject jsonParam = new JSONObject();
        for (int i = 0; i < names.length; i++) {
            jsonParam.put(names[i], values[i]);
        }
        return jsonParam.toJSONString();
    }

}


