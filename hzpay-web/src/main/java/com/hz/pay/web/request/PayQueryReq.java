package com.hz.pay.web.request;

import lombok.Data;
import lombok.ToString;

/**
 * 支付结果查询请求实体
 */
@Data
@ToString
public class PayQueryReq {

    private String mchOrderId; //商户订单号
    private String channelId; //支付渠道id
    private String mchId; //商户id


}
