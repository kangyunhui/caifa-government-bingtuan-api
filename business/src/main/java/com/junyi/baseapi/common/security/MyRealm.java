package com.junyi.baseapi.common.security;

import com.junyi.baseapi.common.redis.JedisUtil;

import io.jsonwebtoken.Claims;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;

@Service
public class MyRealm extends AuthorizingRealm {

    private static org.apache.logging.log4j.Logger log =
            LogManager.getLogger(AuthorizingRealm.class.getName());

    /** 重写token方法，不然Shiro会报错 */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /** 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // String username = JwtUtil.getUsername(principals.toString());
        String value = JedisUtil.getJson(principals.toString());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        /*        //获取用户所有的权限

        simpleAuthorizationInfo.addStringPermissions(permission);*/
        return simpleAuthorizationInfo;
    }

    /** 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
            throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }
        try {
            Claims claims = JwtUtil.checkToken(token);
            if (claims == null) {
                throw new AuthenticationException("token invalid");
            }
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
