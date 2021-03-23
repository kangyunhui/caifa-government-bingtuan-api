package com.junyi.permission.aop;

import com.junyi.permission.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Aspect
@Component
@Slf4j
public class PermissionSortAspect {

    @Pointcut(
            value =
                    "execution(public com.baomidou.mybatisplus.core.metadata.IPage<*>"
                            + " com.junyi.permission.service..*.*(..))")
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
                String sort =
                        CommonUtils.convertSort(
                                (String) args[index], (Class) actualTypeArguments[0]);
                args[index] = sort;
            }
            return proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            log.error("方法执行异常", throwable);
        }
        return null;
    }
}
