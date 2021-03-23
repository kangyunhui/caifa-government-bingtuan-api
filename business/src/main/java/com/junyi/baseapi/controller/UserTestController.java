package com.junyi.baseapi.controller;

import com.junyi.baseapi.common.constant.Constants;
import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.common.exception.bean.ResultGenerator;
import com.junyi.baseapi.pojo.postgres.UserTest;
import com.junyi.baseapi.service.UserTestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户", tags = "用户")
// @RestController
// @RequestMapping("usertest")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTestController {

    private final @NonNull UserTestService userTestService;

    @ApiOperation(value = "查询", notes = "分页查询用户", tags = "用户")
    @GetMapping
    public Result get(
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false, defaultValue = Constants.SORT) String sort,
            @RequestParam(required = false, defaultValue = Constants.ORDER) String order,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResultGenerator.genSuccessResult(
                userTestService.get(page, size, sort, order, name, startDate, endDate));
    }

    @ApiOperation(value = "新增", notes = "新增用户", tags = "用户")
    @PostMapping
    public Result add(@RequestBody UserTest userTest) {
        userTestService.add(userTest);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "更新", notes = "更新用户", tags = "用户")
    @PutMapping("/{uuid}")
    public Result update(@RequestBody UserTest userTest, @PathVariable String uuid) {
        userTest.setUuid(uuid);
        userTestService.update(userTest);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除", notes = "删除用户", tags = "用户")
    @DeleteMapping("/{uuid}")
    public Result delete(@PathVariable String uuid) {
        userTestService.delete(uuid);
        return ResultGenerator.genSuccessResult();
    }
}
