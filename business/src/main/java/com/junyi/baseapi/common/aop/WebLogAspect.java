package com.junyi.baseapi.common.aop;

import com.alibaba.fastjson.JSON;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @Description TODO: @Author Cheng Fan @E-mail @Time 2018/12/13 上午6:50 */
@Aspect
@Component
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.junyi.baseapi.controller.*.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        startTime.set(System.currentTimeMillis());
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return;
        }
        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info(
                "CLASS_METHOD : "
                        + joinPoint.getSignature().getDeclaringTypeName()
                        + "."
                        + joinPoint.getSignature().getName());
        // logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof ServletRequest
                    || arg instanceof ServletResponse
                    || arg instanceof MultipartFile) {
                // ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal
                // to call this method if the current request is not in asynchronous mode (i.e.
                // isAsyncStarted() returns false)
                // ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException:
                // getOutputStream() has already been called for this response
            } else {
                try {
                    logger.info(JSON.toJSONStringWithDateFormat(arg, "yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if ("get".equalsIgnoreCase(attributes.getRequest().getMethod())) {
            return;
        }
        Long start = startTime.get();
        long time = System.currentTimeMillis() - start;

        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
        HttpServletResponse resp = attributes.getResponse();
        logger.info("返回码：" + resp.getStatus());
        logger.info("执行时间：" + time);
    }
}
