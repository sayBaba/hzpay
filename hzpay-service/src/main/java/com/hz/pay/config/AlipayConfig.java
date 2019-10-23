package com.hz.pay.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 读取支付宝接口参数
 */
@Data
@Configuration
public class AlipayConfig {

    @Value("${alipay.url}")
    private String alipayUrl;

    @Value("${alipay.appId}")
    private String appId;

    @Value("${alipay.privateKey}")
    private String privateKey;

    @Value("${alipay.publicKey}")
    private String publicKey;
}
