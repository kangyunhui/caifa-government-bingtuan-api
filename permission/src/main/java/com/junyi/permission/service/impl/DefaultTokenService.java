package com.junyi.permission.service.impl;

import com.junyi.permission.service.TokenService;
import com.junyi.permission.util.JWTUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(
        prefix = "junyi.user",
        name = "pattern",
        havingValue = "default",
        matchIfMissing = true)
public class DefaultTokenService implements TokenService {

    @Value("${token.expired-minute:30}")
    private int expiredMinute;

    private static final Set<String> invalidToken = new HashSet<>();

    {
        new Thread(this::clearInvalidToken, "clearToken").start();
    }

    @Override
    public void expireToken(String token) {
        invalidToken.add(token);
    }

    @Override
    public String getToken(String userId, String xzcode) {
        String token =
                JWTUtils.sign(
                        expiredMinute,
                        new HashMap<String, String>(2) {
                            {
                                put(USERID, userId);
                                put(XZCODE, xzcode);
                            }
                        });

        return token;
    }

    @Override
    public boolean validate(String token, String url) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        boolean verify = JWTUtils.verify(token);
        boolean valid = !invalidToken.contains(token);
        return verify && valid;
    }

    private void clearInvalidToken() {
        while (true) {
            if (invalidToken.size() > 0) {
                invalidToken.removeIf(token -> !JWTUtils.verify(token));
            }
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
