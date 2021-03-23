package com.junyi.baseapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.junyi.baseapi.common.exception.bean.ResultCode;
import com.junyi.baseapi.service.UserTestService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** 对接口进行测试，使用mock方式测试 mock的情况下不会启动spring容器，所以要把除测试对象外的其他引用方法全部mock掉，否则会报空指针的异常 */
@RunWith(MockitoJUnitRunner.class)
class UserTestTestControllerTest {

    // 需要mock的对象用Mock注解
    @Mock private UserTestService userTestService;
    // 实际注入的对象用InjectMocks注解
    @InjectMocks private UserTestController userTestController;

    private MockMvc mockMvc;

    // 初始化步骤
    @BeforeEach
    public void initMock() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userTestController).build();
    }

    @Test
    void testGet() throws Exception {
        // 第一步，mock掉在该controller方法中使用的所有service层的方法
        when(userTestService.get(
                        anyInt(),
                        anyInt(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()))
                .thenReturn(null);

        // 模拟接口调用
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/usertest")
                                .contentType("application/json;charset=UTF-8")
                                .accept("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.code").value(ResultCode.SUCCESS.code()))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testAdd() throws Exception {
        doNothing().when(userTestService).add(any());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/usertest")
                                .contentType("application/json;charset=UTF-8")
                                .content("{\"uuid\":\"1\"}")
                                .accept("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.code").value(ResultCode.SUCCESS.code()))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testUpdate() throws Exception {
        doNothing().when(userTestService).update(any());

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/usertest/1")
                                .contentType("application/json;charset=UTF-8")
                                .content("{\"uuid\":\"1\"}")
                                .accept("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.code").value(ResultCode.SUCCESS.code()))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(userTestService).delete(anyString());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/usertest/1")
                                .contentType("application/json;charset=UTF-8")
                                .content("{\"uuid\":\"1\"}")
                                .accept("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.code").value(ResultCode.SUCCESS.code()))
                .andDo(print())
                .andReturn();
    }
}
