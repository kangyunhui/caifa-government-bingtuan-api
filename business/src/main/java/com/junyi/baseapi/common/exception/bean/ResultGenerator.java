package com.junyi.baseapi.common.exception.bean;

/**
 * @author lijy
 * @create 2018年11月29日 10:02:18
 * @description 返回错误结果
 */
public class ResultGenerator {

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setSuccess(true)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setSuccess(true)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genFailResult(String message) {
        return new Result().setCode(ResultCode.FAIL).setSuccess(false).setMessage(message);
    }

    public static Result genFailResult(ResultCode code, String message) {
        return new Result().setCode(code).setSuccess(false).setMessage(message);
    }

    public static Result genResult(ResultCode code, String message, Object data) {
        return new Result()
                .setCode(code)
                .setSuccess(ResultCode.SUCCESS.equals(code))
                .setMessage(message)
                .setData(data);
    }

    public static Result genResult(ResultCode code, String message) {
        return new Result()
                .setCode(code)
                .setSuccess(ResultCode.SUCCESS.equals(code))
                .setMessage(message);
    }

    public static Result genErrorResult(String message) {
        return new Result()
                .setCode(ResultCode.INTERNAL_SERVER_ERROR)
                .setSuccess(false)
                .setMessage(message);
    }

    public static Result genResult(ResultCode code, String message, Object data, long totalCount) {
        return new Result()
                .setCode(code)
                .setSuccess(ResultCode.SUCCESS.equals(code))
                .setMessage(message)
                .setData(data)
                .setTotalCount(totalCount);
    }
}
