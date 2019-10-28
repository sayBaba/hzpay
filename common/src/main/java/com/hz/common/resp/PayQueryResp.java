package com.hz.common.resp;

import lombok.Data;
import lombok.ToString;

/**
 * 支付结果查询返回实体
 */
@Data
@ToString
public class PayQueryResp<T> {

    private String code; //响应吗 0000-成功，

    private String msg; //

    private T data;
}
