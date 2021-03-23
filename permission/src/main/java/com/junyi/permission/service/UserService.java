package com.junyi.permission.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.junyi.permission.entity.User;
import com.junyi.permission.model.Result;
import com.junyi.permission.model.UserPassword;
import com.junyi.permission.model.UserView;
import com.junyi.permission.util.JWTUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    IPage<UserView> get(
            int page,
            int size,
            String sort,
            String order,
            String name,
            String startDate,
            String endDate,
            String phone,
            String email);

    UserView add(User user);

    void update(User user);

    void delete(String id);

    void delete(String[] ids);

    void modifyPassword(HttpServletRequest request, UserPassword userPassword);

    void reset(HttpServletRequest request, String id);

    User verifyUser(String username, String password);

    UserView getUserInfo(HttpServletRequest request);

    static String getUserId(HttpServletRequest request) {
        String token = request.getHeader(TokenService.ACCESS_TOKEN);
        return JWTUtils.getValue(token, TokenService.USERID);
    }

    default String getXzcode(HttpServletRequest request) {
        String token = request.getHeader(TokenService.ACCESS_TOKEN);
        return JWTUtils.getValue(token, TokenService.XZCODE);
    }

    Result getByRole(int page, int size, String sort, String order, String roleId);

    Result getAbsentByRole(int page, int size, String sort, String order, String roleId);

    UserView getByName(String name);

    List<UserView> getByIds(String[] userIds);

    UserView getById(String uuid);
}
