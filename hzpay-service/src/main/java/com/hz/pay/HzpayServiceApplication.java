package com.hz.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@MapperScan("com.hz.pay.mapper")
@EnableEurekaClient
@SpringBootApplication
public class HzpayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzpayServiceApplication.class, args);
    }

}
