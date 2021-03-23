package com.junyi.baseapi.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.junyi.baseapi.pojo.vo.SmsRequestBody;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lijy
 * @create 2019-07-31 9:39
 * @description leancloud 工具类
 */
@Slf4j
@Component
public final class LeancloudUtils {

    @Autowired private RestProxy restProxy;

    @Value("${sms.appid}")
    private String smsAppid;

    @Value("${sms.appkey}")
    private String smsAppkey;

    @Value("${sms.request-base-url}")
    private String smsRequestBaseUrl;

    private static LeancloudUtils leancloudUtils;

    @PostConstruct
    public void init() {
        leancloudUtils = this;
        leancloudUtils.restProxy = this.restProxy;
        leancloudUtils.smsAppid = this.smsAppid;
        leancloudUtils.smsAppkey = this.smsAppkey;
        leancloudUtils.smsRequestBaseUrl = this.smsRequestBaseUrl;
    }

    public static ResponseEntity<String> requestSmsCode(SmsRequestBody smsRequestBody) {
        ResponseEntity<String> response = getLeanCloudResponse(smsRequestBody, null);
        return response;
    }

    /**
     * 验证短信验证码
     *
     * @param smsRequestBody 请求体
     * @param code 短信验证码
     * @return
     */
    public static ResponseEntity<String> verifySmsCode(SmsRequestBody smsRequestBody, String code) {
        ResponseEntity<String> response = getLeanCloudResponse(smsRequestBody, code);
        return response;
    }

    public static ResponseEntity<String> eventSmsSend(JSONObject json) {
        setRestHeader();
        String reqUrl = leancloudUtils.smsRequestBaseUrl + "requestSmsCode";
        return leancloudUtils
                .restProxy
                .contentType("application/json")
                .post(reqUrl, json, String.class);
    }

    private static void setRestHeader() {
        leancloudUtils.restProxy.headers.put("X-LC-Id", leancloudUtils.smsAppid);
        leancloudUtils.restProxy.headers.put("X-LC-Key", leancloudUtils.smsAppkey);
    }

    private static ResponseEntity<String> getLeanCloudResponse(
            SmsRequestBody smsRequestBody, String code) {
        setRestHeader();
        String reqUrl;
        if (StringUtils.isEmpty(code)) {
            reqUrl = leancloudUtils.smsRequestBaseUrl + "requestSmsCode";
        } else {
            reqUrl = leancloudUtils.smsRequestBaseUrl + "verifySmsCode/" + code;
        }
        return leancloudUtils
                .restProxy
                .contentType("application/json")
                .post(reqUrl, smsRequestBody, String.class);
    }
}
