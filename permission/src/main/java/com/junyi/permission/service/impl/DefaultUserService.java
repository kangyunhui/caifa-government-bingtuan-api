package com.junyi.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junyi.permission.constant.CommonConstant;
import com.junyi.permission.entity.User;
import com.junyi.permission.entity.UserRole;
import com.junyi.permission.event.UserInterfaceChangeEvent;
import com.junyi.permission.exception.PermissionException;
import com.junyi.permission.mapper.UserMapper;
import com.junyi.permission.mapper.UserRoleMapper;
import com.junyi.permission.mapper.UserViewMapper;
import com.junyi.permission.model.Result;
import com.junyi.permission.model.UserPassword;
import com.junyi.permission.model.UserView;
import com.junyi.permission.service.TokenService;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.CommonUtils;
import com.junyi.permission.util.JWTUtils;
import com.junyi.permission.util.Md5Util;
import com.junyi.permission.util.ResultUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(
        prefix = "junyi.user",
        name = "pattern",
        havingValue = "default",
        matchIfMissing = true)
public class DefaultUserService implements UserService {

    private static final String PASSWORD_DEFAULT = "junyi123";

    private final @NonNull UserRoleMapper userRoleMapper;
    private final @NonNull UserMapper userMapper;
    private final @NonNull UserViewMapper userViewMapper;
    private final @NonNull DefaultTokenService tokenService;

    @Resource private HttpServletRequest request;
    @Resource private ApplicationContext context;

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
        Page<UserView> page1 = new Page<>(page, size, true);
        page1.addOrder(
                CommonConstant.ORDER.equals(order) ? OrderItem.desc(sort) : OrderItem.asc(sort));
        LambdaQueryWrapper<UserView> wrapper = Wrappers.lambdaQuery();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like(UserView::getName, name);
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            wrapper.between(UserView::getCreateTime, startDate, endDate);
        }
        if (!StringUtils.isEmpty(phone)) {
            wrapper.like(UserView::getPhone, phone);
        }
        if (!StringUtils.isEmpty(email)) {
            wrapper.like(UserView::getEmail, email);
        }

        Page<UserView> userPage = userViewMapper.selectPage(page1, wrapper);
        return userPage;
    }

    @Override
    public UserView add(User user) {
        if (userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getName, user.getName()))
                > 0) {
            throw new PermissionException("用户名已被使用");
        }
        user.setGuid(CommonUtils.getUUID());
        String salt = getSalt();
        user.setSalt(salt);
        user.setCreateUser(UserService.getUserId(request));
        setPassword(user, user.getPassword());
        userMapper.insert(user);
        return userViewMapper.selectById(user.getGuid());
    }

    private String getSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    private void setPassword(HttpServletRequest request, String id, String newPassword) {
        String token = request.getHeader(TokenService.ACCESS_TOKEN);
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new PermissionException("没有找到要更新的用户");
        }
        setPassword(user, newPassword);
        logoutCurrent(user, token);
        user.setUpdateUser(UserService.getUserId(request));
        userMapper.updateById(user);
    }

    private void setPassword(User user, String newPassword) {
        String salt = user.getSalt();
        String password = Md5Util.getMD5(newPassword) + salt;
        user.setPassword(Md5Util.getMD5(password));
    }

    @Override
    public void update(User user) {
        User userOrg = userMapper.selectById(user.getGuid());
        if (userOrg == null) {
            throw new PermissionException("没有找到要更新的用户");
        }
        userOrg.setDescription(user.getDescription());
        userOrg.setEmail(user.getEmail());
        userOrg.setPhone(user.getPhone());
        userOrg.setUpdateUser(UserService.getUserId(request));
        userOrg.setIsEnable(user.getIsEnable());
        userOrg.setXzcode(user.getXzcode());
        userOrg.setLevel(user.getLevel());
        userOrg.setCompany(user.getCompany());
        userMapper.updateById(userOrg);
    }

    @Override
    public void delete(String id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new PermissionException("没有找到要删除的用户");
        }
        if ("admin".equals(user.getName())) {
            throw new PermissionException("管理员不能删除");
        }
        userMapper.deleteById(id);
        userRoleMapper.delete(
                Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, user.getName()));
        context.publishEvent(new UserInterfaceChangeEvent(this, user.getName()));
    }

    @Override
    public void delete(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<User> userList = userMapper.selectBatchIds(idList);
        if (userList.stream().anyMatch(u -> "admin".equals(u.getName()))) {
            throw new PermissionException("管理员不能删除");
        }
        userMapper.deleteBatchIds(idList);

        userRoleMapper.delete(
                Wrappers.<UserRole>lambdaQuery()
                        .in(
                                UserRole::getUserId,
                                userList.stream().map(User::getName).collect(Collectors.toList())));
        context.publishEvent(new UserInterfaceChangeEvent(this));
    }

    @Override
    public void modifyPassword(HttpServletRequest request, UserPassword userPassword) {
        String token = request.getHeader(TokenService.ACCESS_TOKEN);
        User user = userMapper.selectById(userPassword.getGuid());
        if (user == null) {
            throw new PermissionException("没有找到要更新的用户");
        }
        if (!user.getGuid().equals(JWTUtils.getValue(token, TokenService.USERID))) {
            throw new PermissionException("不能修改其他用户的密码");
        }
        if (!user.getPassword()
                .equals(
                        Md5Util.getMD5(
                                Md5Util.getMD5(userPassword.getPassword()) + user.getSalt()))) {
            throw new PermissionException("原密码不正确");
        }
        setPassword(user, userPassword.getNewPassword());
        logoutCurrent(user, token);
        user.setUpdateUser(UserService.getUserId(request));
        userMapper.updateById(user);
    }

    private void logoutCurrent(User user, String token) {
        if (user.getGuid().equals(JWTUtils.getValue(token, TokenService.USERID))) {
            tokenService.expireToken(token);
        }
    }

    @Override
    public void reset(HttpServletRequest request, String id) {
        setPassword(request, id, PASSWORD_DEFAULT);
    }

    @Override
    public User verifyUser(String username, String password) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, username));
        if (user == null) {
            throw new PermissionException("用户名或密码错误");
        }
        password = Md5Util.getMD5(password) + user.getSalt();
        if (!Md5Util.getMD5(password).equals(user.getPassword())) {
            throw new PermissionException("用户名或密码错误");
        }
        if (!user.getIsEnable()) {
            throw new PermissionException("用户不可用，请联系管理员开通");
        }
        return user;
    }

    @Override
    public UserView getUserInfo(HttpServletRequest request) {
        return getUser(request.getHeader(TokenService.ACCESS_TOKEN));
    }

    @Override
    public Result getByRole(int page, int size, String sort, String order, String roleId) {
        sort = CommonUtils.convertSort(sort, UserView.class);
        Page<UserView> page1 = new Page<>(page, size, true);
        page1.addOrder(
                CommonConstant.ORDER.equals(order) ? OrderItem.desc(sort) : OrderItem.asc(sort));
        return ResultUtils.success(userViewMapper.selectByRole(page1, roleId));
    }

    @Override
    public Result getAbsentByRole(int page, int size, String sort, String order, String roleId) {
        sort = CommonUtils.convertSort(sort, UserView.class);
        Page<UserView> page1 = new Page<>(page, size, true);
        page1.addOrder(
                CommonConstant.ORDER.equals(order) ? OrderItem.desc(sort) : OrderItem.asc(sort));
        return ResultUtils.success(userViewMapper.selectAbsentByRole(page1, roleId));
    }

    @Override
    public UserView getByName(String name) {
        return userViewMapper.selectOne(
                Wrappers.<UserView>lambdaQuery().eq(UserView::getName, name));
    }

    @Override
    public List<UserView> getByIds(String[] userIds) {
        return userViewMapper.selectBatchIds(Arrays.asList(userIds));
    }

    @Override
    public UserView getById(String uuid) {
        return userViewMapper.selectById(uuid);
    }

    private UserView getUser(String token) {
        String userId = JWTUtils.getValue(token, TokenService.USERID);
        UserView userView = userViewMapper.selectById(userId);
        return userView;
    }
}
