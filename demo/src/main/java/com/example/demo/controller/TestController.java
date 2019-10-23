package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class TestController {


    @RequestMapping("/recPay")
    public ModelAndView recPay(Long amount, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();

//        String view = "qrPay";

        String payUrl = "<form name=\"punchout_form\" method=\"post\" action=\"\"https://openapi.alipaydev.com/gateway.do\";?charset=utf-8&method=alipay.trade.wap.pay&sign=cdVANkhORfXvCcaFeFmIeTEAc0QxNg9EohCeQ4lIy4SZrG7d4QLJwG%2FDlzp4D2xfm59pXErf%2BsoVUHn23bJJPRbxKPrDShWJ9B2b7dNyyja7JNwvR0hA%2Bp48Gzng7FsSUsZLfoZb19TAQDUCxvwM89dbq5YyAKI3JFRgq%2F9MI69ezTYN4reKdJbOBKttlkdpx5nejrT7%2FrsvI%2FX5uDBTeRjlnMEE5iBedQ%2BqW%2F10yMVmnhzx8hy5U%2BlmXQgGOh%2FOcU2hUPe5uwMzRzjnWTQa5phk3m2mZcFDn%2FbBi%2FphMPol6kj3cWau8yH90NmQ4%2F1uHCZeO5XWiRfDg1STW9xpIw%3D%3D&return_url=www.baidu.com&notify_url=http%3A%2F%2Fhzpay666.free.idcfengye.com%2Fpay%2FaliPayNotifyRes.htm&version=1.0&app_id=%222016101200667654%22%3B&sign_type=RSA2&timestamp=2019-10-23+16%3A12%3A14&alipay_sdk=alipay-sdk-java-4.8.10.ALL&format=json\">\n" +
                "<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;body&quot;:&quot;XXPAY捐助商品G_0001&quot;,&quot;out_trade_no&quot;:&quot;1571818334277&quot;,&quot;product_code&quot;:&quot;QUICK_WAP_PAY&quot;,&quot;quit_url&quot;:&quot;www.baidu.com&quot;,&quot;subject&quot;:&quot;XXPAY捐助商品G_0001&quot;,&quot;total_amount&quot;:&quot;0.01&quot;}\">\n" +
                "<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n" +
                "</form>\n" +
                "<script>document.forms[0].submit();</script>";

        HashMap orderMap = new HashMap();
//        orderMap.put("payUrl",payUrl);

        orderMap.put("resCode","SUCCESS");

        HashMap goodsOrder = new HashMap();
        goodsOrder.put("goodsName","测试");

        modelAndView.setViewName("pay");

        modelAndView.addObject("client","alipay");
        modelAndView.addObject("orderMap",orderMap);
        modelAndView.addObject("goodsOrder", goodsOrder);
        modelAndView.addObject("amount","0.01");
        modelAndView.addObject("payUrl", payUrl);
        return modelAndView;
    }
}
