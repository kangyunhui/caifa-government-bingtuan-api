package com.junyi.baseapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ${package.Entity}.${entity};

public interface ${entity}Service extends IService<${entity}>{

    IPage<${entity}> get(int page, int size, String sort, String order);

    void update(${entity} role);

    void delete(String uuid);
}