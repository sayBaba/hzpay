package com.hz.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.common.util.AmountUtil;
import com.hz.common.util.PayDigestUtil;
import com.hz.common.util.XXPayUtil;
import com.hz.shop.constant.MchIdConstant;
import com.hz.shop.model.GoodsOrder;
import com.hz.shop.service.IGoodsOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟商城
 */
@Controller
@RequestMapping("/shop")
public class ShopController {

    private Logger logger = LoggerFactory.getLogger(ShopController.class);

//    @Resource
    @Autowired
    private IGoodsOrderService goodsOrderService;

    /** 程序入口
     * 加载扫码页面
     * @return
     */
    @RequestMapping("/showCode")
    public String showCode(){
        logger.info("....加载二维码支付页面....");
        return "openQrPay";
    }

    /**
     * 扫码支付入口
     * @param modelMap
     * @param amount
     * @param request
     * @return
     */
    @RequestMapping("/recPay1")
    public String recPay1(ModelMap modelMap, Long amount, HttpServletRequest request){
        logger.info("接受到扫描支付请求，金额为:{}",amount);
        String view = "qrPay";

        //1. 获取 ua
        String ua = request.getHeader("User-Agent");
        logger.info("ua = {}",ua);

        //2.判断ua为空
        if(StringUtils.isEmpty(ua)) {
            String errorMessage = "User-Agent为空！";
            logger.info("{}信息：{}", logger, errorMessage);
            modelMap.put("result", "failed");
            modelMap.put("resMsg", errorMessage);
            return view;
        }

        //支付宝wap支付
        String client = "alipay";
        String channelId = "ALIPAY_WAP";

        //3.判断支付方式
        if(ua.contains("Alipay")) {
            client = "alipay";       //支付方式
            channelId = "ALIPAY_WAP"; //支付宝wap支付

        }else { //TODO

        }

        if(client == null) {
            String errorMessage = "请用微信或支付宝扫码";
            logger.info("{}信息：{}", errorMessage);
            modelMap.put("result", "failed");
            modelMap.put("resMsg", errorMessage);
            return view;
        }

        //3.模拟生成-订单
        String goodsId = "G_0001";
        GoodsOrder goodsOrder = goodsOrderService.addGoodsOrder(goodsId,amount);

        //4.调用统一下单接口。
        Map params = new HashMap<>();
        params.put("channelId", channelId);
        Map map = createPayOrder(goodsOrder,params);
        logger.info("---------------------------------");
        logger.info("----"+map.get("payUrl"));
        logger.info("---------------------------------");
        modelMap.addAttribute("client",client);
        modelMap.addAttribute("orderMap",map);
        modelMap.put("goodsOrder", goodsOrder);
        modelMap.put("amount", AmountUtil.convertCent2Dollar(goodsOrder.getAmount()+""));
        return view;
    }

    /**
     * 封装支付接口参数
     */
    private Map createPayOrder(GoodsOrder goodsOrder, Map<String, Object> params) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", MchIdConstant.MCHID);           // 商户ID
        paramMap.put("mchOrderNo", goodsOrder.getGoodsOrderId());  // 商户订单号
        paramMap.put("channelId", params.get("channelId"));             // 支付渠道ID, WX_NATIVE,ALIPAY_WAP
        paramMap.put("amount", goodsOrder.getAmount());                          // 支付金额,单位分
        paramMap.put("currency", "cny");                    // 币种, cny-人民币
        paramMap.put("notifyUrl", MchIdConstant.NOTIFY_URL);         // 回调URL

        paramMap.put("clientIp", "18998949646");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", goodsOrder.getGoodsName());
        paramMap.put("body", goodsOrder.getGoodsName());
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2

        String reqSign = PayDigestUtil.getSign(paramMap, MchIdConstant.REQ_KEY);
        logger.info("订单号：{},生成的密文：{}",goodsOrder.getGoodsOrderId(),reqSign);
        paramMap.put("sign", reqSign);// 签名

        String reqData = "params=" + paramMap.toJSONString();
        logger.info("请求支付中心下单接口,请求数据:" + reqData);

        //调用统一下单接口(zuul网关路由)
        String result = XXPayUtil.call4Post(MchIdConstant.baseUrl+"/pay/create_order?" + reqData); //TODO
        logger.info("请求支付中心下单接口,响应数据:{}",result);
        if(StringUtils.isEmpty(result)){
            logger.info("请求支付中心下单接口,响应数据为null");
            return null;
        }
        Map retMap = JSON.parseObject(result);

        if("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
//            String checkSign = PayDigestUtil.getSign(retMap, MchIdConstant.RES_KEY, "sign", "payParams");
//            String retSign = (String) retMap.get("sign");
//            if(checkSign.equals(retSign)) {
//                System.out.println("=========支付中心下单验签成功=========");
//            }else {
//                System.err.println("=========支付中心下单验签失败=========");
//                return null;
//            }
        }
        return retMap;
    }

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

        modelAndView.setViewName("qrPay");

        modelAndView.addObject("client","alipay");
        modelAndView.addObject("orderMap",orderMap);
        modelAndView.addObject("goodsOrder", goodsOrder);
        modelAndView.addObject("amount","0.01");
        modelAndView.addObject("payUrl", payUrl);
        return modelAndView;
    }

    public static void main(String[] args) {
        String a ="a" +"b";
        String b = new String(a);
        System.out.println(a==b);
    }
}
