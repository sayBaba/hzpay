package com.hz.shop;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopApplicationTests {

    String apiUrl = "https://openapi.alipaydev.com/gateway.do";
    String appid = "2016101200667654";
    String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDNVlSolnd0tCd0sBHufukJ6p2o2UcH1nLxhcvDxENwlBoEHgkLfM1gwMtWp86LVkkSrODp+RdfLSq1tX3XYKBxl5Y9Y96keLwXwAbSQPbEKH0gpwb3G9SFP7piGP//Wva1zyhN/vEm+aBhhegAvVI0v24HwSvqT0KTSX5RrQyxOBI77dDFTaYSBSFOIHgGWtLPZrADkNchHuJM8mq5jIBIzQh9YGoMD0T5X5Lnl1NBR14N8hvSGQr3ZLKcRa4VuEkGvJE8BCQv/9XYNP4jPzhlk2QPVwIaf1q+qf4ADAsyRACdg8Ij+XI55tNKujy3DCm42FWrGW9d9AvGHs05SXpRAgMBAAECggEAJiYz9F6AMx6FdhiAPF83zK/aOtSjbcFZ+aVyBh0gqnnZm4o5A/lb7u3i2Waz6Q9O5DlaUB4HSZeKp9fgWXOKW5BeSWmUo9oKmB8FJWsMbcVX+gkOMjrpcRg1+KPMbBTqYTzrsBH9m9AlC/lk8T47V7F5L9ej4RR4ugSdg28zPHlo4xLqbQHsjvIzkaoOH1cL07rdRHI7sap69a0Cz0lTUS42g45e1vzuynEkx+M9rreEkCbagISZJ2J80YTjoOzT1jzmbyzxVp61vRanCNJxHhZ/QmO/+adNso3vjbxC/t/qRMhPN+xTsdUe0VVz+j6Jj+rngPJctzPIxThpGi3UQQKBgQD6ckXxcDEqkIeNcCU3mbxqL8Z0SkgpeUXjCrfrHv2ZYTEdidwWM3INJrwsrEmLPzd5GdYGudFm0ExqbAjddpZq/lLHc8ZpPfAOjVaTpsK/GpbB8Iv3fh6J0gckj2o/AP/xCQirv8YJmxNoxA38eROUnpl6TuNH5s2Ai2rW4oNsdwKBgQDR4/uwPPedIKIxOIXaTDc8VCG541gmV1F/wpDGU1KhuNE+V1Mprv1OivyPwVvK/pa4uVYONj0CbdSTy0gk74WthuJKCGwkgAVtOcnPFK0K8VgwkRsOB4ldqyFKj6szkcBWIAhKh0evTZF32Koe8eRRhMpOfv1aD4dtb9RKuIUpdwKBgAeU7gkhHFvthC0D+Bx2yv1r9NYQaZ7XzQXSjT7Q8VxzVMlQIxmI9SrwwUGY9aEBiOxhm9kYRv6Vz7ppCtupe2RBzr+7AYauGVZXcQeHRN4EMAwu/A3CA6qocQ7qSI0fup/RZjHM3HHt/+hsBwBdqfoV0w+g08CZfB8oGVPoB5KBAoGBALh7HWa7P5zA6S5AEw5bfRUukXI0i+8vQnf8CIn5BKEWs/uKf5MctpJJgVHNTyQt/0YfyK63J4qyBG1e+GtWh/WMn+sYWgz+5UYqy+GSt8HtfWk6sT/0id1lo95IRt8N1bMddowjOXOwxxJCwsxuKhMolzTFU2PMUGl1KHAAnTJjAoGAbz4icYpAb9oum2zNrhdJRvUkF875tHC/ckcnllaG7vHHJd2K96NNFzy1bjnVcvx23a43JFMD2AcL7XF+MkrRoqmlEZ4R0iI2q8XBmEUMbKQzFodv74r8huptBpJyt3brti+dD0gMODr7KLO89IKV4WGAg2PyOwDTMtRK1LNaKCA=";
    String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzVZUqJZ3dLQndLAR7n7pCeqdqNlHB9Zy8YXLw8RDcJQaBB4JC3zNYMDLVqfOi1ZJEqzg6fkXXy0qtbV912CgcZeWPWPepHi8F8AG0kD2xCh9IKcG9xvUhT+6Yhj//1r2tc8oTf7xJvmgYYXoAL1SNL9uB8Er6k9Ck0l+Ua0MsTgSO+3QxU2mEgUhTiB4BlrSz2awA5DXIR7iTPJquYyASM0IfWBqDA9E+V+S55dTQUdeDfIb0hkK92SynEWuFbhJBryRPAQkL//V2DT+Iz84ZZNkD1cCGn9avqn+AAwLMkQAnYPCI/lyOebTSro8twwpuNhVqxlvXfQLxh7NOUl6UQIDAQAB";


    @Test
    void contextLoads() {
    }

    /**
     * 手机wap支付
     * @throws AlipayApiException
     */
    @Test
    public void testAlipayWap() throws AlipayApiException {


        AlipayClient client = new DefaultAlipayClient(apiUrl, appid, privateKey,"json", "utf-8", pubKey, "RSA2");

        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(String.valueOf(System.currentTimeMillis())); //生成请求流水
        model.setSubject("测试"); // 商品的标题/交易标题/订单标题/订单关键字等。
        model.setTotalAmount("1.00"); //金额
        model.setBody("测试");
        model.setProductCode("QUICK_WAP_PAY");
        // 获取objParams参数

        model.setQuitUrl("www.baidu.com");

        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl("www.baidu.com");
        // 设置同步地址
        alipay_request.setReturnUrl("www.baidu.com");

        try {
            AlipayResponse alipayResponse =  client.pageExecute(alipay_request);
            System.out.println("----------------------");
            System.out.println("code = "+alipayResponse.getCode());
            System.out.println("msg = "+alipayResponse.getMsg());
            System.out.println("body = "+alipayResponse.getBody());

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫码支付
     */
    @Test
    public void testAli(){

        AlipayClient client = new DefaultAlipayClient(apiUrl, appid, privateKey,"json", "utf-8", pubKey, "RSA2");
        AlipayTradePagePayRequest alipay_request = new AlipayTradePagePayRequest();
        // 封装请求支付信息
        AlipayTradePagePayModel model=new AlipayTradePagePayModel();
        model.setOutTradeNo("P0020191023093200000003");
        model.setSubject("测试");
        model.setTotalAmount("1.00");
        model.setBody("测试111");
        model.setProductCode("QUICK_WAP_WAY");
        // 获取objParams参数
//        String objParams = payOrder.getExtra();
        String qr_pay_mode = "2";
        String qrcode_width = "200";

        model.setQrPayMode(qr_pay_mode);
        model.setQrcodeWidth(Long.parseLong(qrcode_width));
        alipay_request.setBizModel(model);

        // 设置异步通知地址
        alipay_request.setNotifyUrl("www.baidu.com");
        // 设置同步地址
        alipay_request.setReturnUrl("www.baidu.com");

        try {
            AlipayResponse alipayResponse =  client.pageExecute(alipay_request);
            System.out.println("----------------------");
            System.out.println("code = "+alipayResponse.getCode());
            System.out.println("msg = "+alipayResponse.getMsg());
            System.out.println("body = "+alipayResponse.getBody());



        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void test01(){
        AlipayClient client = new DefaultAlipayClient(apiUrl, appid, privateKey,"json", "utf-8", pubKey, "RSA2");

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("body","测试"); //可空
        bizContent.put("timeout_express","");
        bizContent.put("time_expire","");
        bizContent.put("auth_token","");
        bizContent.put("goods_type","");
        bizContent.put("passback_params","");
        bizContent.put("promo_params","");
        bizContent.put("extend_params","");
        bizContent.put("merchant_order_no","");
        bizContent.put("enable_pay_channels","");
        bizContent.put("disable_pay_channels","");
        bizContent.put("store_id","");
        bizContent.put("specified_channel","");
        bizContent.put("business_params","");
        bizContent.put("ext_user_info","");
        /***************必传******************/
        bizContent.put("subject","测试商品");
        bizContent.put("out_trade_no",System.currentTimeMillis()); //流水
        bizContent.put("total_amount","1.00");//金额
        bizContent.put("quit_url","www.baidu.com");
        bizContent.put("product_code","QUICK_WAP_WAY"); //手机网页wap支付
        request.setBizContent(bizContent.toJSONString());

        try {
            AlipayResponse alipayResponse =  client.pageExecute(request);
            System.out.println("----------------------");
            System.out.println("code = "+alipayResponse.getCode());
            System.out.println("msg = "+alipayResponse.getMsg());
            System.out.println("body = "+alipayResponse.getBody());

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * 支付宝交易查询
     */
    @Test
    public void test02() throws AlipayApiException {

        String pubKey1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzJnbTT7sBQJTZvp3gGztei1V2eONrrbhxuPHojkAFTQzGE7nsWL2/TvVbOJihCq8JQtU9gSXBedNePetNLz4R7eMcZztTV9M9kxxwB5TKxjbI3l9DFDj3Q9sOUq8F1Afy8XiBfYdqvv+Haz4AWDdo6EljvXY6amrXbyBralIyXC/7exOqLs17/gx4DInfdf8ophOFbRSYbQCcRbDyxPdqT7mUY9ozfmoWaj/acjbH2gGGY26ptF9bDtkrPYLPgeIUNqYU1LsWgqqDxhL5eDYIGGvPMr5aFq9s29BjEAWdoDDAnUt8R0azhc1A6I1ONVspQToxPAMMVaYAirb1EXrQIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient(apiUrl,appid,privateKey,"json","GBK",pubKey1,"RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no","P0020191028103659000000");
        request.setBizContent(jsonObject.toJSONString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);

        System.err.println(JSON.toJSONString(response));


        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 支付宝退款
     */
    @Test
    public void test03() throws AlipayApiException {
        String pubKey1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzJnbTT7sBQJTZvp3gGztei1V2eONrrbhxuPHojkAFTQzGE7nsWL2/TvVbOJihCq8JQtU9gSXBedNePetNLz4R7eMcZztTV9M9kxxwB5TKxjbI3l9DFDj3Q9sOUq8F1Afy8XiBfYdqvv+Haz4AWDdo6EljvXY6amrXbyBralIyXC/7exOqLs17/gx4DInfdf8ophOFbRSYbQCcRbDyxPdqT7mUY9ozfmoWaj/acjbH2gGGY26ptF9bDtkrPYLPgeIUNqYU1LsWgqqDxhL5eDYIGGvPMr5aFq9s29BjEAWdoDDAnUt8R0azhc1A6I1ONVspQToxPAMMVaYAirb1EXrQIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient(apiUrl,appid,privateKey,"json","GBK",pubKey1,"RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("refund_amount", 0.01);
//        jsonObject.put("refund_currency","cny");
        jsonObject.put("out_request_no",String.valueOf(System.currentTimeMillis()));
        jsonObject.put("out_trade_no","P0020191028103659000000");
        request.setBizContent(jsonObject.toJSONString());
        AlipayTradeRefundResponse response = alipayClient.execute(request);

        System.err.println(JSON.toJSONString(response));

    }

}
