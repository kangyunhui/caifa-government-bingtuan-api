package com.junyi.baseapi.service.impl;

import com.junyi.baseapi.service.AsyncTaskService;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {

    private static final Executor EXECUTOR =
            new ThreadPoolExecutor(
                    0,
                    Math.max(Runtime.getRuntime().availableProcessors(), 10),
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>());

    /**
     * 执行一个任务，该任务没有参数，也没有返回值
     *
     * @param runnable 需要执行的任务
     * @param exceptionHandle 异常处理函数,如果没有发生异常，该函数不会调用
     */
    @Override
    public CompletableFuture<Void> execute(
            Runnable runnable, Consumer<? super Throwable> exceptionHandle) {
        return CompletableFuture.runAsync(runnable, EXECUTOR)
                .whenComplete(
                        (t, e) -> {
                            if (e != null) {
                                exceptionHandle.accept(e);
                            }
                        });
    }

    /**
     * 执行一个任务，该任务有参数，没有返回值
     *
     * @param consumer 需要执行的任务
     * @param param 参数
     * @param exceptionHandle 异常处理函数，如果没有发生异常，该函数不会调用
     * @param <T> 参数类型
     */
    @Override
    public <T> CompletableFuture<Void> execute(
            Consumer<T> consumer, T param, Consumer<? super Throwable> exceptionHandle) {
        return CompletableFuture.runAsync(() -> consumer.accept(param), EXECUTOR)
                .whenComplete(
                        (result, exception) -> {
                            if (exception != null) {
                                exceptionHandle.accept(exception);
                            }
                        });
    }

    /**
     * 执行一个任务，该任务没有参数，有返回值
     *
     * @param supplier 需要执行的任务
     * @param resultHandle 结果处理函数
     * @param exceptionHandle 异常处理函数，如果没有发生异常，该函数不会调用
     * @param <R> 返回值类型
     */
    @Override
    public <R> CompletableFuture<R> execute(
            Supplier<R> supplier,
            Consumer<R> resultHandle,
            Consumer<? super Throwable> exceptionHandle) {
        return CompletableFuture.supplyAsync(supplier, EXECUTOR)
                .whenComplete(
                        (result, exception) -> {
                            if (exception != null) {
                                exceptionHandle.accept(exception);
                            } else {
                                resultHandle.accept(result);
                            }
                        });
    }

    /**
     * 执行一个任务，该任务有参数，有返回值
     *
     * @param function 执行的任务
     * @param param 参数
     * @param resultHandle 结果处理函数
     * @param exceptionHandle 异常处理函数，如果没有发生异常，该函数不会调用
     * @param <T> 参数类型
     * @param <R> 返回值类型
     */
    @Override
    public <T, R> CompletableFuture<R> execute(
            Function<T, R> function,
            T param,
            Consumer<R> resultHandle,
            Consumer<? super Throwable> exceptionHandle) {
        return CompletableFuture.supplyAsync(() -> function.apply(param), EXECUTOR)
                .whenComplete(
                        (result, exception) -> {
                            if (exception != null) {
                                exceptionHandle.accept(exception);
                            } else {
                                resultHandle.accept(result);
                            }
                        });
    }
}
