package com.hz.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    /**
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
    @RequestMapping("/recPay")
    public String recPay(ModelMap modelMap, Long amount, HttpServletRequest request){
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
            client = "alipay";
            channelId = "ALIPAY_WAP";

        }else { //TODO

        }

        if(client == null) {
            String errorMessage = "请用微信或支付宝扫码";
            logger.info("{}信息：{}", errorMessage);
            modelMap.put("result", "failed");
            modelMap.put("resMsg", errorMessage);
            return view;
        }

        //3.创建订单
        String goodsId = "G_0001";
        GoodsOrder goodsOrder = goodsOrderService.addGoodsOrder(goodsId,amount);

        //4.封装统一下单接口所需的参数

        Map params = new HashMap<>();
        params.put("channelId", channelId); //TODO
        createPayOrder(goodsOrder,params);

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
        paramMap.put("clientIp", "18998949646");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", goodsOrder.getGoodsName());
        paramMap.put("body", goodsOrder.getGoodsName());
        paramMap.put("notifyUrl", MchIdConstant.NOTIFY_URL);         // 回调URL
        paramMap.put("param1", "");                         // 扩展参数1
        paramMap.put("param2", "");                         // 扩展参数2

        String reqSign = PayDigestUtil.getSign(paramMap, MchIdConstant.REQ_KEY);
        logger.info("订单号：{},生成的密文：{}",goodsOrder.getGoodsOrderId(),reqSign);
        paramMap.put("sign", reqSign);// 签名

        String reqData = "params=" + paramMap.toJSONString();
        logger.info("请求支付中心下单接口,请求数据:" + reqData);

        //调用统一下单接口  改springCloud调用
        String result = XXPayUtil.call4Post(MchIdConstant.baseUrl+"/pay/create_order?" + reqData); //TODO
        logger.info("请求支付中心下单接口,响应数据:{}",result);
        if(StringUtils.isEmpty(result)){
            logger.info("请求支付中心下单接口,响应数据为null");
            return null;
        }

        Map retMap = JSON.parseObject(result);
        if("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
//            String checkSign = PayDigestUtil.getSign(retMap, resKey, "sign", "payParams");
//            String retSign = (String) retMap.get("sign");
//            if(checkSign.equals(retSign)) {
//                System.out.println("=========支付中心下单验签成功=========");
//            }else {
//                System.err.println("=========支付中心下单验签失败=========");
//                return null;
//            }
        }



        return null;
    }


}
