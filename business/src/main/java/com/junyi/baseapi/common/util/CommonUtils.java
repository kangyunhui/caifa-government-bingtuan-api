package com.junyi.baseapi.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @author lijy
 * @create 2018-12-20 13:26
 * @description 工具类
 */
public class CommonUtils {

    /**
     * 获取uuid
     *
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 产生4位随机数(0000-9999)
     *
     * @return 4位随机数
     */
    public static String getFourRandom() {
        return StringUtils.leftPad(new Random().nextInt(10000) + "", 4, "0");
    }

    public static String getType(String dQlBaseCode) {

        if (null == dQlBaseCode) {
            return "3";
        }

        switch (dQlBaseCode) {
            case "许可-00345-001-01":
                return "1";
            case "许可-00345-001-02":
                return "2";
            default:
                return "1";
        }
    }
}
