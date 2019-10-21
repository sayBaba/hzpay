package com.hz.shop.mapper;

import com.hz.shop.model.GoodsOrder;
import org.apache.ibatis.annotations.Param;

public interface GoodsOrderMapper {

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(String goodsOrderId);

//  int updateByExampleSelective(@Param("record") GoodsOrder record, @Param("example") GoodsOrderExample example);

    int updateByPrimaryKeySelective(GoodsOrder record);

}
