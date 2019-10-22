package com.hz.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
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

/**
 * 支付接口
 */
@RestController
@RequestMapping("/pay")
public class PayOrderController {

    private Logger logger = LoggerFactory.getLogger(PayOrderController.class);

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private ParamasCheck paramasCheck;

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
        logger.info("###### 开始接收商户统一下单请求 ######，请求参数：{}",params);
        String instance = client.description();
        if (StringUtils.isEmpty(params)){
            return null;
        }
        JSONObject po = JSONObject.parseObject(params);

        paramasCheck.validateParams(po);

        return null;
    }

}
