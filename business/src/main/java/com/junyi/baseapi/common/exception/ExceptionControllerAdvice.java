package com.junyi.baseapi.common.exception;

import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.common.exception.bean.ResultCode;
import com.junyi.baseapi.common.exception.bean.ResultGenerator;
import com.junyi.permission.exception.PermissionException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.text.ParseException;
import java.util.NoSuchElementException;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletResponse;

/** @description 全局异常处理类 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(Exception.class)
    public Result jsonErrorHandler(HttpServletResponse resp, Exception e) {
        log.error(e.getMessage(), e);

        if (e instanceof NoHandlerFoundException) {
            resp.setStatus(ResultCode.NOT_FOUND.code());
            return ResultGenerator.genFailResult(ResultCode.NOT_FOUND, e.getMessage());
        }

        if (e instanceof PermissionException) {
            resp.setStatus(ResultCode.FAIL.code());
            return ResultGenerator.genFailResult(ResultCode.FAIL, e.getMessage());
        }

        if (e instanceof IllegalArgumentException
                || e instanceof NoSuchElementException
                || e instanceof CustomException) {
            resp.setStatus(ResultCode.FAIL.code());
            return ResultGenerator.genFailResult(ResultCode.FAIL, e.getMessage());
        }

        if (e instanceof ParseException || e instanceof HttpMessageNotReadableException) {
            resp.setStatus(ResultCode.FAIL.code());
            return ResultGenerator.genFailResult(ResultCode.FAIL, "请检查时间格式或者其他字段格式是否正确");
        }

        if (e instanceof SizeLimitExceededException
                || e instanceof MaxUploadSizeExceededException) {
            resp.setStatus(ResultCode.FAIL.code());
            return ResultGenerator.genFailResult(ResultCode.FAIL, "请上传" + maxFileSize + "以下的文件");
        }

        if (e instanceof DataIntegrityViolationException
                && e.getCause() != null
                && e.getCause().getCause() != null
                && !StringUtils.isEmpty(e.getCause().getCause().getMessage())
                && (e.getCause().getCause().getMessage().contains("错误: 对于可变字符类型来说，值太长了")
                        || e.getCause()
                                .getCause()
                                .getMessage()
                                .contains("value too long for type character"))) {
            resp.setStatus(ResultCode.FAIL.code());
            // 数据库出现字段长度过长时所抛的异常的message
            String msg = e.getCause().getCause().getMessage();
            // 获取报错字段的最大长度并返回到调用方
            return ResultGenerator.genFailResult(
                    ResultCode.FAIL,
                    "已超出字段最长长度限制: " + msg.substring(msg.indexOf("(") + 1, msg.indexOf(")")));
        }

        resp.setStatus(ResultCode.INTERNAL_SERVER_ERROR.code());
        return ResultGenerator.genFailResult(ResultCode.INTERNAL_SERVER_ERROR, "系统错误");
    }
}
