package com.junyi.permission.controller;

import com.junyi.permission.annotation.NoAuthorized;
import com.junyi.permission.constant.CommonConstant;
import com.junyi.permission.entity.Button;
import com.junyi.permission.model.Result;
import com.junyi.permission.service.ButtonService;
import com.junyi.permission.util.CommonUtils;
import com.junyi.permission.util.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "按钮", tags = "按钮")
@RestController
@RequestMapping("button")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ButtonController {

    private final @NonNull ButtonService buttonService;

    @ApiOperation(value = "查询", notes = "分页查询", tags = "按钮")
    @GetMapping
    public Result get(
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = CommonConstant.PAGE_SIZE) int size,
            @RequestParam(required = false, defaultValue = CommonConstant.SORT) String sort,
            @RequestParam(required = false, defaultValue = CommonConstant.ORDER) String order) {
        return ResultUtils.success(buttonService.get(page, size, sort, order));
    }

    @ApiOperation(value = "单条查询", notes = "单条记录查询", tags = "按钮")
    @GetMapping("/{uuid}")
    public Result getOne(@PathVariable String uuid) {
        return ResultUtils.success(buttonService.getById(uuid));
    }

    @ApiOperation(value = "新增", notes = "新增", tags = "按钮")
    @PostMapping
    public Result add(@RequestBody Button button) {
        button.setGuid(CommonUtils.getUUID());
        buttonService.add(button);
        return ResultUtils.success(button);
    }

    @ApiOperation(value = "更新", notes = "更新", tags = "按钮")
    @PutMapping("/{uuid}")
    public Result update(@RequestBody Button button, @PathVariable String uuid) {
        button.setGuid(uuid);
        buttonService.update(button);
        return ResultUtils.success();
    }

    @ApiOperation(value = "删除", notes = "删除", tags = "按钮")
    @DeleteMapping("/{uuid}")
    public Result delete(@PathVariable String uuid) {
        buttonService.delete(uuid);
        return ResultUtils.success();
    }

    @ApiOperation(value = "设置接口", notes = "设置该按钮使用的接口", tags = "按钮")
    @PutMapping("interface/{btnId}")
    public Result saveRoleView(@PathVariable String btnId, @RequestBody String[] interfaceIds) {
        buttonService.saveButtonInterface(btnId, interfaceIds);
        return ResultUtils.success();
    }

    @NoAuthorized
    @ApiOperation(value = "分组查询", notes = "按钮分组信息，包含该角色的已选择信息", tags = "按钮")
    @GetMapping("group/{roleId}")
    public Result getGroupButton(@PathVariable String roleId) {
        return ResultUtils.success(buttonService.getGroupButton(roleId));
    }

    @NoAuthorized
    @ApiOperation(value = "查询用户可见按钮", notes = "查询用户可见的所有按钮", tags = "按钮")
    @GetMapping("user")
    public Result getUserView() {
        return ResultUtils.success(buttonService.getUserButton());
    }
}
