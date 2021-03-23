package com.junyi.permission.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.junyi.permission.entity.User;
import com.junyi.permission.model.Result;
import com.junyi.permission.model.UserPassword;
import com.junyi.permission.model.UserView;
import com.junyi.permission.service.UserService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = "junyi.user", name = "pattern", havingValue = "sso")
public class SsoUserService implements UserService {

    @Value("${junyi.sso.url:null}")
    private String ssoUrl;

    private final @NonNull RestTemplate restTemplate;
    private final @NonNull HttpServletRequest request;

    @Override
    public IPage<UserView> get(
            int page,
            int size,
            String sort,
            String order,
            String name,
            String startDate,
            String endDate,
            String phone,
            String email) {
        return null;
    }

    @Override
    public UserView add(User user) {
        return null;
    }

    @Override
    public void update(User user) {}

    @Override
    public void delete(String id) {}

    @Override
    public void delete(String[] ids) {}

    @Override
    public void modifyPassword(HttpServletRequest request, UserPassword userPassword) {}

    @Override
    public void reset(HttpServletRequest request, String id) {}

    @Override
    public User verifyUser(String username, String password) {
        return null;
    }

    @Override
    public UserView getUserInfo(HttpServletRequest request) {
        return null;
    }

    @Override
    public Result getByRole(int page, int size, String sort, String order, String roleId) {
        return null;
    }

    @Override
    public Result getAbsentByRole(int page, int size, String sort, String order, String roleId) {
        return null;
    }

    @Override
    public UserView getByName(String name) {
        return null;
    }

    @Override
    public List<UserView> getByIds(String[] userIds) {
        return null;
    }

    @Override
    public UserView getById(String uuid) {
        return null;
    }
}
