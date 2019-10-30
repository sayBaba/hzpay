package com.hz.pay.controller;

import com.hz.common.req.RefundOrderReq;
import com.hz.pay.model.RefundOrder;
import com.hz.pay.service.IRefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private IRefundService iRefundService;


    /**
     * 创建退款流水
     * @param refundOrderReq
     * @return
     */
    @RequestMapping(value = "/refund/create")
    public String createPayOrder(@RequestBody RefundOrderReq refundOrderReq) {
        RefundOrder refundOrder = iRefundService.createPayOrder(refundOrderReq);
        //退款请求放入mq
        amqpTemplate.convertAndSend(refundOrder);
        return null;
    }



}
