package com.junyi.baseapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junyi.baseapi.common.constant.Constants;
import com.junyi.baseapi.common.exception.ResourceNotFoundException;
import com.junyi.baseapi.common.util.CommonUtils;
import com.junyi.baseapi.mapper.UserTestMapper;
import com.junyi.baseapi.pojo.postgres.UserTest;
import com.junyi.baseapi.service.UserTestService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/** 此类只是一个样例，并非真实业务 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTestServiceImpl implements UserTestService {

    private final @NonNull UserTestMapper userTestMapper;

    @Resource private HttpServletRequest request;

    @Override
    public IPage<UserTest> get(
            int page,
            int size,
            String sort,
            String order,
            String name,
            String startDate,
            String endDate) {
        Page<UserTest> page1 = new Page<>(page, size, true);
        page1.addOrder(Constants.ORDER.equals(order) ? OrderItem.desc(sort) : OrderItem.asc(sort));
        LambdaQueryWrapper<UserTest> wrapper = Wrappers.lambdaQuery();

        if (!StringUtils.isEmpty(name)) {
            wrapper.like(UserTest::getUserName, name);
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            wrapper.between(UserTest::getCreationTime, startDate, endDate);
        }

        return userTestMapper.selectPage(page1, wrapper);
    }

    @Override
    public void add(UserTest userTest) {
        userTest.setUuid(CommonUtils.getUUID());
        userTestMapper.insert(userTest);
    }

    @Override
    public void update(UserTest userTest) {
        UserTest userTestOrg = userTestMapper.selectById(userTest.getUuid());
        if (userTestOrg == null) {
            throw new ResourceNotFoundException("没有找到要更新的用户");
        }
        userTestOrg.setCompanyName(userTest.getCompanyName());
        userTestMapper.updateById(userTestOrg);
    }

    @Override
    public void delete(String id) {
        UserTest userTest = userTestMapper.selectById(id);
        if (userTest == null) {
            throw new ResourceNotFoundException("没有找到要删除的用户");
        }
        userTestMapper.deleteById(id);
    }
}
