package com.junyi.baseapi.common.aop;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/** 用于处理分页情况下排序字段的切面 由于前端返回的是模型字段名而不是数据库字段名，需要将模型字段名转换成数据库字段名以正确使用排序功能 */
@Aspect
@Component
@Slf4j
public class SortAspect {

    @Pointcut(
            value =
                    "execution(public com.baomidou.mybatisplus.core.metadata.IPage<*>"
                            + " com.junyi.baseapi.service..*.*(..))")
    public void pageService() {}

    @Around("pageService()")
    public Object convertSort(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String[] parameters = signature.getParameterNames();
        int index = -1;
        for (int i = 0; i < parameters.length; i++) {
            if ("sort".equals(parameters[i])) {
                index = i;
                break;
            }
        }
        try {
            if (index == -1) {
                return proceedingJoinPoint.proceed();
            }
            Type genericReturnType = signature.getMethod().getGenericReturnType();
            Object[] args = proceedingJoinPoint.getArgs();
            if (genericReturnType instanceof ParameterizedType) {
                Type[] actualTypeArguments =
                        ((ParameterizedType) genericReturnType).getActualTypeArguments();
                String sort = convertSort((String) args[index], (Class) actualTypeArguments[0]);
                args[index] = sort;
            }
            return proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            log.error("方法执行异常", throwable);
        }
        return null;
    }

    private static String convertSort(String sort, Class clazz) {
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
