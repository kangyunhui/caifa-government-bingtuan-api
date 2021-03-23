package com.junyi.permission.util;

import com.baomidou.mybatisplus.annotation.TableField;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.UUID;

public class CommonUtils {
    /**
     * 获取uuid
     *
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String convertSort(String sort, Class clazz) {
        Field declaredField;
        try {
            declaredField = clazz.getDeclaredField(sort);
        } catch (NoSuchFieldException e) {
            return sort;
        }

        if (declaredField == null) {
            return sort;
        }

        TableField tableField = declaredField.getAnnotation(TableField.class);
        if (tableField == null) {
            return sort;
        }

        String value = tableField.value();
        return !StringUtils.isEmpty(value) ? value : sort;
    }
}
