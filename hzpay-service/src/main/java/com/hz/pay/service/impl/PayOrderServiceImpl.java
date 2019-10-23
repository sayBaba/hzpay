package com.hz.pay.service.impl;

import com.hz.pay.mapper.PayOrderMapper;
import com.hz.pay.model.PayOrder;
import com.hz.pay.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 */
@Service
public class PayOrderServiceImpl implements IPayOrderService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Override
    public int createPayOrder(PayOrder payOrder) {

        return payOrderMapper.insertSelective(payOrder);
    }
}
