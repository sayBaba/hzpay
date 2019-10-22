package com.hz.pay.service.impl;

import com.hz.pay.mapper.MchInfoMapper;
import com.hz.pay.model.MchInfo;
import com.hz.pay.service.IMchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 查询商户相关信息
 */
@Service
public class MchInfoServiceImpl implements IMchInfoService {

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Override
    public MchInfo selectMchInfo(String mchId) {
        return mchInfoMapper.selectByPrimaryKey(mchId);
    }
}
