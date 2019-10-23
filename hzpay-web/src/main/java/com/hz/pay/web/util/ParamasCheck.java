package com.hz.pay.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.common.util.MySeq;
import com.hz.common.util.XXPayUtil;
import com.hz.pay.web.service.MchInfoServiceClient;
import com.hz.pay.web.service.PayOrderServiceClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 支付请求参数校验
 */
@Component
public class ParamasCheck {

    private static Logger logger = LoggerFactory.getLogger(ParamasCheck.class);

    @Autowired
    private MchInfoServiceClient mchInfoServiceClient;

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;


    /**
     * 支付请求参数校验
     * @param params
     * @return
     */

    public Object validateParams(JSONObject params) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage = null;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户订单号
        String channelId = params.getString("channelId"); 	    // 渠道ID
        String amount = params.getString("amount"); 		    // 支付金额（单位分）
        String currency = params.getString("currency");         // 币种
        String clientIp = params.getString("clientIp");	        // 客户端IP
        String device = params.getString("device"); 	        // 设备
        String extra = params.getString("extra");		        // 特定渠道发起时额外参数
        String param1 = params.getString("param1"); 		    // 扩展参数1
        String param2 = params.getString("param2"); 		    // 扩展参数2
        String notifyUrl = params.getString("notifyUrl"); 		// 支付结果回调URL
        String sign = params.getString("sign"); 				// 签名
        String subject = params.getString("subject");	        // 商品主题
        String body = params.getString("body");	                // 商品描述信息

        // 验证请求参数有效性（必选项）
        if(StringUtils.isBlank(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(mchOrderNo)) {
            errorMessage = "request params[mchOrderNo] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(channelId)) {
            errorMessage = "request params[channelId] error.";
            return errorMessage;
        }
        if(!NumberUtils.isNumber(amount)) {
            errorMessage = "request params[amount] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(currency)) {
            errorMessage = "request params[currency] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(notifyUrl)) {
            errorMessage = "request params[notifyUrl] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(subject)) {
            errorMessage = "request params[subject] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(body)) {
            errorMessage = "request params[body] error.";
            return errorMessage;
        }

        // 根据不同渠道,判断extra参数
        /**

         if(PayConstant.PAY_CHANNEL_WX_JSAPI.equalsIgnoreCase(channelId)) {
         if(StringUtils.isEmpty(extra)) {
         errorMessage = "request params[extra] error.";
         return errorMessage;
         }
         JSONObject extraObject = JSON.parseObject(extra);
         String openId = extraObject.getString("openId");
         if(StringUtils.isBlank(openId)) {
         errorMessage = "request params[extra.openId] error.";
         return errorMessage;
         }
         }else if(PayConstant.PAY_CHANNEL_WX_NATIVE.equalsIgnoreCase(channelId)) {
         if(StringUtils.isEmpty(extra)) {
         errorMessage = "request params[extra] error.";
         return errorMessage;
         }
         JSONObject extraObject = JSON.parseObject(extra);
         String productId = extraObject.getString("productId");
         if(StringUtils.isBlank(productId)) {
         errorMessage = "request params[extra.productId] error.";
         return errorMessage;
         }
         }else if(PayConstant.PAY_CHANNEL_WX_MWEB.equalsIgnoreCase(channelId)) {
         if(StringUtils.isEmpty(extra)) {
         errorMessage = "request params[extra] error.";
         return errorMessage;
         }
         JSONObject extraObject = JSON.parseObject(extra);
         String productId = extraObject.getString("sceneInfo");
         if(StringUtils.isBlank(productId)) {
         errorMessage = "request params[extra.sceneInfo] error.";
         return errorMessage;
         }
         if(StringUtils.isBlank(clientIp)) {
         errorMessage = "request params[clientIp] error.";
         return errorMessage;
         }
         }
         *
         */

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "request params[sign] error.";
            return errorMessage;
        }

        // 1.查询商户信息
        JSONObject mchInfo = null;
        String retStr = mchInfoServiceClient.selectMchInfo(getJsonParam("mchId", mchId)); //TODO springCloud 调用，查询商户信息
        //{code :"0000",msg"请求成功",result:{"":"","",""}}
        logger.info("商户id为：{},查询商户信息结果：{}",mchId,retStr);
        if(StringUtils.isEmpty(retStr)){
            return "查询商户没有正常返回数据";
        }
        JSONObject retObj = JSON.parseObject(retStr);

        //解析接口返回的数据
        if("0000".equals(retObj.getString("code"))) {
            mchInfo = retObj.getJSONObject("result");
            if (mchInfo == null) {
                errorMessage = "Can't found mchInfo[mchId="+mchId+"] record in db.";
                return errorMessage;
            }
            if(mchInfo.getByte("state") != 1) {
                errorMessage = "mchInfo not available [mchId="+mchId+"] record in db.";
                return errorMessage;
            }
        }else {
            errorMessage = "Can't found mchInfo[mchId="+mchId+"] record in db.";
            logger.info("查询商户没有正常返回数据,code={},msg={}", retObj.getString("code"), retObj.getString("msg"));
            return errorMessage;
        }
        String reqKey = mchInfo.getString("reqKey");

        if (StringUtils.isBlank(reqKey)) {
            errorMessage = "reqKey is null[mchId="+mchId+"] record in db.";
            return errorMessage;
        }

        // 2.查询商户对应的支付渠道
        JSONObject payChannel = null;
        retStr = payOrderServiceClient.selectPayChannel(getJsonParam(new String[]{"channelId", "mchId"}, new String[]{channelId, mchId}));
        logger.info("商户id为：{},查询对应的支付渠道结果：{}",mchId,retStr);

        retObj = JSON.parseObject(retStr);
        if("0000".equals(retObj.getString("code"))) {
            payChannel = JSON.parseObject(retObj.getString("result"));
            if(payChannel == null) {
                errorMessage = "Can't found payChannel[channelId="+channelId+",mchId="+mchId+"] record in db.";
                return errorMessage;
            }
            if(payChannel.getByte("state") != 1) {
                errorMessage = "channel not available [channelId="+channelId+",mchId="+mchId+"]";
                return errorMessage;
            }
        }else {
            errorMessage = "Can't found payChannel[channelId="+channelId+",mchId="+mchId+"] record in db.";
            logger.info("查询渠道没有正常返回数据,code={},msg={}", retObj.getString("code"), retObj.getString("msg"));
            return errorMessage;
        }

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, reqKey);
        if(!verifyFlag) {
            errorMessage = "Verify XX pay sign failed.";
            return errorMessage;
        }

        // 验证参数通过,返回JSONObject对象
        JSONObject payOrder = new JSONObject();
        payOrder.put("payOrderId", MySeq.getPay());
        payOrder.put("mchId", mchId);
        payOrder.put("mchOrderNo", mchOrderNo);
        payOrder.put("channelId", channelId);
        payOrder.put("amount", Long.parseLong(amount));
        payOrder.put("currency", currency);
        payOrder.put("clientIp", clientIp);
        payOrder.put("device", device);
        payOrder.put("subject", subject);
        payOrder.put("body", body);
        payOrder.put("extra", extra);
        payOrder.put("channelMchId", payChannel.getString("channelMchId"));
        payOrder.put("param1", param1);
        payOrder.put("param2", param2);
        payOrder.put("notifyUrl", notifyUrl);
        return payOrder;
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

    /**
     * 转成json字符串
     * @param name
     * @param value
     * @return
     */
    private String getJsonParam(String name, Object value) {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(name, value);
        return jsonParam.toJSONString();
    }

}
