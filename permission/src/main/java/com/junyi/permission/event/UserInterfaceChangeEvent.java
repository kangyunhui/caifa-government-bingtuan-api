package com.junyi.permission.event;

import org.springframework.context.ApplicationEvent;

/** 用来刷新用户接口权限的事件 */
public class UserInterfaceChangeEvent extends ApplicationEvent {
    private String userId;

    public UserInterfaceChangeEvent(Object source) {
        super(source);
    }

    public UserInterfaceChangeEvent(Object source, String userId) {
        super(source);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
