package com.hz.pay;

import com.hz.pay.config.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.Set;

@SpringBootTest
class HzpayServiceApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
         //流水号            金额  状态
        //GM002215120800002,0.01,3",  "GM002215120800003,0.01,3

        //将本地t_PAy_order中的数据放入redis
        redisUtil.sadd("{account}:localSet", "GM002215120800002,0.01,3","GM002215120800003,0.01,3");

        //流水号            金额  状态
        //GM002215120800002,0.01,3",  "GM002215120800003,0.01,3
        //将支付宝的交易数据从t_recon_temp中的数据放入redis
        redisUtil.sadd("{account}:outerSet", "GM002215120800002,0.01,3","GM002215120800003,0.01,1");


        //进行2个集合的比对，得出交集union，将交集放入key”{account}:union”中
        redisUtil.sinterstore("{account}:union", "{account}:localSet", "{account}:outerSet");
        //union = GM002215120800002,0.01,3

        //localSet和outerSet分别与交集进行比较，得出差集{account}:localDif
        redisUtil.sdiffstore("{account}:localDiff", "{account}:localSet", "{account}:union");
        //localDiff = GM002215120800003,0.01,3

        redisUtil.sdiffstore("{account}:outerDiff", "{account}:outerSet", "{account}:union");
        //outerDiff = GM002215120800003,0.01,1


        Set<String> set = redisUtil.smembers("{account}:localDiff");
        if(set!=null){
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String str = it.next();
                System.out.println("localDiff:"+str);
            }
        }

        Set<String> set1 = redisUtil.smembers("{account}:outerDiff");

        if(set1!=null){
            Iterator<String> it = set1.iterator();
            while (it.hasNext()) {
                String str = it.next();
                System.out.println("outerDiff:"+str);
            }
        }

    }

}
