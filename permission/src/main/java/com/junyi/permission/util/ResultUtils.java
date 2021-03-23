package com.junyi.permission.util;

import com.junyi.permission.model.Result;
import com.junyi.permission.model.ResultCode;

/** 构造返回的工具类 */
public class ResultUtils {

    private static final String DEFAULT_SUCCESS_MESSAGE = "success";

    public static Result success() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setSuccess(true)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result success(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setSuccess(true)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result fail(String message) {
        return new Result().setCode(ResultCode.FAIL).setSuccess(false).setMessage(message);
    }

    public static Result fail(ResultCode code, String message) {
        return new Result().setCode(code).setSuccess(false).setMessage(message);
    }

    public static Result getResult(ResultCode code, String message, Object data) {
        return new Result()
                .setCode(code)
                .setSuccess(ResultCode.SUCCESS.equals(code))
                .setMessage(message)
                .setData(data);
    }

    public static Result getResult(ResultCode code, String message) {
        return new Result()
                .setCode(code)
                .setSuccess(ResultCode.SUCCESS.equals(code))
                .setMessage(message);
    }

    public static Result error(String message) {
        return new Result()
                .setCode(ResultCode.INTERNAL_SERVER_ERROR)
                .setSuccess(false)
                .setMessage(message);
    }

    public static Result getResult(ResultCode code, String message, Object data, long totalCount) {
        return new Result()
                .setCode(code)
                .setSuccess(ResultCode.SUCCESS.equals(code))
                .setMessage(message)
                .setData(data)
                .setTotalCount(totalCount);
    }
}
