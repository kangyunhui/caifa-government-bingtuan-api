package com.junyi.permission.controller;

import com.junyi.permission.annotation.NoAuthorized;
import com.junyi.permission.model.Result;
import com.junyi.permission.service.InterfaceService;
import com.junyi.permission.util.ResultUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "接口", tags = "接口")
@RestController
@RequestMapping("interface")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InterfaceController {

    private final @NonNull InterfaceService interService;

    @NoAuthorized
    @ApiOperation(value = "查询", notes = "接口分组信息，包含该按钮的已选择信息", tags = "接口")
    @GetMapping("group/{buttonId}")
    public Result getGroupInterface(@PathVariable String buttonId) {
        return ResultUtils.success(interService.getGroupInterface(buttonId));
    }

    @NoAuthorized
    @ApiOperation(value = "查询", notes = "查询所有接口信息", tags = "接口")
    @GetMapping
    public Result get() {
        return ResultUtils.success(interService.get());
    }

    @NoAuthorized
    @ApiOperation(value = "查询按钮已拥有", notes = "查询按钮已拥有的所有接口id", tags = "接口")
    @GetMapping("check/{buttonId}")
    public Result getCheckedInterface(@PathVariable String buttonId) {
        return ResultUtils.success(interService.getCheckedInterface(buttonId));
    }
}
