package com.junyi.baseapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.junyi.baseapi.pojo.postgres.UserTest;

/** 此类只是一个样例，并非真实业务 */
public interface UserTestService {

    IPage<UserTest> get(
            int page,
            int size,
            String sort,
            String order,
            String name,
            String startDate,
            String endDate);

    void add(UserTest userTest);

    void update(UserTest userTest);

    void delete(String id);
}
