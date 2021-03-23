package com.junyi.baseapi.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.pojo.vo.SmsRequestBody;

/**
 * @author lijy
 * @create 2019-08-06 17:07
 * @description 短信应用接口
 */
public interface ISms {

    /** "0" 表示请求操作 */
    String TYPE_REQUESTSMSCODE = "0";
    /** "1" 表示验证操作 */
    String TYPE_VERIFYSMSCODE = "1";

    /** 请求短信验证码 */
    Result requestSmsCode(SmsRequestBody smsRequestBody);
    /** 验证短信验证码 */
    Result verifySmsCode(SmsRequestBody smsRequestBody, String code);
    /** 采伐证状态更新推送 */
    Result eventSmsSend(JSONObject json);
}
