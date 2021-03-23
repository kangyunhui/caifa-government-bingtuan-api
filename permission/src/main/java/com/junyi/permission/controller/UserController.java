package com.junyi.permission.controller;

import com.junyi.permission.annotation.NoAuthorized;
import com.junyi.permission.constant.CommonConstant;
import com.junyi.permission.entity.User;
import com.junyi.permission.model.Result;
import com.junyi.permission.model.UserPassword;
import com.junyi.permission.service.RoleService;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "用户", tags = "用户")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final @NonNull UserService userService;
    private final @NonNull RoleService roleService;

    @ApiOperation(value = "查询", notes = "分页查询用户", tags = "用户")
    @GetMapping
    public Result get(
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_SIZE) int size,
            @RequestParam(required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(required = false, defaultValue = CommonConstant.ORDER) String order,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {
        return ResultUtils.success(
                userService.get(page, size, sort, order, name, startDate, endDate, phone, email));
    }

    @ApiOperation(value = "单条查询", notes = "单条记录查询", tags = "用户")
    @GetMapping("/{uuid}")
    public Result getOne(@PathVariable String uuid) {
        return ResultUtils.success(userService.getById(uuid));
    }

    @ApiOperation(value = "新增", notes = "新增用户", tags = "用户")
    @PostMapping
    public Result add(@RequestBody User user) {
        return ResultUtils.success(userService.add(user));
    }

    @ApiOperation(value = "更新", notes = "更新用户", tags = "用户")
    @PutMapping("/{id}")
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setGuid(id);
        userService.update(user);
        return ResultUtils.success();
    }

    @ApiOperation(value = "删除", notes = "删除用户", tags = "用户")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        userService.delete(id);
        return ResultUtils.success();
    }

    @ApiOperation(value = "批量删除", notes = "批量删除用户", tags = "用户")
    @DeleteMapping
    public Result delete(@RequestBody String[] ids) {
        userService.delete(ids);
        return ResultUtils.success();
    }

    @ApiOperation(value = "重置密码", notes = "重置用户密码", tags = "用户")
    @PutMapping("password/reset/{id}")
    public Result reset(HttpServletRequest request, @PathVariable String id) {
        userService.reset(request, id);
        return ResultUtils.success();
    }

    @NoAuthorized
    @ApiOperation(value = "修改密码", notes = "修改用户密码", tags = "用户")
    @PutMapping("password/update")
    public Result updatePassword(
            HttpServletRequest request, @RequestBody UserPassword userPassword) {
        userService.modifyPassword(request, userPassword);
        return ResultUtils.success();
    }

    @ApiOperation(value = "设置角色", notes = "设置该用户的角色", tags = "用户")
    @PutMapping("role/{userId}")
    public Result saveRoleInterface(@PathVariable String userId, @RequestBody String[] roleIds) {
        roleService.bindUser(roleIds, userId);
        return ResultUtils.success();
    }

    @NoAuthorized
    @ApiOperation(value = "查询用户角色", notes = "查询用户拥有的角色", tags = "角色")
    @GetMapping("/role/{userId}")
    public Result getRoleName(@PathVariable String userId) {
        return ResultUtils.success(roleService.getRoleList(userId));
    }
}
