package com.hz.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.common.util.MyBase64;
import com.hz.pay.model.MchInfo;
import com.hz.pay.service.IMchInfoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户相关信息接口
 */
@RestController
public class MchInfoServiceController {

    private Logger logger = LoggerFactory.getLogger(MchInfoServiceController.class);

    @Autowired
    private IMchInfoService mchInfoService;

    @RequestMapping(value = "/mch_info/select")
    public String selectMchInfo(@RequestParam String jsonParam) {
        logger.info("查询商户信息请求参数：{}....",jsonParam);
        JSONObject retObj = new JSONObject();
        String param = new String(MyBase64.decode(jsonParam));
        if(StringUtils.isBlank(jsonParam)) {
            retObj.put("code", "0001"); // 参数错误
            retObj.put("msg", "缺少参数");
            return retObj.toJSONString();
        }

        JSONObject paramObj = JSON.parseObject(param);
        String mchId = paramObj.getString("mchId");
        MchInfo mchInfo = mchInfoService.selectMchInfo(mchId);
        if(mchInfo == null) {
            retObj.put("code", "0002");
            retObj.put("msg", "数据对象不存在");
            return retObj.toJSONString();
        }
        retObj.put("code", "0000");
        retObj.put("result", JSON.toJSON(mchInfo));
        logger.info("查询商户信息返回result:{}", retObj.toJSONString());
        return retObj.toJSONString();
    }



}
