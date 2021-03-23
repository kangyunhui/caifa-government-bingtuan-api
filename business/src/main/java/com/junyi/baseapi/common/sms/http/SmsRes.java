package com.junyi.baseapi.common.sms.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lijy
 * @create 2019-07-25 14:14
 * @description leancloud 错误返回body
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRes {
    private String code;
    private String error;
}
