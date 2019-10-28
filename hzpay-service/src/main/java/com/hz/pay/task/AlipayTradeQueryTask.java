package com.hz.pay.task;

import com.hz.common.constant.PayConstant;
import com.hz.pay.controller.AlipayNotifyController;
import com.hz.pay.model.PayOrder;
import com.hz.pay.service.AlipayService;
import com.hz.pay.service.IPayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 支付宝交易查询定时任务
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class AlipayTradeQueryTask {

    private Logger logger = LoggerFactory.getLogger(AlipayTradeQueryTask.class);

    @Autowired
    private IPayOrderService iPayOrderService;

    @Autowired
    AlipayService alipayService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendTradeQuery(){
        logger.info("-------------------支付宝交易查询定时任务---------------");

        //查询 pay_order表中，状态为1的数据。
       List<PayOrder> payOrderList = iPayOrderService.getPayListByStatus(PayConstant.PAY_CHANNEL_ALIPAY_WAP,"1");
       if (CollectionUtils.isEmpty(payOrderList)){
           logger.info("没有符合条件的数据");
           return;
       }

        //调用支付宝查询接口
       for (int i = 0;i<payOrderList.size();i++){
           PayOrder payOrder = payOrderList.get(i);
           String str = alipayService.getAlipayPayRlt(payOrder.getPayOrderId());
           logger.info("支付宝查询接口返回：{} ",str);
           //解析str，判断支付宝状态更新数据库pay_order状态

       }

    }

}
