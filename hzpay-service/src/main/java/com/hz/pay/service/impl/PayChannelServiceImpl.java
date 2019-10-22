package com.hz.pay.service.impl;

import com.hz.pay.mapper.PayChannelMapper;
import com.hz.pay.model.PayChannel;
import com.hz.pay.model.PayChannelExample;
import com.hz.pay.service.IPayChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 支付渠道接口实现类
 */
@Service
public class PayChannelServiceImpl implements IPayChannelService {

    @Autowired
    private PayChannelMapper payChannelMapper;

    @Override
    public PayChannel selectPayChannel(String channelId, String mchId) {
        PayChannelExample example = new PayChannelExample();
        PayChannelExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        criteria.andMchIdEqualTo(mchId);
        List<PayChannel> payChannelList = payChannelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(payChannelList)) return null;
        return payChannelList.get(0);
    }
}
