package com.hz.pay.controller;

import com.hz.common.req.RefundOrderReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款接口
 */
@RestController
public class RefundOrderController {

    private Logger logger = LoggerFactory.getLogger(RefundOrderController.class);


    /**
     * 创建退款流水
     * @param refundOrderReq
     * @return
     */
    @RequestMapping(value = "/refund/create")
    public String createPayOrder(@RequestBody RefundOrderReq refundOrderReq) {

        return null;
    }



}
