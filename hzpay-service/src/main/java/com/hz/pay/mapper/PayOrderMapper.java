package com.hz.pay.mapper;

import com.hz.pay.model.PayOrder;
import com.hz.pay.model.PayOrderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayOrderMapper {

    int countByExample(PayOrderExample example);

    int deleteByExample(PayOrderExample example);

    int deleteByPrimaryKey(String payOrderId);

    int insert(PayOrder record);

    int insertSelective(PayOrder record);

    List<PayOrder> selectByExample(PayOrderExample example);

    PayOrder selectByPrimaryKey(String payOrderId);

    int updateByExampleSelective(@Param("record") PayOrder record, @Param("example") PayOrderExample example);

    int updateByExample(@Param("record") PayOrder record, @Param("example") PayOrderExample example);

    int updateByPrimaryKeySelective(PayOrder record);

    int updateByPrimaryKey(PayOrder record);

    /**
     * 根据商户id和商户订单号查询
     * @param mchId
     * @param machOrderId
     * @return
     */
    PayOrder selectByMchOrderId(@Param("mchId") String mchId,@Param("mchOrderId") String machOrderId);

    /**
     *
     * @param channelId
     * @param status
     * @return
     */
   List<PayOrder> selectByChanelIdAndStatus(@Param("channelId") String channelId,@Param("status") String status);

}