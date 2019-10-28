package com.hz.pay.mq;
import com.alibaba.fastjson.JSONObject;
import com.hz.common.util.PayDigestUtil;
import com.hz.common.util.XXPayUtil;
import com.hz.pay.mapper.MchNotifyMapper;
import com.hz.pay.model.MchInfo;
import com.hz.pay.model.MchNotify;
import com.hz.pay.model.PayOrder;
import com.hz.pay.service.IMchInfoService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


/**
 * mq消费者
 */
@Component
public class NotifyMchCustomer {

    private static final Logger logger = LoggerFactory.getLogger(NotifyMchCustomer.class);

    //通知业务系统的url
    private static final String mechNotifyUrl = "http://hzpay666.free.idcfengye.com/revive/notice?";

    @Autowired
    private IMchInfoService iMchInfoService;

    @Autowired
    private MchNotifyMapper mchNotifyMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RabbitListener(queues=MQConfig.NOTIFY_MCH_QUEUE)
    public void receive(Message message,NotifyMchMessage msg,Channel channel){
        logger.info("消息队列id:{}消费者开始....",msg.getPayId());

        PayOrder payOrder = msg.getPayOrder();
        if(ObjectUtils.isEmpty(payOrder)){
            logger.info("消息队列:{}队列中payOrder为空....",msg.getPayId());
            return;
        }
        //判断是否内部系统，在发送http通知。
        MchInfo mchInfo = iMchInfoService.selectMchInfo(payOrder.getMchId());
        if(ObjectUtils.isEmpty(mchInfo)){
            logger.info("消息队列id:{}，没有查找到对应的商户:{}信息",msg.getPayId(),payOrder.getMchId());
            return;
        }

        //封装返回的参数
        JSONObject paramMap = new JSONObject();
        paramMap.put("goodsOrderId",payOrder.getMchOrderNo());
        paramMap.put("status",payOrder.getStatus());
        paramMap.put("amount",payOrder.getAmount());
        paramMap.put("mchId",payOrder.getMchId());
        paramMap.put("channelId",payOrder.getChannelId());

        String respSign = PayDigestUtil.getSign(paramMap, mchInfo.getResKey());
        logger.info("订单号：{},异步通知商户生成的加密密文：{}",payOrder.getMchOrderNo(),respSign);
        paramMap.put("sign", respSign);// 签名

        String respData = "params=" + paramMap.toJSONString();
        //发送通知
        String result = XXPayUtil.call4Post(mechNotifyUrl +respData);
        logger.info("订单号：{},异步通知返回的结果：{}",payOrder.getMchOrderNo(),result);
        Byte b = '1';
        int rl = mchNotifyMapper.insertSelective( this.createMchNotify(b,payOrder));

        // 通知5次--5次没有收到结果,那就通知失败：3 否则就是通知中：1, 成功就是：2
        MchNotify mchNotify = mchNotifyMapper.selectByPrimaryKey(payOrder.getPayOrderId());

       if ("success".equals(result)){
            //更新status 为 2,
            Date date = new Date();
            mchNotify.setLastNotifyTime(date);
            mchNotify.setUpdateTime(date);
            String dbRlt = result+":"+respData;
            mchNotify.setResult(dbRlt);
            Integer count = mchNotify.getNotifyCount().intValue()+1;
            mchNotify.setNotifyCount(count.byteValue());
            Byte status = 2;
            mchNotify.setStatus(status);
            mchNotifyMapper.updateByPrimaryKeySelective(mchNotify);

            //手动确认
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e) {
                logger.error("IOException",e);
            }
        }else{
            //解决重复发送。每隔15分钟发一次。
            int count = mchNotify.getNotifyCount().intValue();
            if(count<5){
                // 通过广播模式发布延时消息 延时30分钟 持久化消息 消费后销毁 这里无需指定路由，会广播至每个绑定此交换机的队列
                rabbitTemplate.convertAndSend(MQConfig.Delay_Exchange_Name, "", payOrder, message1 -> {
                    message1.getMessageProperties().setExpiration("60000");
                    return message1;
                });

            }else {
                Date date = new Date();
                mchNotify.setLastNotifyTime(date);
                mchNotify.setUpdateTime(date);
                Byte status = 3;
                mchNotify.setStatus(status);
                String dbRlt = result+"已通知5次:"+respData;
                mchNotify.setResult(dbRlt);
                mchNotifyMapper.updateByPrimaryKeySelective(mchNotify);
            }
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e) {
                logger.error("IOException",e);
            }
        }

    }

    /**
     * 封装通知商户对象
     * @param status
     * @param payOrder
     * @return
     */
    private MchNotify createMchNotify(Byte status,PayOrder payOrder){
        MchNotify mchNotify1 = new MchNotify();
//      LocalDate localDate = LocalDate.now();
        Date date = new Date();
        mchNotify1.setCreateTime(date);
        mchNotify1.setLastNotifyTime(date);
        mchNotify1.setMchId(payOrder.getMchId());
        mchNotify1.setMchOrderNo(payOrder.getMchOrderNo());
        mchNotify1.setOrderId(payOrder.getPayOrderId());
        mchNotify1.setNotifyUrl(mechNotifyUrl);
        Byte b = 0;
        mchNotify1.setNotifyCount(b);
        mchNotify1.setStatus(status);
        mchNotify1.setOrderType("1");
        return mchNotify1;

    }


    public static void main(String[] args) {
        Byte b = 1;
        System.out.println(b+1);
    }
}
