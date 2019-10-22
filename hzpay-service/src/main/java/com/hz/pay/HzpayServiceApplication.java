package com.hz.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class HzpayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzpayServiceApplication.class, args);
    }

}
