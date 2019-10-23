package com.hz.pay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付宝异步通知入口
 */
@RestController
public class AlipayNotifyController {

    private Logger logger = LoggerFactory.getLogger(AlipayNotifyController.class);

    /**
     * 支付宝移动支付后台通知响应
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/pay/aliPayNotifyRes.htm")
    public void aliPayNotifyRes(HttpServletRequest request, HttpServletResponse response){
//        String result =  doAliPayRes(request, response);


    }





}
