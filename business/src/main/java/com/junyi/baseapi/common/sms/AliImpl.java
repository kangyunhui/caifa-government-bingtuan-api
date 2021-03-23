package com.junyi.baseapi.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.pojo.vo.SmsRequestBody;

/**
 * @author lijy
 * @create 2019-08-06 17:25
 * @description 阿里短信服务(暂未实现)
 */
public final class AliImpl implements ISms {
    @Override
    public Result requestSmsCode(SmsRequestBody smsRequestBody) {
        return null;
    }

    @Override
    public Result verifySmsCode(SmsRequestBody smsRequestBody, String code) {
        return null;
    }

    @Override
    public Result eventSmsSend(JSONObject json) {
        return null;
    }
}
