package com.hz.pay.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 支付宝交易对账
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class AlipayTradeReconTask {

    private Logger logger = LoggerFactory.getLogger(AlipayTradeReconTask.class);

    @Scheduled(cron = "0 0/1 * * * ?")
    public void startRecon(){
        logger.info("-------------------支付宝交易对账定时任务---------------");
        //0.清空临时表存t_recon_temp

        //1.调用支付宝的对账接口，下载对账文件

        //2.读取对账文件中的内容，csv，存入t_recon_temp 临时表

        //3.对t_pay_order 和存入t_recon_temp 进行匹配

        //4.调用redis执行对账。

        //5.将对账差异存入差役表。




    }


}
