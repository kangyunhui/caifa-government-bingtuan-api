package com.junyi.baseapi.common.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.common.sms.http.SmsRes;
import com.junyi.baseapi.common.util.JsonReturnUtil;
import com.junyi.baseapi.pojo.vo.SmsRequestBody;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author lijy
 * @create 2019-08-06 17:11
 * @description leancloud短信服务
 */
@Slf4j
public final class LeancloudImpl implements ISms {

    @Override
    public Result requestSmsCode(SmsRequestBody smsRequestBody) {
        try {
            ResponseEntity<String> response = LeancloudUtils.requestSmsCode(smsRequestBody);
            return this.doResponse(response, TYPE_REQUESTSMSCODE);
        } catch (Exception e) {
            return this.handleException(e, TYPE_REQUESTSMSCODE);
        }
    }

    @Override
    public Result verifySmsCode(SmsRequestBody smsRequestBody, String code) {
        try {
            ResponseEntity<String> response = LeancloudUtils.verifySmsCode(smsRequestBody, code);
            return this.doResponse(response, TYPE_VERIFYSMSCODE);
        } catch (Exception e) {
            return this.handleException(e, TYPE_VERIFYSMSCODE);
        }
    }

    @Override
    public Result eventSmsSend(JSONObject json) {
        try {
            ResponseEntity<String> response = LeancloudUtils.eventSmsSend(json);
            return this.doResponse(response, TYPE_REQUESTSMSCODE);
        } catch (Exception e) {
            return this.handleException(e, TYPE_REQUESTSMSCODE);
        }
    }

    private Result doResponse(ResponseEntity<String> response, String type) {
        if (null != response
                && response.getStatusCode().equals(HttpStatus.OK)
                && response.getBody().equals("{}")) {
            if (TYPE_REQUESTSMSCODE.equals(type)) {
                return JsonReturnUtil.success("获取验证码成功");
            }
            return JsonReturnUtil.success("验证码输入正确！");
        }
        if (TYPE_REQUESTSMSCODE.equals(type)) {
            log.error("获取验证码失败: Response:" + response.getBody());
            return JsonReturnUtil.fail("获取验证码失败");
        }
        log.error("验证码输入错误: Response:" + response.getBody());
        return JsonReturnUtil.fail("验证码输入错误");
    }

    private Result handleException(Exception e, String type) {
        if (e instanceof HttpClientErrorException.BadRequest) {
            String responseBodyAsString =
                    ((HttpClientErrorException.BadRequest) e).getResponseBodyAsString();
            SmsRes smsRes = JSON.parseObject(responseBodyAsString, SmsRes.class);
            if (TYPE_REQUESTSMSCODE.equals(type)) {
                log.error("获取验证码失败：{}", smsRes.getError());
                return JsonReturnUtil.fail(smsRes.getError());
            }
            log.error("验证码输入错误：{}", smsRes.getError());
            return JsonReturnUtil.fail(smsRes.getError());
        }

        if (TYPE_REQUESTSMSCODE.equals(type)) {
            log.error("获取验证码失败: {}", e.getMessage());
            return JsonReturnUtil.fail("获取验证码失败");
        }
        log.error("验证码输入错误：{}", e.getMessage());
        return JsonReturnUtil.fail("验证码输入错误");
    }
}
