package com.hz.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hz.common.constant.PayConstant;
import com.hz.common.util.XXPayUtil;
import com.hz.pay.web.service.PayOrderServiceClient;
import com.hz.pay.web.util.ParamasCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 支付接口
 */
@RestController
@RequestMapping("/pay")
public class PayOrderController {

    private Logger logger = LoggerFactory.getLogger(PayOrderController.class);

    @Autowired
    private ParamasCheck paramasCheck;

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;

    /**
     * 统一下单接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建支付订单
     * 3)根据商户选择渠道,调用支付服务进行下单
     * 4)返回下单数据
     * @param params
     * @return
     */
    @RequestMapping("/create_order")
    public String payOrder(@RequestParam String params) {
        logger.info("###### 开始接收商户统一下单请求 ######，请求参数：{}", params);
        if (StringUtils.isEmpty(params)) {
            return null;
        }
        //字符解析为json
        JSONObject po = JSONObject.parseObject(params);

        //参数校验
        Object object = paramasCheck.validateParams(po);

        if (object instanceof String) {
            logger.info("{}参数校验不通过:{}", object);
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, null));
        }

        JSONObject payOrder = null;
        if (object instanceof JSONObject) {
            payOrder = (JSONObject) object;
        }

        if (payOrder == null) {
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心下单失败", null, null));
        }

        // 生成支付流水
        String result = payOrderServiceClient.createPayOrder(payOrder.toJSONString());
        logger.info("创建支付订单,结果:{}", result);
        if (org.apache.commons.lang.StringUtils.isEmpty(result)) {
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "创建支付订单失败", null, null));
        }


        return null;
    }


}
