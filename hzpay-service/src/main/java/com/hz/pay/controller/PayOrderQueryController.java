package com.hz.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.hz.common.resp.PayQueryData;
import com.hz.common.resp.PayQueryResp;
import com.hz.pay.model.PayOrder;
import com.hz.pay.service.IPayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付查询结果
 */
@RestController
public class PayOrderQueryController {

    private Logger logger = LoggerFactory.getLogger(PayOrderQueryController.class);

    @Autowired
    private IPayOrderService iPayOrderService;

    /**
     * 交易订单查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/payRlt/select")
    public PayQueryResp selectMchInfo(@RequestParam String jsonParam) {

        PayQueryResp payQueryResp = new PayQueryResp();
        logger.info("接收到交易查询请求：{}",jsonParam);
        //请求参数判断 TODO
        JSONObject obj = new JSONObject();
        if(!StringUtils.isEmpty(jsonParam)){
            obj = JSONObject.parseObject(jsonParam);
        }

        PayOrder payOrder = iPayOrderService.getPayOrderBymchOrderId(obj.getString("mchId"),obj.getString("mchOrderId"));
        if (ObjectUtils.isEmpty(payOrder)){
            payQueryResp.setCode("9999");
            payQueryResp.setMsg("查询失败，没有找到记录");
            return payQueryResp;
        }
        PayQueryData payQueryData = new PayQueryData();
        //把前者的值赋给后者
        BeanUtils.copyProperties(payOrder,payQueryData);

        payQueryResp.setCode("0000");
        payQueryResp.setMsg("请求成功");
        payQueryResp.setData(payQueryData);
        return payQueryResp;
    }

}
