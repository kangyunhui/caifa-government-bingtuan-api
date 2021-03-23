package com.junyi.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.junyi.permission.entity.Interface;
import com.junyi.permission.model.Node;

import java.util.*;

public interface InterfaceService extends IService<Interface> {

    List<Node> getGroupInterface(String buttonId);

    void loadUserInterface();

    void loadUserInterface(String userId);

    void loadNoAuthorisedInterface();

    boolean verifyPermission(String token, String requestUrl);

    boolean matchPublicInterface(String url);

    void loadPublicInterface();

    List<Interface> get();

    List<String> getCheckedInterface(String buttonId);
}
