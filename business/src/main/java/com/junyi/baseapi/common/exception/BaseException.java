package com.junyi.baseapi.common.exception;

public class BaseException extends Exception {
    private static final long serialVersionUID = 1552376614047169923L;

    private String message;
    private Integer code;

    public BaseException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
