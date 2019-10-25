package com.hz.pay.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

    //异步通知队列
	public static final String NOTIFY_MCH_QUEUE = "NOTIFY_MCH_QUEUE";
	public static final String QUEUE = "queue";

    // 通知延时交换机
    public static final String Delay_Exchange_Name = "NOTIFY_DELAY_EXCHANGE";

    // 通知延时队列
    public static final String Timeout_Trade_Queue_Name = "NOTIFY_DELAY_QUEUE";


    @Bean
    public Queue delayPayQueue() {
        return new Queue(MQConfig.Timeout_Trade_Queue_Name, true);
    }

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(Delay_Exchange_Name, "x-delayed-message", true, false, args);
    }

    /**
     * 把立即消费的队列和延时消费的exchange绑定在一起
     *
     * @return
     */
    @Bean
    public Binding bindingNotify() {
        return BindingBuilder.bind(delayPayQueue()).to(delayExchange()).with("").noargs();
    }


//    // 定义广播模式的延时交换机 无需绑定路由
//    @Bean
//    FanoutExchange delayExchange(){
//        Map<String, Object> args = new HashMap<String, Object>();
//        args.put("x-delayed-type", "direct");
//        FanoutExchange topicExchange = new FanoutExchange(MQConfig.Delay_Exchange_Name, true, false, args);
//        topicExchange.setDelayed(true);
//        return topicExchange;
//    }



    // 定义消息模板用于发布消息，并且设置其消息转换器
/*    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }*/
//
//    @Bean
//    RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }

/*
    @Bean
	public MessageConverter getMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
*/

	@Bean
	public Queue queue() {
		return new Queue(QUEUE, true);
	}

}
