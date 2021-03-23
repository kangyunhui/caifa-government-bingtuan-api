package com.junyi.permission.exception;

import com.junyi.permission.model.ResultCode;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/** 系统异常类 */
@Slf4j
@Getter
public class PermissionException extends RuntimeException {
    private ResultCode code;

    public PermissionException(String message) {
        super(message);
        this.code = ResultCode.FAIL;
        log.error(message);
    }

    public PermissionException(String message, Exception e) {
        super(message);
        this.code = ResultCode.FAIL;
        log.error(message + " " + e);
    }

    public PermissionException(String message, ResultCode code) {
        super(message);
        this.code = code;
        log.error(message);
    }

    public PermissionException(String message, Exception e, ResultCode code) {
        super(message);
        this.code = code;
        log.error(message + " " + e);
    }
}
