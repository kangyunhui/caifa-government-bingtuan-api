package com.junyi.baseapi.common.util;

import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.common.exception.bean.ResultCode;
import com.junyi.baseapi.common.exception.bean.ResultGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: JsonReturnUtil @Description: 仅仅为了统一构造Json返回对象<br>
 * 不允许添加任何方法
 *
 * @author Cheng Fan
 * @date 2016年12月27日 下午10:45:05
 */
@Slf4j
public final class JsonReturnUtil {

    /**
     * 成功，返回对象
     *
     * @param data 返回对象
     * @return 构造对象
     */
    public static Result success(Object data) {
        return ResultGenerator.genResult(ResultCode.SUCCESS, ResultCode.SUCCESS.name(), data);
    }

    /**
     * 成功，返回对象
     *
     * @param data 返回对象
     * @return 构造对象
     */
    public static Result success(String msg, Object data, long totalCount) {
        return ResultGenerator.genResult(ResultCode.SUCCESS, msg, data, totalCount);
    }

    /**
     * 成功，返回对象
     *
     * @param msg 返回消息
     * @return 构造对象
     */
    public static Result success(String msg) {
        return ResultGenerator.genResult(ResultCode.SUCCESS, msg);
    }

    /**
     * 成功，返回对象
     *
     * @param data 返回对象
     * @param msg 返回消息
     * @return 构造对象
     */
    public static Result success(String msg, Object data) {
        return ResultGenerator.genResult(ResultCode.SUCCESS, msg, data);
    }

    /**
     * 失败返回内容
     *
     * @param msg
     * @return
     */
    public static Result fail(String msg) {
        log.warn(msg);
        throw new IllegalArgumentException(msg);
    }

    /**
     * 异常返回内容
     *
     * @param msg
     * @return
     */
    public static Result error(String msg) {
        return ResultGenerator.genErrorResult(msg);
    }
}
