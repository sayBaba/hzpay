package com.hz.pay.utils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.hz.pay.config.AlipayConfig;
import com.hz.pay.model.PayChannel;
import com.hz.pay.model.PayOrder;
import com.hz.pay.service.IPayChannelService;
import com.hz.pay.service.IPayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 验证支付宝异步回调参数
 */
@Component
public class VerifyAliPayRespData {

    private Logger logger = LoggerFactory.getLogger(VerifyAliPayRespData.class);

    @Autowired
    private IPayOrderService iPayOrderService;

    @Autowired
    private IPayChannelService iPayChannelService;

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 验证支付宝支付通知参数
     * @return
     */
    public boolean verifyAliPayParams(Map<String, Object> payContext){

        Map<String,String> params = (Map<String,String>)payContext.get("parameters");
        String out_trade_no = params.get("out_trade_no");		// 商户订单号
        String total_amount = params.get("total_amount");   		// 支付金额

        //1.判断交易订单号，和金额是否存在
        if (StringUtils.isEmpty(out_trade_no)) {
            logger.error("AliPay Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }

        if (StringUtils.isEmpty(total_amount)) {
            logger.error("AliPay Notify parameter total_amount is empty. total_fee={}", total_amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }

        //2.判断交易订单号是否存在
        PayOrder payOrder = iPayOrderService.selectPayOrder(out_trade_no);
        if (payOrder == null) {
            logger.error("Can't found payOrder form db. payOrderId={}, ", out_trade_no);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        //3.判断商户和支付方式是否存在
        String mchId = payOrder.getMchId();
        String channelId = payOrder.getChannelId();
        PayChannel payChannel = iPayChannelService.selectPayChannel(channelId, mchId);
        if(payChannel == null) {
            logger.error("Can't found payChannel form db. mchId={} channelId={}, ", out_trade_no, mchId, channelId);
            payContext.put("retMsg", "Can't found payChannel");
            return false;
        }

        //4.判断数据真实性，判断sign值
        boolean flag = false;
        try {
            flag = AlipaySignature.rsaCheckV1(params, alipayConfig.getPublicKey(), "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            logger.error("AlipaySignature.rsaCheckV1 error",e);
        }
        String errorMessage = null;
        //TODO
//        if(!flag){
//            errorMessage = "rsaCheckV1 failed.";
//            logger.error("AliPay Notify parameter {}", errorMessage);
//            payContext.put("retMsg", errorMessage);
//            return false;
//        }

        //5..判断数据库的金额和支付宝的金额是否一致
        long aliPayAmt = new BigDecimal(total_amount).movePointRight(2).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            logger.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, out_trade_no);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

    public static void main(String[] args) {
        String xx = "{gmt_create=2019-10-24 11:54:46, charset=utf-8, seller_email=xydskv8413@sandbox.com, notify_time=2019-10-24 11:54:47, subject=XXPAY捐助商品G_0001, sign=jg7eHjzPtmwRpPHKc/rzwYBhnYNAYywH86O2w2HHFF5Os9D62aUtxhflCYQ8eZN9oHSDHkx5fl6kSU6Q0hyiqYB6MiC6CyGqw4ehJz4LwO2AGHDZAmZ2TfIH0fLbJrMeDSCNucvQcn32xZByN1NRVW2e1iRTD8EVfA4D48Bc0ruNE9HI1esmOLvKsQzQct5ChyScksdED1uIBE+DkYCG22zXSXtifRyKeL1u77MrqKUlFY7TM+Jg2BKTtwWqDM1FZK8gJ9qDvfteHBOcDO6MrDY90ENlJDGhozmZ9TnGezvRyLXhdO46M20TQihPmx4kvFc5bstLwDpywFzLatAF/Q==, body=XXPAY捐助商品G_0001, buyer_id=2088102179845676, version=1.0, notify_id=2019102400222115447045671000589496, notify_type=trade_status_sync, out_trade_no=P0020191024115439000032, total_amount=0.01, trade_status=WAIT_BUYER_PAY, trade_no=2019102422001445671000110486, auth_app_id=2016101200667654, buyer_logon_id=sys***@sandbox.com, app_id=2016101200667654, sign_type=RSA2, seller_id=2088102179213370}\n";
    }
}
