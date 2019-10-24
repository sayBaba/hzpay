package com.hz.pay.service;

import com.alibaba.fastjson.JSONObject;
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
    public String getAlipayWApUrl(PayOrder payOrder) {

//        AlipayClient client = new DefaultAlipayClient(alipayConfig.getAlipayUrl(), alipayConfig.getAppId(),
//                alipayConfig.getPrivateKey(), "json", "utf-8", alipayConfig.getPublicKey(), "RSA2");

        String apiUrl = "https://openapi.alipaydev.com/gateway.do";
        String appid = "2016101200667654";
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDNVlSolnd0tCd0sBHufukJ6p2o2UcH1nLxhcvDxENwlBoEHgkLfM1gwMtWp86LVkkSrODp+RdfLSq1tX3XYKBxl5Y9Y96keLwXwAbSQPbEKH0gpwb3G9SFP7piGP//Wva1zyhN/vEm+aBhhegAvVI0v24HwSvqT0KTSX5RrQyxOBI77dDFTaYSBSFOIHgGWtLPZrADkNchHuJM8mq5jIBIzQh9YGoMD0T5X5Lnl1NBR14N8hvSGQr3ZLKcRa4VuEkGvJE8BCQv/9XYNP4jPzhlk2QPVwIaf1q+qf4ADAsyRACdg8Ij+XI55tNKujy3DCm42FWrGW9d9AvGHs05SXpRAgMBAAECggEAJiYz9F6AMx6FdhiAPF83zK/aOtSjbcFZ+aVyBh0gqnnZm4o5A/lb7u3i2Waz6Q9O5DlaUB4HSZeKp9fgWXOKW5BeSWmUo9oKmB8FJWsMbcVX+gkOMjrpcRg1+KPMbBTqYTzrsBH9m9AlC/lk8T47V7F5L9ej4RR4ugSdg28zPHlo4xLqbQHsjvIzkaoOH1cL07rdRHI7sap69a0Cz0lTUS42g45e1vzuynEkx+M9rreEkCbagISZJ2J80YTjoOzT1jzmbyzxVp61vRanCNJxHhZ/QmO/+adNso3vjbxC/t/qRMhPN+xTsdUe0VVz+j6Jj+rngPJctzPIxThpGi3UQQKBgQD6ckXxcDEqkIeNcCU3mbxqL8Z0SkgpeUXjCrfrHv2ZYTEdidwWM3INJrwsrEmLPzd5GdYGudFm0ExqbAjddpZq/lLHc8ZpPfAOjVaTpsK/GpbB8Iv3fh6J0gckj2o/AP/xCQirv8YJmxNoxA38eROUnpl6TuNH5s2Ai2rW4oNsdwKBgQDR4/uwPPedIKIxOIXaTDc8VCG541gmV1F/wpDGU1KhuNE+V1Mprv1OivyPwVvK/pa4uVYONj0CbdSTy0gk74WthuJKCGwkgAVtOcnPFK0K8VgwkRsOB4ldqyFKj6szkcBWIAhKh0evTZF32Koe8eRRhMpOfv1aD4dtb9RKuIUpdwKBgAeU7gkhHFvthC0D+Bx2yv1r9NYQaZ7XzQXSjT7Q8VxzVMlQIxmI9SrwwUGY9aEBiOxhm9kYRv6Vz7ppCtupe2RBzr+7AYauGVZXcQeHRN4EMAwu/A3CA6qocQ7qSI0fup/RZjHM3HHt/+hsBwBdqfoV0w+g08CZfB8oGVPoB5KBAoGBALh7HWa7P5zA6S5AEw5bfRUukXI0i+8vQnf8CIn5BKEWs/uKf5MctpJJgVHNTyQt/0YfyK63J4qyBG1e+GtWh/WMn+sYWgz+5UYqy+GSt8HtfWk6sT/0id1lo95IRt8N1bMddowjOXOwxxJCwsxuKhMolzTFU2PMUGl1KHAAnTJjAoGAbz4icYpAb9oum2zNrhdJRvUkF875tHC/ckcnllaG7vHHJd2K96NNFzy1bjnVcvx23a43JFMD2AcL7XF+MkrRoqmlEZ4R0iI2q8XBmEUMbKQzFodv74r8huptBpJyt3brti+dD0gMODr7KLO89IKV4WGAg2PyOwDTMtRK1LNaKCA=";
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzVZUqJZ3dLQndLAR7n7pCeqdqNlHB9Zy8YXLw8RDcJQaBB4JC3zNYMDLVqfOi1ZJEqzg6fkXXy0qtbV912CgcZeWPWPepHi8F8AG0kD2xCh9IKcG9xvUhT+6Yhj//1r2tc8oTf7xJvmgYYXoAL1SNL9uB8Er6k9Ck0l+Ua0MsTgSO+3QxU2mEgUhTiB4BlrSz2awA5DXIR7iTPJquYyASM0IfWBqDA9E+V+S55dTQUdeDfIb0hkK92SynEWuFbhJBryRPAQkL//V2DT+Iz84ZZNkD1cCGn9avqn+AAwLMkQAnYPCI/lyOebTSro8twwpuNhVqxlvXfQLxh7NOUl6UQIDAQAB";



        AlipayClient client = new DefaultAlipayClient(apiUrl, appid, privateKey,"json", "utf-8", pubKey, "RSA2");

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl("http://hzpay777.vipgz2.idcfengye.com/notify/payResult.htm"); //后端异步通知，更新交易订单。
        request.setReturnUrl("http://hzpay666.free.idcfengye.com/shop/showCode"); //支付完成，跳转此页面

        JSONObject bizContent = new JSONObject();
        bizContent.put("body", payOrder.getBody()); //可空
        bizContent.put("timeout_express", "");
        bizContent.put("time_expire", "");
        bizContent.put("auth_token", "");
        bizContent.put("goods_type", "");
        bizContent.put("passback_params", "");
        bizContent.put("promo_params", "");
        bizContent.put("extend_params", "");
        bizContent.put("merchant_order_no", "");
        bizContent.put("enable_pay_channels", "");
        bizContent.put("disable_pay_channels", "");
        bizContent.put("store_id", "");
        bizContent.put("specified_channel", "");
        bizContent.put("business_params", "");
        bizContent.put("ext_user_info", "");
        /***************必传******************/
        bizContent.put("subject", payOrder.getSubject());
        bizContent.put("out_trade_no", payOrder.getPayOrderId()); //流水
        bizContent.put("total_amount", AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));//金额
        bizContent.put("quit_url", "www.baidu.com");
        bizContent.put("product_code", "QUICK_WAP_WAY"); //手机网页wap支付
        request.setBizContent(bizContent.toJSONString());
        try {
            AlipayResponse alipayResponse = client.pageExecute(request);
            System.out.println("----------------------");
            System.out.println("code = " + alipayResponse.getCode());
            System.out.println("msg = " + alipayResponse.getMsg());
            String body = alipayResponse.getBody();
            System.out.println("body = " + alipayResponse.getBody());
            return body;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }


}
