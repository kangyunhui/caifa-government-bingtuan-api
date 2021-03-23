package com.junyi.baseapi.service;

import com.junyi.baseapi.pojo.postgres.UserTest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author wangmingyue
 * @description 限制用户登录
 */
@Slf4j
@Component
public class LoginSessionService {

    public static Map<String, HttpSession> MAP_SESSION = new HashMap<>(); // session缓存
    public static Map<String, String> MAP_TOKEN = new HashMap<>(); // token缓存

    boolean doLogin(HttpServletRequest request, UserTest userTest, String jwt, String type) {
        String userID = userTest.getUuid();
        // 缓存中当前用户的登录设备session
        HttpSession cacheSession = MAP_SESSION.get(type + userID);
        // 此次登录的当前用户的设备session
        HttpSession requestSession = request.getSession();
        boolean jwtEmpty = StringUtils.isEmpty(jwt);
        log.info("jwt是否为空:" + jwtEmpty);
        if (requestSession.equals(cacheSession)) { // 两次登录用户session相同
            return true; // 相同客户端重复登录
        } else {
            log.info("当前登录session与该用户已缓存session不一致");
        }
        if (cacheSession == null) {
            // 获取当前登录用户的session
            HttpSession session = request.getSession();
            // 将用户信息存到session中
            session.setAttribute("user", userTest);
            // 添加到缓存
            MAP_SESSION.put(type + userID, session);
            if (!jwtEmpty) {
                log.info(String.format("当前用户 %s 未登录且jwt值不为空, 新增jwt", userTest.getUserName()));
                LoginSessionService.MAP_TOKEN.put(type + userID, jwt);
            }
            log.info(String.format("当前用户 %s 未登录, 新增session", userTest.getUserName()));
            return true;
        }
        return false;
    }

    void doLogout(String userID) {
        if (MAP_SESSION.get(userID) == null) return; // 未登录, 不需要登出
        HttpSession session = MAP_SESSION.get(userID);
        MAP_SESSION.remove(userID);

        try {
            // 得到session的所属性合集
            Enumeration e = session.getAttributeNames();
            // 删除所有属性
            while (e.hasMoreElements()) {
                String sessionName = (String) e.nextElement();
                session.removeAttribute(sessionName);
            }
            // 废除该session
            session.invalidate();
        } catch (Exception e) {
            log.warn(e.toString(), e);
            log.warn(String.format("session可能失效, userID:%s", userID));
        }
        // 清除登录的用户token
        LoginSessionService.MAP_TOKEN.remove(userID);
        log.info(String.format("登出用户 %s jwt清空", userID));
    }

    public void logout(String token) {
        if (MAP_TOKEN.containsValue(token)) {
            // 有这个token, 且token已经过期
            List<String> keys = getKey(MAP_TOKEN, token);
            if (CollectionUtils.isEmpty(keys)) {
                log.warn("登出失败, token所属用户未登录");
            }
            if (keys.size() != 1) {
                log.warn("登出失败, token所属用户多个");
            }
            // 登出
            doLogout(keys.get(0));
        } else {
            log.info("token不存在");
        }
    }

    private static List<String> getKey(Map<String, String> map, Object value) {
        List<String> keyList = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key).equals(value)) {
                keyList.add(key);
            }
        }
        return keyList;
    }
}
