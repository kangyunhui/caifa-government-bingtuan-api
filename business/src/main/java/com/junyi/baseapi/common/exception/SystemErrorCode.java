package com.junyi.baseapi.common.exception;

/**
 * 系统级别异常码
 *
 * @author kingapex
 * @version v1.0.0
 * @since v1.0.0 2017年3月27日 下午6:56:35
 */
public class SystemErrorCode {

    /** 无权限异常 */
    public static final String NO_PERMISSION = "001";
    /** 资源未能找到 */
    public static final String RESOURCE_NOT_FOUND = "002";
    /** 错误的请求参数 */
    public static final String INVALID_REQUEST_PARAMETER = "003";
    /** 错误的配置参数 */
    public static final String INVALID_CONFIG_PARAMETER = "004";
    /** 错误的配置参数 */
    public static final String INVALID_COTENT = "005";
    /** 服务器异常 */
    public static final String SERVER_ERROR = "006";
    /** 登录状态已失效 */
    public static final String INVALID_LOGIN = "007";
}
