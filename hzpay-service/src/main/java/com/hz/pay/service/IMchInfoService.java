package com.hz.pay.service;

import com.hz.pay.model.MchInfo;

/**
 * 查询商户信息
 */
public interface IMchInfoService {

    /**
     * 根据商户id查询商户信息
     * @param mchId
     * @return
     */
    public MchInfo selectMchInfo (String mchId);
}
