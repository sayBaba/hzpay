package com.hz.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.hz.common.constant.PayConstant;
import com.hz.pay.model.PayOrder;
import com.hz.pay.mq.MQConfig;
import com.hz.pay.mq.NotifyMchMessage;
import com.hz.pay.service.IPayOrderService;
import com.hz.pay.utils.VerifyAliPayRespData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝异步通知入口
 */
@RestController
public class AlipayNotifyController {

    private Logger logger = LoggerFactory.getLogger(AlipayNotifyController.class);

    @Autowired
    private VerifyAliPayRespData verifyAliPayRespData;

    @Autowired
    private IPayOrderService iPayOrderService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 支付宝移动支付后台通知响应
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/notify/payResult.htm")
    public void aliPayNotifyRes(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        PrintWriter printWriter = null;
        try {
             printWriter = response.getWriter();
        } catch (IOException e) {
            logger.error("IOException",e);
        }

//        String result =  doAliPayRes(request, response);
        //从request中获取支付宝，回调的参数
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

//        String pbKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzJnbTT7sBQJTZvp3gGztei1V2eONrrbhxuPHojkAFTQzGE7nsWL2/TvVbOJihCq8JQtU9gSXBedNePetNLz4R7eMcZztTV9M9kxxwB5TKxjbI3l9DFDj3Q9sOUq8F1Afy8XiBfYdqvv+Haz4AWDdo6EljvXY6amrXbyBralIyXC/7exOqLs17/gx4DInfdf8ophOFbRSYbQCcRbDyxPdqT7mUY9ozfmoWaj/acjbH2gGGY26ptF9bDtkrPYLPgeIUNqYU1LsWgqqDxhL5eDYIGGvPMr5aFq9s29BjEAWdoDDAnUt8R0azhc1A6I1ONVspQToxPAMMVaYAirb1EXrQIDAQAB";
//
//        boolean signVerified = false; //调用SDK验证签名
//        try {
//             signVerified = AlipaySignature.rsaCheckV1(params, pbKey, "utf-8", "RSA2"); //调用SDK验证签名
//            System.err.println("------signVerified-----"+ signVerified);
//
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
        logger.info("接受到支付宝的异步回调参数参数:params={}", params);

        //1.参数为空响应支付宝fail
        if(params.isEmpty()) {
            logger.error("{}请求参数为空");
            printWriter.println("fail");
            return;
        }

        //2.判断支付宝返回的参数是否正常
        Map<String, Object> payContext = new HashMap();
        payContext.put("parameters", params);
        if(!verifyAliPayRespData.verifyAliPayParams(payContext)) {
            logger.info("--------支付宝验证签名失败---");
//            printWriter.println("fail");
//            return;
        }else{
            logger.info("------支付宝验证签名成功-----");
        }

        //3.更新交易订单的状态
        String trade_status = params.get("trade_status");// 交易状态

        PayOrder payOrder = null;
        if (trade_status.equals(PayConstant.AlipayConstant.TRADE_STATUS_SUCCESS) ||
                trade_status.equals(PayConstant.AlipayConstant.TRADE_STATUS_FINISHED)) {

            int updatePayOrderRows;
             payOrder = (PayOrder)payContext.get("payOrder");

            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                updatePayOrderRows = iPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    logger.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    logger.info("{}响应给支付宝结果：{}", PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                    printWriter.println("success");
                }

                logger.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }

        }else{
            // 其他状态
            logger.info("支付状态trade_status={},不做业务处理", trade_status);
            logger.info("响应给支付宝结果：{}", PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
            printWriter.println("success");
        }

        // 4.mq异步通知业务系统shop.
        if(!ObjectUtils.isEmpty(payOrder)){
            NotifyMchMessage mchMessage = new NotifyMchMessage();
            mchMessage.setPayId(payOrder.getMchOrderNo());
            mchMessage.setPayOrder(payOrder);
            amqpTemplate.convertAndSend(MQConfig.NOTIFY_MCH_QUEUE,mchMessage);
        }

//        doNotify(payOrder);
        logger.info("====== 完成接收支付宝支付回调通知 ======");

    }





}
