package com.hz.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayResponse;
import com.hz.common.constant.PayConstant;
import com.hz.common.enums.PayEnum;
import com.hz.common.util.MyBase64;
import com.hz.common.util.XXPayUtil;
import com.hz.pay.model.MchInfo;
import com.hz.pay.model.PayChannel;
import com.hz.pay.model.PayOrder;
import com.hz.pay.service.AlipayService;
import com.hz.pay.service.IMchInfoService;
import com.hz.pay.service.IPayChannelService;
import com.hz.pay.service.IPayOrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付宝支付
 */
@RestController
public class AlipayPaymentController {

    private Logger logger = LoggerFactory.getLogger(AlipayPaymentController.class);

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private IMchInfoService iMchInfoService;

    @Autowired
    private IPayChannelService iPayChannelService;

    @Autowired
    private IPayOrderService iPayOrderService;

    @RequestMapping(value = "/pay/channel/ali_wap")
    public String doAliPayWapReq(@RequestParam String jsonParam) {
        logger.info("接受到支付宝手机网站支付请求，参数：{}....",jsonParam);
        JSONObject paramObj = JSON.parseObject(jsonParam);
        PayOrder payOrder = paramObj.getObject("payOrder", PayOrder.class);
        String payOrderId = payOrder.getPayOrderId();
        String mchId = payOrder.getMchId();
        String channelId = payOrder.getChannelId();

        //获取商户信息
        MchInfo mchInfo = iMchInfoService.selectMchInfo(mchId);
        String resKey = mchInfo == null ? "" : mchInfo.getResKey();

        //判断key是否存在
        if(StringUtils.isEmpty(resKey)){
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }

        //判断支付渠道是否存在
        PayChannel payChannel = iPayChannelService.selectPayChannel(channelId, mchId);
        if(ObjectUtils.isEmpty(payChannel)){
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "", PayConstant.RETURN_VALUE_FAIL, PayEnum.ERR_0001));
        }

        String payUrl = alipayService.getAlipayWApUrl(payOrder);

        //把流水更新为 支付中
        if(!StringUtils.isEmpty(payUrl)){
            iPayOrderService.updatePayOrder(payOrder);
        }

        Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
        map.put("payOrderId", payOrderId);
        map.put("payUrl", MyBase64.encode(payUrl.getBytes()));
        logger.info("支付宝手机网站支付返回的参数：{}....",XXPayUtil.makeRetData(map, resKey));
        return XXPayUtil.makeRetData(map, resKey);

    }

}
