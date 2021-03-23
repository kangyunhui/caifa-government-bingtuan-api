package com.junyi.baseapi.controller;


import com.junyi.baseapi.common.constant.Constants;
import com.junyi.baseapi.common.exception.bean.Result;
import com.junyi.baseapi.common.exception.bean.ResultGenerator;
import com.junyi.baseapi.common.util.CommonUtils;
import ${package.Entity}.${entity};
import ${package.Service}.${entity}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Api(value = "${entity}", tags = "${entity}")
@RestController
@RequestMapping("${entity?uncap_first}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ${entity}Controller {

    private final @NonNull ${entity}Service ${entity?uncap_first}Service;

    @ApiOperation(value = "查询", notes = "分页查询", tags = "${entity}")
    @GetMapping
    public Result get(@RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE) int page,
                      @RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
                      @RequestParam(required = false, defaultValue = Constants.SORT) String sort,
                      @RequestParam(required = false, defaultValue = Constants.ORDER) String order) {
        return ResultGenerator.genSuccessResult(${entity?uncap_first}Service.get(page, size, sort, order));
    }

    @ApiOperation(value = "单条查询", notes = "单条记录查询", tags = "${entity}")
    @GetMapping("/{uuid}")
    public Result getOne(@PathVariable String uuid) {
        return ResultGenerator.genSuccessResult(${entity?uncap_first}Service.getById(uuid));
    }

    @ApiOperation(value = "新增", notes = "新增", tags = "${entity}")
    @PostMapping
    public Result add(@RequestBody ${entity} ${entity?uncap_first}) {
        ${entity?uncap_first}.setUuid(CommonUtils.getUUID());
        if(${entity?uncap_first}Service.save(${entity?uncap_first})){
            return ResultGenerator.genSuccessResult(${entity?uncap_first});
        }
            return ResultGenerator.genFailResult("保存失败");
    }

    @ApiOperation(value = "更新", notes = "更新", tags = "${entity}")
    @PutMapping("/{uuid}")
    public Result update(@RequestBody ${entity} ${entity?uncap_first}, @PathVariable String uuid) {
        ${entity?uncap_first}.setUuid(uuid);
        ${entity?uncap_first}Service.update(${entity?uncap_first});
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除", notes = "删除", tags = "${entity}")
    @DeleteMapping("/{uuid}")
    public Result delete(@PathVariable String uuid) {
        ${entity?uncap_first}Service.delete(uuid);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "批量删除", notes = "批量删除", tags = "${entity}")
    @DeleteMapping
    public Result delete(@RequestBody String[] uuids) {
        ${entity?uncap_first}Service.removeByIds(Arrays.asList(uuids));
        return ResultGenerator.genSuccessResult();
    }
}