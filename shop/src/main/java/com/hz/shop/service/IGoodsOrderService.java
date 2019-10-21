package com.hz.shop.service;

import com.hz.shop.model.GoodsOrder;

/**
 *商品订单相关接口
 */
public interface IGoodsOrderService {

    /**
     * 添加商品订单
     */
    public GoodsOrder addGoodsOrder(String goodsId, Long amount);
}
