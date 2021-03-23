package com.junyi.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 一个标识，用来标记接口是否为开放接口（开放接口指不需要用户登录就可以访问的接口） */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Public {}
