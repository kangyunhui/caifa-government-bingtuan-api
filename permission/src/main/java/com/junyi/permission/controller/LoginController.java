package com.junyi.permission.controller;

import com.junyi.permission.annotation.NoAuthorized;
import com.junyi.permission.annotation.Public;
import com.junyi.permission.entity.User;
import com.junyi.permission.model.Result;
import com.junyi.permission.model.UserView;
import com.junyi.permission.service.TokenService;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Api("登录")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final @NonNull TokenService tokenService;
    private final @NonNull UserService userService;

    @Public
    @ApiOperation(value = "用户登录", notes = "用户登录，返回token", tags = "登录")
    @PostMapping("login")
    public Result login(@RequestBody Map<String, String> param) {
        String username = param.get("username");
        String password = param.get("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultUtils.fail("用户名或密码为空");
        }
        User user = userService.verifyUser(username, password);
        String token = tokenService.getToken(user.getGuid(), user.getXzcode());
        Map<String, String> result =
                new HashMap<String, String>(1) {
                    {
                        put(TokenService.ACCESS_TOKEN, token);
                    }
                };

        return ResultUtils.success(result);
    }

    /**
     * 退出到登录界面
     *
     * @return 登录界面
     */
    @NoAuthorized
    @ApiOperation(value = "登出", notes = "用户登出", tags = "登录")
    @GetMapping("loginout")
    public Result loginOut(HttpServletRequest request) {
        tokenService.expireToken(request.getHeader(TokenService.ACCESS_TOKEN));
        return ResultUtils.success();
    }

    @NoAuthorized
    @ApiOperation(value = "用户信息", notes = "用户信息", tags = "用户")
    @GetMapping("userinfo")
    public Result userinfo(HttpServletRequest request) {
        UserView userView = userService.getUserInfo(request);
        return ResultUtils.success(userView);
    }

    @Public
    @ApiOperation(value = "注册", notes = "注册", tags = "用户")
    @PostMapping("register")
    public Result register(@RequestBody User user) {
        return ResultUtils.success(userService.add(user));
    }
}
