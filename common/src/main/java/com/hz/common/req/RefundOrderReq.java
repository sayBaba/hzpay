package com.hz.common.req;

import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotNull;

/**
 * 退款接口请求参数
 */
@Data
@ToString
public class RefundOrderReq {

    @NotNull(message ="商户id为空")
    private String mchId; //商户id

    @NotNull(message ="渠道id为空")
    private String channelId; //

    private String payOrderId; //支付订单号   支付的时候，支付服务生成的
    @NotNull(message ="商户支付单号为空")
    private String mchOrderNo; //  支付的时候，商户生成的

    @NotNull(message ="商户退款订单号为空")
    private String mchRefundNo; //商户生成的退款订单号

    @NotNull(message ="退款金额为空")
    private String amount; //退款金额

    @NotNull(message ="币种为空")
    private String currency; // 币种

    private String clientIp; //客户端IP
    private String device; //设备
    private String extra; //特定渠道发起时额外参数
    private String param1; //特定渠道发起时额外参数
    private String param2; //特定渠道发起时额外参数
    private String notifyUrl; //回调地址，比如回调shop项目
    private String channelUser; //渠道用户标识,如微信openId,支付宝账号
    private String userName; //用户姓名
    private String remarkInfo; //备注


    private String payAmt; //支付金额



    @NotNull(message ="签名sign为空")
    private String sign; //签名

}
