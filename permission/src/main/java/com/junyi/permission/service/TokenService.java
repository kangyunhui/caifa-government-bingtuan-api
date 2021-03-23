package com.junyi.permission.service;

public interface TokenService {
    String ACCESS_TOKEN = "Authorization";
    String USERID = "userId";
    String XZCODE = "xzcode";

    /**
     * 设置token过期
     *
     * @param token
     */
    void expireToken(String token);

    /**
     * 验证token
     *
     * @param token
     * @return
     */
    boolean validate(String token, String url);

    /**
     * 获取token
     *
     * @param username
     * @param xzcode
     * @return
     */
    String getToken(String username, String xzcode);
}
