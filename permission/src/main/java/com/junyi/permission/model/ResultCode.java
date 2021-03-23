package com.junyi.permission.model;

/**
 * @author lijy
 * @create 2018年11月29日 10:00:59
 * @description http错误码枚举类
 */
public enum ResultCode {
    SUCCESS(200), // 成功
    FAIL(400), // 失败
    UNAUTHORIZED(401), // 未认证（签名错误） 未登录
    FORBIDDEN(403), // 禁止操作
    NOT_FOUND(404), // 接口不存在
    INTERNAL_SERVER_ERROR(500); // 服务器内部错误

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
