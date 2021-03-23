package com.junyi.permission.controller;

import com.junyi.permission.annotation.NoAuthorized;
import com.junyi.permission.constant.CommonConstant;
import com.junyi.permission.entity.Role;
import com.junyi.permission.model.Result;
import com.junyi.permission.service.RoleService;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "角色", tags = "角色")
@RestController
@RequestMapping("role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final @NonNull RoleService roleService;
    private final @NonNull UserService userService;

    @ApiOperation(value = "查询", notes = "分页查询", tags = "角色")
    @GetMapping
    public Result get(
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_SIZE) int size,
            @RequestParam(required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(required = false, defaultValue = CommonConstant.ORDER) String order,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResultUtils.success(
                roleService.get(page, size, sort, order, name, startDate, endDate));
    }

    @ApiOperation(value = "单条查询", notes = "单条记录查询", tags = "角色")
    @GetMapping("/{uuid}")
    public Result getOne(@PathVariable String uuid) {
        return ResultUtils.success(roleService.getById(uuid));
    }

    @ApiOperation(value = "新增", notes = "新增", tags = "角色")
    @PostMapping
    public Result add(@RequestBody Role role) {
        return ResultUtils.success(roleService.add(role));
    }

    @ApiOperation(value = "更新", notes = "更新", tags = "角色")
    @PutMapping("/{id}")
    public Result update(@RequestBody Role role, @PathVariable String id) {
        role.setGuid(id);
        roleService.update(role);
        return ResultUtils.success();
    }

    @ApiOperation(value = "删除", notes = "删除", tags = "角色")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        roleService.delete(id);
        return ResultUtils.success();
    }

    @ApiOperation(value = "批量删除", notes = "批量删除", tags = "角色")
    @DeleteMapping
    public Result delete(@RequestBody String[] ids) {
        roleService.delete(ids);
        return ResultUtils.success();
    }

    @NoAuthorized
    @ApiOperation(value = "查询所有角色", notes = "查询所有角色，包含用户是否拥有的标识", tags = "角色")
    @GetMapping("/check/{userId}")
    public Result get(@PathVariable String userId) {
        return ResultUtils.success(roleService.getChecks(userId));
    }

    @ApiOperation(value = "设置按钮权限", notes = "设置该角色可操作的按钮", tags = "角色")
    @PutMapping("button/{roleId}")
    public Result saveRoleButton(@PathVariable String roleId, @RequestBody String[] buttonIds) {
        roleService.saveRoleButton(roleId, buttonIds);
        return ResultUtils.success();
    }

    @ApiOperation(value = " 查询已设置用户", notes = "获取该角色已设置用户", tags = "角色")
    @GetMapping("user/{roleId}")
    public Result getUserList(
            @PathVariable String roleId,
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_SIZE) int size,
            @RequestParam(required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(required = false, defaultValue = CommonConstant.ORDER) String order) {
        return userService.getByRole(page, size, sort, order, roleId);
    }

    @ApiOperation(value = "查询未设置用户", notes = "获取该角色未设置用户", tags = "角色")
    @GetMapping("user/absent/{roleId}")
    public Result getAbsentUserList(
            @PathVariable String roleId,
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_SIZE) int size,
            @RequestParam(required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(required = false, defaultValue = CommonConstant.ORDER) String order) {
        return userService.getAbsentByRole(page, size, sort, order, roleId);
    }

    @ApiOperation(value = "绑定用户", notes = "为该角色绑定用户", tags = "角色")
    @PutMapping("user/bind/{roleId}")
    public Result bindUser(@PathVariable String roleId, @RequestBody String[] userIds) {
        roleService.bindUser(roleId, userIds);
        return ResultUtils.success();
    }

    @ApiOperation(value = "解绑用户", notes = "为该角色解除用户绑定", tags = "角色")
    @PutMapping("user/ubind/{roleId}")
    public Result unbindUser(@PathVariable String roleId, @RequestBody String[] userIds) {
        roleService.unbindUser(roleId, userIds);
        return ResultUtils.success();
    }
}
