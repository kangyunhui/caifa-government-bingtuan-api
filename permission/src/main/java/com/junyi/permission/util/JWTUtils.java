package com.junyi.permission.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    /** 加密钥匙，随机字符串 */
    private static final String KEY = "d9528d5e718a48f3b6b7fc725825815d0463f6427";

    /**
     * 生成token
     *
     * @param minute 过期时间，分钟
     * @param claims 携带参数
     * @return token字符串
     */
    public static String sign(int minute, Map<String, String> claims) {
        try {
            Date date = new Date(System.currentTimeMillis() + minute * 60 * 1000);
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            Map<String, Object> map = new HashMap<>(2);
            map.put("type", "JWT");
            map.put("alg", "HS256");
            JWTCreator.Builder builder = JWT.create().withHeader(map);
            for (Map.Entry<String, String> entry : claims.entrySet()) {
                builder.withClaim(entry.getKey(), entry.getValue());
            }

            return builder.withExpiresAt(date).sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * 获取token中的信息
     *
     * @param token
     * @param key
     * @return
     */
    public static String getValue(String token, String key) {
        try {
            return JWT.decode(token).getClaim(key).asString();
        } catch (Exception e) {
            return null;
        }
    }
}
