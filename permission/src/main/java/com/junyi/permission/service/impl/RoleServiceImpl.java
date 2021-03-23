package com.junyi.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junyi.permission.constant.CommonConstant;
import com.junyi.permission.entity.Role;
import com.junyi.permission.entity.RoleButton;
import com.junyi.permission.entity.UserRole;
import com.junyi.permission.event.UserInterfaceChangeEvent;
import com.junyi.permission.exception.PermissionException;
import com.junyi.permission.mapper.RoleButtonMapper;
import com.junyi.permission.mapper.RoleMapper;
import com.junyi.permission.mapper.UserRoleMapper;
import com.junyi.permission.model.Node;
import com.junyi.permission.model.UserView;
import com.junyi.permission.service.RoleService;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.CommonUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {

    private final @NonNull RoleMapper roleMapper;
    private final @NonNull UserRoleMapper userRoleMapper;
    private final @NonNull UserService userService;
    private final @NonNull RoleButtonMapper roleButtonMapper;

    @Resource private HttpServletRequest request;
    @Resource private ApplicationContext context;

    @Override
    public IPage<Role> get(
            int page,
            int size,
            String sort,
            String order,
            String name,
            String startDate,
            String endDate) {
        Page<Role> page1 = new Page<>(page, size, true);
        page1.addOrder(
                CommonConstant.ORDER.equals(order) ? OrderItem.desc(sort) : OrderItem.asc(sort));
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like(Role::getName, name);
        }
        if (!StringUtils.isEmpty(startDate)) {
            wrapper.between(Role::getCreateTime, startDate, endDate);
        }
        return roleMapper.selectPage(page1, wrapper);
    }

    @Override
    public Role add(Role role) {
        role.setGuid(CommonUtils.getUUID());
        role.setCreateUser(UserService.getUserId(request));
        roleMapper.insert(role);
        return role;
    }

    @Override
    public void update(Role role) {
        role.setUpdateUser(UserService.getUserId(request));
        roleMapper.updateById(role);
    }

    @Override
    public void delete(String id) {
        List<UserRole> userRoles =
                userRoleMapper.selectList(
                        Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, id));
        if (!CollectionUtils.isEmpty(userRoles)) {
            throw new PermissionException(
                    String.format(
                            "该角色绑定以下用户:%s，不可删除,请先删除用户再操作",
                            userRoles.stream()
                                    .map(UserRole::getUserId)
                                    .distinct()
                                    .collect(Collectors.joining(","))));
        }
        roleMapper.deleteById(id);
        context.publishEvent(new UserInterfaceChangeEvent(this));
    }

    @Override
    public void delete(String[] ids) {
        List<UserRole> userRoles =
                userRoleMapper.selectList(
                        Wrappers.<UserRole>lambdaQuery()
                                .in(UserRole::getRoleId, Arrays.asList(ids)));
        if (!CollectionUtils.isEmpty(userRoles)) {
            List<Role> roles =
                    roleMapper.selectList(
                            Wrappers.<Role>lambdaQuery()
                                    .in(
                                            Role::getGuid,
                                            userRoles.stream()
                                                    .map(UserRole::getRoleId)
                                                    .distinct()));
            throw new PermissionException(
                    String.format(
                            "以下角色有绑定用户:%s，不可删除,请先删除用户再操作",
                            roles.stream()
                                    .map(Role::getName)
                                    .distinct()
                                    .collect(Collectors.joining(","))));
        }
        roleMapper.deleteBatchIds(Arrays.asList(ids));
        context.publishEvent(new UserInterfaceChangeEvent(this));
    }

    @Override
    public List<Node> getChecks(String userId) {
        List<Role> roleList = roleMapper.selectList(null);

        // 非系统管理员只能看到自己创建的角色和自己拥有的角色
        String currentUser = UserService.getUserId(request);
        UserView userView = userService.getById(userId);
        List<Role> currentUserRoleResults = userRoleMapper.selectUserRole(userId);
        if (!CommonConstant.ADMIN.equals(userView.getName())) {
            Set<String> currentRole =
                    currentUserRoleResults.stream().map(Role::getGuid).collect(Collectors.toSet());
            roleList =
                    roleList.stream()
                            .filter(
                                    r ->
                                            currentUser.equals(r.getCreateUser())
                                                    || currentRole.contains(r.getGuid()))
                            .collect(Collectors.toList());
        }

        List<UserRole> userRoleList =
                userRoleMapper.selectList(
                        Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        Set<String> checkedSet =
                userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        return roleList.stream()
                .map(r -> new Node(r.getGuid(), r.getName(), checkedSet.contains(r.getGuid())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleButton(String roleId, String[] buttonIds) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new PermissionException("没有找到要操作的角色");
        }
        List<RoleButton> list = new ArrayList<>(buttonIds.length);
        for (String buttonId : buttonIds) {
            RoleButton roleButton = new RoleButton();
            roleButton.setGuid(CommonUtils.getUUID());
            roleButton.setRoleId(roleId);
            roleButton.setButtonId(buttonId);
            list.add(roleButton);
        }
        role.setUpdateUser(UserService.getUserId(request));
        roleMapper.updateById(role);
        // 先删除原先的按钮权限再插入
        roleButtonMapper.delete(
                Wrappers.<RoleButton>lambdaQuery().eq(RoleButton::getRoleId, roleId));
        if (!list.isEmpty()) {
            roleButtonMapper.insertBatchSomeColumn(list);
        }

        // 刷新内存中的接口权限
        context.publishEvent(new UserInterfaceChangeEvent(this));
    }

    @Override
    public List<Role> getRoleList(String userId) {
        return userRoleMapper.selectUserRole(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindUser(String roleId, String[] userIds) {
        saveUserRole(userIds, new String[] {roleId});
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindUser(String[] roleIds, String userId) {
        saveUserRole(new String[] {userId}, roleIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unbindUser(String roleId, String[] userIds) {
        userRoleMapper.delete(
                Wrappers.<UserRole>lambdaQuery()
                        .eq(UserRole::getRoleId, roleId)
                        .in(UserRole::getUserId, Arrays.asList(userIds)));
        for (String userId : userIds) {
            context.publishEvent(new UserInterfaceChangeEvent(this, userId));
        }
    }

    @Override
    public Role getById(String uuid) {
        return roleMapper.selectById(uuid);
    }

    public void saveUserRole(String[] userIds, String[] roleIds) {
        List<UserRole> list = new ArrayList<>(roleIds.length);
        for (String userId : userIds) {
            for (String roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setGuid(CommonUtils.getUUID());
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                list.add(userRole);
            }
            userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
            if (!CollectionUtils.isEmpty(list)) {
                userRoleMapper.insertBatchSomeColumn(list);
            }
            context.publishEvent(new UserInterfaceChangeEvent(this, userId));
        }
    }
}
