package com.junyi.baseapi.common.exception;

/**
 * 自定义异常(CustomException)
 *
 * @date 2018/10/30 13:59
 */
public class CustomException extends RuntimeException {

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException() {
        super();
    }
}
