package com.hz.shop.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接收hzpay-service的回调
 */
@RestController
@RequestMapping("/revive")
public class HzpayNoticeController {

    private Logger logger = LoggerFactory.getLogger(HzpayNoticeController.class);

    /**
     * hzpay通知入口
     * @return
     */
    @RequestMapping("/notice")
    public String recivePayNotice(@RequestParam String params){
        logger.info("###### 接受到HZPAY的回调请求 ######，请求参数：{}", params);
        if(StringUtils.isEmpty(params)){
            return "fail";
        }
        //更新商户订单
        JSONObject object = JSONObject.parseObject(params);
//      {"goodsOrderId":"G20191025162033000002","amount":1,"mchId":"10000001","sign":"7FF0B8A68A27E467C91955E0E4305025","channelId":"ALIPAY_WAP","status":2}

        //验证sign
        String sgin = object.get("sign").toString();
        boolean flag = true;
        if (!flag){
            return "fail";
        }

        //1.判断交易订单号是否存在

        //2.判断交易订单号，和金额是否存在

        //3.更新t_goods_order订单的状态。

        return "fail";
    }


}
