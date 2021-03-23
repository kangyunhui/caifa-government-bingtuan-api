package com.junyi.permission.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author lijy
 * @create 2018年11月29日 10:03:19
 * @description 全局异常处理bean
 */
public class Result {

    /** 状态响应码 */
    private int code;

    /** 响应结果 成功/失败 */
    private boolean success;

    /** 响应信息 */
    private String message;

    /** 响应数据 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    /** 数据总数 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalCount;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Result setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        return this;
    }
}
