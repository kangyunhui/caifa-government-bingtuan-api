package com.junyi.baseapi.common.util;

import static com.junyi.baseapi.common.constant.Constants.REGSTR;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author lijy
 * @create 2019-01-14 10:12
 * @description 校验工具类
 */
public class ValidateUtils {

    /** 手机号码正则 */
    public static final String REGEX_PHONE =
            "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])"
                    + "|(18[0-9])|(19[8,9]))\\d{8}$";
    /** 邮箱正则 */
    public static final String REGEX_EMAIL =
            "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    // 统一社会信用代码校验正则
    private static final String REGEX_SOCIAL_CREDIT_CODE =
            "[1-9A-GY]{1}[1239]{1}[1-5]{1}[0-9]{5}[0-9A-Z]{10}";

    /**
     * 校验字段是否为空
     *
     * @param name 字段名称
     * @param str 字段值
     */
    public static void validateName(String name, String str) {
        if (StringUtils.isBlank(str)) throw new IllegalArgumentException(name + "不能为空");
    }

    /**
     * 校验手机号码
     *
     * @param phone 手机号码
     */
    public static void validatePhone(String phone) {
        validateName("手机号码", phone);

        if (!phone.matches(REGEX_PHONE)) throw new IllegalArgumentException("手机号码格式不正确");
    }

    /**
     * 校验手机号码
     *
     * @param name 提示字段
     * @param phone 手机号码
     */
    public static void validatePhone(String name, String phone) {
        validateName(name, phone);

        if (!phone.matches(REGEX_PHONE)) throw new IllegalArgumentException(name + "格式不正确");
    }

    /**
     * 校验邮箱
     *
     * @param emailStr
     */
    public static void validateEmail(String emailStr) {
        validateName("邮箱", emailStr);

        if (!emailStr.matches(REGEX_EMAIL)) throw new IllegalArgumentException("邮箱格式不正确");
    }

    public static void validateSocialCreditCode(String socialcreditcode) {
        validateName("统一社会信用代码", socialcreditcode);

        if (!socialcreditcode.matches(REGEX_SOCIAL_CREDIT_CODE))
            throw new IllegalArgumentException("统一社会信用代码格式不正确");
    }

    /**
     * 为空判断
     *
     * @param o
     * @return
     */
    public static Boolean isNull(Object o) {
        return Objects.isNull(o);
    }

    public static Boolean isLessThanOrEqualToDays(Date dateStart, Date dateEnd, int days) {
        return (dateEnd.getTime() - dateStart.getTime() - days * 24 * 60 * 60 * 1000) <= 0;
    }

    public static Date getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static boolean rexCheckPassword(String passWord) {
        // 必须含有数字、字母、特殊符号/8位或者以上
        return passWord.matches(REGSTR);
    }
}
