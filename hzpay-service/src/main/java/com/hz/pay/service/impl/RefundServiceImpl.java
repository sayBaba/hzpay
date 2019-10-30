package com.hz.pay.service.impl;

import com.hz.common.req.RefundOrderReq;
import com.hz.pay.mapper.RefundOrderMapper;
import com.hz.pay.model.RefundOrder;
import com.hz.pay.service.IRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

@Service
public class RefundServiceImpl implements IRefundService {

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Override
    public RefundOrder createPayOrder(RefundOrderReq refundOrderReq) {
        RefundOrder refundOrder = new RefundOrder();
        Byte status = 0;
        refundOrder.setStatus(status);
        refundOrder.setChannelId(refundOrderReq.getChannelId());
        refundOrder.setChannelMchId(refundOrderReq.getMchId());
        refundOrder.setPayOrderId(refundOrderReq.getPayOrderId()); //支付订单号
        refundOrder.setMchRefundNo(refundOrderReq.getMchRefundNo()); //商户退款流水
        refundOrder.setChannelOrderNo(refundOrderReq.getMchOrderNo()); //商户支付流水
        refundOrder.setRefundOrderId(String.valueOf(System.currentTimeMillis()));  //支付系统生成的退款流水
        refundOrder.setPayAmount(Long.parseLong(refundOrderReq.getPayAmt())); //单位为分,支付金额
        refundOrder.setRefundAmount(Long.parseLong(refundOrderReq.getAmount())); //退款金额
        refundOrder.setCurrency(refundOrderReq.getCurrency());
        refundOrder.setNotifyUrl(refundOrderReq.getNotifyUrl()); //退款回调地址
        Date date = new Date();
        refundOrder.setCreateTime(date);
        refundOrder.setUpdateTime(date);
        int rl = refundOrderMapper.insertSelective(refundOrder);
        return refundOrder;
    }
}
