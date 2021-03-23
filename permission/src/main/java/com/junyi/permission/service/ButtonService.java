package com.junyi.permission.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.junyi.permission.entity.Button;
import com.junyi.permission.model.Node;

import java.util.List;

public interface ButtonService extends IService<Button> {

    IPage<Button> get(int page, int size, String sort, String order);

    void update(Button role);

    void delete(String uuid);

    void saveButtonInterface(String btnId, String[] interfaceIds);

    List<Node> getGroupButton(String roleId);

    List<Button> getUserButton();

    void add(Button button);
}
