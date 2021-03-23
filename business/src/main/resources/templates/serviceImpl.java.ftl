package com.junyi.baseapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junyi.baseapi.common.constant.Constants;
import com.junyi.baseapi.common.exception.ResourceNotFoundException;
import ${package.Mapper}.${entity}Mapper;
import ${package.Entity}.${entity};
import ${package.Service}.${entity}Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ${entity}ServiceImpl extends ServiceImpl<${entity}Mapper, ${entity}> implements ${entity}Service {

    private final @NonNull ${entity}Mapper ${entity?uncap_first}Mapper;

    @Resource private HttpServletRequest request;

    @Override
    public IPage<${entity}> get(
            int page,
            int size,
            String sort,
            String order) {
        Page<${entity}> page1 = new Page<>(page, size, true);
        page1.addOrder(Constants.ORDER.equals(order) ? OrderItem.desc(sort) : OrderItem.asc(sort));
        LambdaQueryWrapper<${entity}> wrapper = Wrappers.lambdaQuery();

        return ${entity?uncap_first}Mapper.selectPage(page1, wrapper);
    }

    @Override
    public void update(${entity} ${entity?uncap_first}) {
        ${entity} ${entity?uncap_first}Org = ${entity?uncap_first}Mapper.selectById(${entity?uncap_first}.getUuid());
        if (${entity?uncap_first}Org == null) {
            throw new ResourceNotFoundException("没有找到要更新的记录");
        }
        ${entity?uncap_first}Mapper.updateById(${entity?uncap_first}Org);
    }

    @Override
    public void delete(String id) {
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Mapper.selectById(id);
        if (${entity?uncap_first} == null) {
            throw new ResourceNotFoundException("没有找到要删除的记录");
        }
        ${entity?uncap_first}Mapper.deleteById(id);
    }
}