package com.hz.pay.mq;

import com.hz.pay.model.PayOrder;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 延时队列消费
 */
@Component
public class NotifyMchDelayedCustomer {

    private static final Logger logger = LoggerFactory.getLogger(NotifyMchDelayedCustomer.class);

    @RabbitListener(queues=MQConfig.Timeout_Trade_Queue_Name)
    public void receive(Message message, PayOrder payOrder, Channel channel){
        logger.info("---------延时队列消费正在消费-----------:{}",payOrder.getMchOrderNo());
        //C

    }

}
