package com.junyi.baseapi.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestBody {
    private String mobilePhoneNumber;
    private String code;
}
