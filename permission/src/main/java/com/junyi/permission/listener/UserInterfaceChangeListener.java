package com.junyi.permission.listener;

import com.junyi.permission.event.UserInterfaceChangeEvent;
import com.junyi.permission.service.InterfaceService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

/** 刷新用户接口权限的监听器 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInterfaceChangeListener {

    private final @NonNull InterfaceService interfaceService;

    @TransactionalEventListener
    public void onLoadUserInterface(UserInterfaceChangeEvent event) {
        if (StringUtils.isEmpty(event.getUserId())) {
            interfaceService.loadUserInterface();
        } else {
            interfaceService.loadUserInterface(event.getUserId());
        }
    }
}
