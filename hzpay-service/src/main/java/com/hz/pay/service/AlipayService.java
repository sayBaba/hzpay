package com.hz.pay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.hz.common.util.AmountUtil;
import com.hz.pay.config.AlipayConfig;
import com.hz.pay.controller.AlipayPaymentController;
import com.hz.pay.model.PayOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 封装支付宝各个支付方式的请求参数
 */
@Service
public class AlipayService {

    private Logger logger = LoggerFactory.getLogger(AlipayService.class);

    @Autowired
    private AlipayConfig  alipayConfig;

    /**
     * 支付宝wap支付
     */
    public String getAlipayWApUrl(PayOrder payOrder){

        AlipayClient client = new DefaultAlipayClient(alipayConfig.getAlipayUrl(), alipayConfig.getAppId(),
                alipayConfig.getPrivateKey(),"json", "utf-8", alipayConfig.getPublicKey(), "RSA2");
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(String.valueOf(System.currentTimeMillis())); //生成请求流水
        model.setSubject(payOrder.getSubject()); // 商品的标题/交易标题/订单标题/订单关键字等。
        model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString())); // 支付宝接口为元，系统为分。
        model.setBody(payOrder.getBody());
        model.setProductCode("QUICK_WAP_PAY"); //支付宝产品码
        // 获取objParams参数
        model.setQuitUrl("www.baidu.com"); //支付中段跳转的页面

        alipay_request.setBizModel(model);

        // 设置异步通知地址
        String notifyUrl = "http://hzpay666.free.idcfengye.com"+"/pay/aliPayNotifyRes.htm";
        alipay_request.setNotifyUrl(notifyUrl);  ///属于后端通知，我们根据返回的参数去更新交易状态。
        // 设置同步地址
        alipay_request.setReturnUrl("www.baidu.com"); //页面跳转

        try {
            AlipayResponse alipayResponse =  client.pageExecute(alipay_request);
            return alipayResponse.getBody();
        } catch (AlipayApiException e) {
            logger.error("AlipayApiException",e);
            return null;
        }

    }


}
