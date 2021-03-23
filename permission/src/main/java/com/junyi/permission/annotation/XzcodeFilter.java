package com.junyi.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author erma66.feng
 * @email erma66@sina.cn
 * @date 2021-02-16
 * @description 通过xzcode过滤数据的标识
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface XzcodeFilter {
    /**
     * 排除的方法名
     *
     * @return
     */
    String[] excludedMethods() default {};
}
