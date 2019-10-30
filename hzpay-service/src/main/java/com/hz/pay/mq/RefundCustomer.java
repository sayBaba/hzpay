package com.hz.pay.mq;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hz.pay.model.RefundOrder;
import com.hz.pay.service.AlipayService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 退款消费者
 */
@Component
public class RefundCustomer {


    @Autowired
    private AlipayService alipayService;

    @RabbitListener(queues=MQConfig.NOTIFY_MCH_QUEUE)
    public void receive(Message message, RefundOrder refundOrder, Channel channel){
        String outTradeNo = refundOrder.getPayOrderId();
        String refundNo = refundOrder.getRefundOrderId() ;
        String amt = String.valueOf(refundOrder.getRefundAmount());

        //调用支付宝退款接口
        AlipayTradeRefundResponse refundResponse = alipayService.getAlipayRefund(outTradeNo,refundNo,amt);
        //解析支付宝的返回结果
        if(refundResponse.getSubCode().equals("TRADE_SUCCESS")){
            //更新交易表为成功
        }else{

        }

        //异步通知商户


    }
}
