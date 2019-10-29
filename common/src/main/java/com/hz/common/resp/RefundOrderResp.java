package com.hz.common.resp;

import lombok.Data;
import lombok.ToString;

/**
 * 退款接口返回参数
 */
@Data
@ToString
public class RefundOrderResp<T>{

    private String code; //返回状态码
    private String msg; //返回状态码

    private T data; //返回对象信息




}
