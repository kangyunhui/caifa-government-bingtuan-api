package com.junyi.baseapi.service;

import com.junyi.baseapi.service.impl.AsyncTaskServiceImpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
class AsyncTaskServiceTest {

    // 实际注入的对象用InjectMocks注解
    @InjectMocks private AsyncTaskServiceImpl asyncTaskService;

    // 初始化步骤
    @BeforeEach
    public void initMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test() {
        System.out.println(Thread.currentThread());
        // 无参数无返回值-无异常
        asyncTaskService.execute(
                () -> {
                    System.out.println("hello");
                    System.out.println(Thread.currentThread());
                },
                exception -> System.out.println(exception.getMessage()));
        // 无参数无返回值-有异常
        asyncTaskService.execute(
                () -> {
                    System.out.println(Thread.currentThread());
                    throw new RuntimeException("exception");
                },
                exception -> System.out.println(exception.getMessage()));

        // 有参数无返回值-无异常
        asyncTaskService.execute(
                param -> {
                    System.out.println(param);
                    System.out.println(Thread.currentThread());
                },
                "hello",
                exception -> System.out.println(exception.getMessage()));
        // 有参数无返回值-有异常
        asyncTaskService.execute(
                param -> {
                    System.out.println(Thread.currentThread());
                    throw new RuntimeException("exception");
                },
                "hello",
                exception -> System.out.println(exception.getMessage()));

        // 无参数，有返回值-无异常
        asyncTaskService.execute(
                () -> {
                    System.out.println("hello");
                    System.out.println(Thread.currentThread());
                    return "world";
                },
                result -> {
                    System.out.println(result);
                },
                exception -> System.out.println(exception.getMessage()));
        // 无参数，有返回值-有异常
        asyncTaskService.execute(
                () -> {
                    System.out.println(Thread.currentThread());
                    throw new RuntimeException("exception");
                },
                result -> {
                    System.out.println(result);
                },
                exception -> System.out.println(exception.getMessage()));

        // 有参数，有返回值-无异常
        asyncTaskService.execute(
                a -> {
                    System.out.println(a);
                    System.out.println(Thread.currentThread());
                    return "world";
                },
                "hello",
                result -> {
                    System.out.println(result);
                },
                exception -> System.out.println(exception.getMessage()));
        // 有参数，有返回值-有异常
        asyncTaskService.execute(
                a -> {
                    System.out.println(Thread.currentThread());
                    throw new RuntimeException("exception");
                },
                "hello",
                result -> {
                    System.out.println(result);
                },
                exception -> System.out.println(exception.getMessage()));

        batchTest();
    }

    private void batchTest() {
        // 线程安全集合，用来保存结果
        Map<Integer, String> result = new ConcurrentHashMap<>();
        // 初始化总数据
        List<DemoData> dataList = getDemoData();
        // 异步任务结果处理集合
        List<CompletableFuture> futureList = new ArrayList<>(100);
        // 分批处理，每次处理100条
        for (int i = 0; i < dataList.size(); i += 100) {
            futureList.add(
                    asyncTaskService.execute(
                            this::filterData,
                            dataList.subList(i, i + 100),
                            errorData -> {
                                handleResult(errorData, result);
                            },
                            exception -> {
                                System.out.println(exception.getMessage());
                            }));
        }
        // 等待所有任务完成
        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("错误数据数:" + result.size());
    }

    /**
     * 过滤数据的方法
     *
     * @param demoDataList
     * @return
     */
    private List<ErrorData> filterData(List<DemoData> demoDataList) {
        List<ErrorData> errorDataList = new ArrayList<>();
        for (int i = 0; i < demoDataList.size(); i++) {
            String error = validData(demoDataList.get(i));
            if (!StringUtils.isEmpty(error)) {
                errorDataList.add(new ErrorData(demoDataList.get(i).getIndex(), error));
            }
        }
        return errorDataList;
    }

    /**
     * 校验数据的方法
     *
     * @param demoData
     * @return
     */
    private static String validData(DemoData demoData) {
        if (demoData.getIndex() % 3 == 0) {
            return "error data";
        }
        return null;
    }

    /**
     * 处理过滤结果
     *
     * @param errorDataList
     * @param result
     */
    private static void handleResult(List<ErrorData> errorDataList, Map<Integer, String> result) {
        result.putAll(
                errorDataList.stream()
                        .collect(Collectors.toMap(ErrorData::getIndex, ErrorData::getError)));
    }

    /**
     * 初始化数据
     *
     * @return
     */
    private List<DemoData> getDemoData() {
        List<DemoData> demoData = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            demoData.add(new DemoData(i, "demo" + i));
        }
        return demoData;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class DemoData {
        private int index;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ErrorData {
        private int index;
        private String error;
    }
}
