package com.junyi.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 一个标识，用来标记接口是否为免鉴权接口（免鉴权接口指对所有登录用户开放的接口） */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoAuthorized {}
