package com.hz.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.common.util.MyBase64;
import com.hz.pay.model.PayChannel;
import com.hz.pay.service.IPayChannelService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户支付渠道相关接口
 */
@RestController
public class PayChannelServiceController {

    private Logger logger = LoggerFactory.getLogger(MchInfoServiceController.class);

    @Autowired
    private IPayChannelService payChannelService;

    @RequestMapping(value = "/pay_channel/select")
    public String selectPayChannel(@RequestParam String jsonParam) {
        logger.info("查询商户支付渠道请求参数为：{},start", jsonParam);
        JSONObject retObj = new JSONObject();
        if(StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001"); // 参数错误
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }

        JSONObject paramObj = JSON.parseObject(jsonParam);
        String channelId = paramObj.getString("channelId");
        String mchId = paramObj.getString("mchId");
        PayChannel payChannel = payChannelService.selectPayChannel(channelId, mchId);
        if(payChannel == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "数据对象不存在");
            return retObj.toJSONString();
        }
        retObj.put("code", "0000");
        retObj.put("result", JSON.toJSON(payChannel));
        logger.info("查询商户支付渠道请求返回参数为：{},end.", retObj);
        return retObj.toJSONString();
    }


}
