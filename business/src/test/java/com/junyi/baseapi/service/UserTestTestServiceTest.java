package com.junyi.baseapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.junyi.baseapi.mapper.UserTestMapper;
import com.junyi.baseapi.pojo.postgres.UserTest;
import com.junyi.baseapi.service.impl.UserTestServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;

/** 对UserService进行单元测试，使用mock方式测试 mock的情况下不会启动spring容器，所以要把除测试对象外的其他引用方法全部mock掉，否则会报空指针的异常 */
@RunWith(MockitoJUnitRunner.class)
class UserTestTestServiceTest {

    // 需要mock的对象用Mock注解
    @Mock private UserTestMapper userTestMapper;
    // 实际注入的对象用InjectMocks注解
    @InjectMocks private UserTestServiceImpl userService;

    // 初始化步骤
    @BeforeEach
    public void initMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGet() {
        IPage<UserTest> page = new Page<>(1, 1, 1);
        page.setRecords(
                new ArrayList<UserTest>() {
                    {
                        add(new UserTest());
                    }
                });
        // 第一步，mock掉在该service方法中使用的所有mapper层的方法
        when(userTestMapper.selectPage(any(IPage.class), any(Wrapper.class))).thenReturn(page);

        // 构造参数，执行断言
        IPage<UserTest> userPage = userService.get(1, 1, "creation_time", "desc", null, null, null);
        Assert.isTrue(userPage.getTotal() == 1, "元素总数不正确");
        Assert.isTrue(userPage.getCurrent() == 1, "当前页数不正确");
    }

    @Test
    void testAdd() {
        when(userTestMapper.insert(any())).thenReturn(1);

        userService.add(new UserTest());
    }

    @Test
    void testUpdate() {
        UserTest userTest = new UserTest();
        when(userTestMapper.selectById(any())).thenReturn(userTest);
        when(userTestMapper.updateById(any())).thenReturn(1);

        userService.update(userTest);
    }

    @Test
    void testDelete() {
        UserTest userTest = new UserTest();
        when(userTestMapper.selectById(any())).thenReturn(userTest);
        when(userTestMapper.deleteById(any())).thenReturn(1);

        userService.delete("1");
    }
}
