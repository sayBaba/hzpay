package com.hz.pay.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.common.req.RefundOrderReq;
import com.hz.common.resp.PayQueryData;
import com.hz.common.resp.PayQueryResp;
import com.hz.common.resp.RefundOrderResp;
import com.hz.common.util.XXPayUtil;
import com.hz.pay.web.service.MchInfoServiceClient;
import com.hz.pay.web.service.PayOrderServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款接口
 */
@RestController
public class RefundOrderController {

    private Logger logger = LoggerFactory.getLogger(RefundOrderController.class);

    @Autowired
    private MchInfoServiceClient mchInfoServiceClient;

    @Autowired
    private PayOrderServiceClient payOrderServiceClient;




    /**
     * 退款接口
     * @return
     */
    @RequestMapping("/refund/order")
    public RefundOrderResp refundOrder(@Validated RefundOrderReq refundOrderReq){
        logger.info("*****接受到业务系统的退款请求，参数：{}*****",refundOrderReq);
        RefundOrderResp refundOrderResp = new RefundOrderResp();

        //判断商户否存在
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("mchId", refundOrderReq.getMchId());
        String mchRlt = mchInfoServiceClient.selectMchInfo(jsonParam.toJSONString());
        if (StringUtils.isEmpty(mchRlt)){
            logger.info("商户订单号：{}商户id不存在",refundOrderReq.getMchId());
            refundOrderResp.setCode("9999");
            refundOrderResp.setMsg("商户id不存在");
            return refundOrderResp;
        }

        //判断支付渠道是否存在
        String rlt = payOrderServiceClient.selectPayChannel(getJsonParam(new String[]{"channelId", "mchId"}, new String[]{channelId, mchId}));
        if (StringUtils.isEmpty(rlt)){
            logger.info("商户号：{}channelId不存在",refundOrderReq.getMchId());
            refundOrderResp.setCode("9999");
            refundOrderResp.setMsg("channelId不存在");
            return refundOrderResp;
        }
        JSONObject retObj = JSON.parseObject(mchRlt);

        JSONObject params = new JSONObject();
        params.put("mchId",refundOrderReq.getMchId());
        params.put("mchOrderNo",refundOrderReq.getMchOrderNo());
        params.put("channelId",refundOrderReq.getChannelId());

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, retObj.getString("ReqKey"));
        if(!verifyFlag) {
            logger.info("商户号：{}验证签名失败",refundOrderReq.getMchId());
            refundOrderResp.setCode("9999");
            refundOrderResp.setMsg("验证签名失败");
            return refundOrderResp;
        }

        //根据商户订单号查询交易订单
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mchId",refundOrderReq.getMchId());
        jsonObject.put("mchOrderNo",refundOrderReq.getMchOrderNo());
        PayQueryResp payQueryResp = payOrderServiceClient.queryPayOrder(jsonObject.toJSONString());
        if(ObjectUtils.isEmpty(payQueryResp)){
            logger.info("商户号：{}退款失败：没有找到交易订单",refundOrderReq.getMchId());
            refundOrderResp.setCode("9999");
            refundOrderResp.setMsg("没有找到交易订单");
            return refundOrderResp;
        }
        if(!"0000".equals(payQueryResp.getCode())){
            logger.info("商户号：{}退款失败：{}",refundOrderReq.getMchId(),payQueryResp.getMsg());
            refundOrderResp.setCode("9999");
            refundOrderResp.setMsg("查询失败");
            return refundOrderResp;
        }

        PayQueryData payQueryData = (PayQueryData) payQueryResp.getData();
        if(ObjectUtils.isEmpty(payQueryData)){
            logger.info("商户号：{}退款失败：{}",refundOrderReq.getMchId(),payQueryResp.getMsg());
            refundOrderResp.setCode("9999");
            refundOrderResp.setMsg("查询失败");
            return refundOrderResp;
        }

        long refundAmt = Long.parseLong(refundOrderReq.getAmount()); //退款金额
        if(refundAmt> payQueryData.getAmount()){
            logger.info("商户号：{}退款失败：退款金额超过支付金额",refundOrderReq.getMchId());
            refundOrderResp.setCode("9999");
            refundOrderResp.setMsg("退款失败");
            return refundOrderResp;
        }

        //生成退款订单
        refundOrderReq.setPayOrderId(payQueryData.getPayOrderId());
        payOrderServiceClient.createRefundOrder(refundOrderReq);
        refundOrderResp.setCode("0000");
        refundOrderResp.setMsg("退款申请中");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("status","0");
        jsonObject1.put("desc","退款申请中");
        refundOrderResp.setData(jsonObject1);

        return refundOrderResp;
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
