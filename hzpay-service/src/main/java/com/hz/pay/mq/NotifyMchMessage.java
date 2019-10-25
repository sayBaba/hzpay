package com.hz.pay.mq;

import com.hz.pay.model.PayOrder;
import lombok.Data;

import java.io.Serializable;

/**
 * MQ消息
 */
@Data
public class NotifyMchMessage implements Serializable {

    private String payId; //订单号，做为mq消息的唯一标识

    private PayOrder payOrder; //消息内容
}
