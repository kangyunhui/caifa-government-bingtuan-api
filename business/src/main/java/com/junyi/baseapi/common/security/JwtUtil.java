package com.junyi.baseapi.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.stereotype.Component;

import java.util.Date;

import javax.servlet.ServletException;

@Component
public class JwtUtil {

    static final String base64EncodedSecretKey = "base64YfdzxSecretPrivateKey"; // 私钥
    static long FRONT_TOKEN_EXP = 0; // 前台用户过期时间,单位毫秒
    static long MANAGER_TOKEN_EXP = 0; // 后台管理过期时间,单位毫秒

    public static Claims checkToken(String token) throws ServletException {
        try {
            final Claims claims =
                    Jwts.parser()
                            .setSigningKey(base64EncodedSecretKey)
                            .parseClaimsJws(token)
                            .getBody();
            return claims;
        } catch (ExpiredJwtException e1) {
            throw new ServletException("token expired");
        } catch (Exception e) {
            throw new ServletException("other token exception");
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 生成签名,设置过期时间
     *
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + (60 * 10 * 1000));
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
    }
}
