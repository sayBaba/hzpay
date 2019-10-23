package com.hz.pay.service.impl;

import com.hz.common.constant.PayConstant;
import com.hz.pay.mapper.PayOrderMapper;
import com.hz.pay.model.PayOrder;
import com.hz.pay.model.PayOrderExample;
import com.hz.pay.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付订单接口实现类
 */
@Service
public class PayOrderServiceImpl implements IPayOrderService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Override
    public int createPayOrder(PayOrder payOrder) {

        return payOrderMapper.insertSelective(payOrder);
    }


    @Override
    public int updatePayOrder(PayOrder payOrder1) {
        PayOrder payOrder = new PayOrder();
        payOrder.setStatus(PayConstant.PAY_STATUS_PAYING); //更新为支付中
        if(payOrder1.getChannelOrderNo() != null) {
            payOrder.setChannelOrderNo(payOrder1.getChannelOrderNo());
        }
        payOrder.setPaySuccTime(System.currentTimeMillis());
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrder1.getPayOrderId());
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_INIT);
        return payOrderMapper.updateByExampleSelective(payOrder, example);
    }
}
