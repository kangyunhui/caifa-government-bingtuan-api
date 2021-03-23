package com.junyi.baseapi.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.pojo.vo.SmsRequestBody;

import java.util.ResourceBundle;

/**
 * @author lijy
 * @create 2019-08-06 17:40
 * @description 短信client
 */
public final class SmsClient {

    private static final ISms iSms;

    static {
        /**
         * 不需要在配置文件中配置，则可直接 isms = new LeancloudImpl();每次更换短信服务商，修改此处即可
         * 需要配置文件中配置，则采用下面方式获取短信服务商类型，更改类型在smm-type.properties文件中，这种方式需要多个实现类
         */
        ResourceBundle resourceBundle = ResourceBundle.getBundle("sms-type");
        String type = resourceBundle.getString("type");
        iSms = getSmsType(type);
    }

    public static Result requestSmsCode(SmsRequestBody smsRequestBody) {
        return iSms.requestSmsCode(smsRequestBody);
    }

    public static Result verifySmsCode(SmsRequestBody smsRequestBody, String code) {
        return iSms.verifySmsCode(smsRequestBody, code);
    }

    public static Result eventSmsSend(JSONObject json) {
        return iSms.eventSmsSend(json);
    }

    private static ISms getSmsType(String type) {
        switch (type) {
            case "0":
                return new LeancloudImpl();
            case "1":
                return new AliImpl();
            default:
                return new LeancloudImpl();
        }
    }
}
