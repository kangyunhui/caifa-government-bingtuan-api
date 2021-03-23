package com.junyi.permission.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.junyi.permission.entity.Role;
import com.junyi.permission.model.Node;

import java.util.List;

public interface RoleService {
    IPage<Role> get(
            int page,
            int size,
            String sort,
            String order,
            String name,
            String startDate,
            String endDate);

    Role add(Role role);

    void update(Role role);

    void delete(String id);

    void delete(String[] ids);

    List<Node> getChecks(String userId);

    void saveRoleButton(String roleId, String[] buttonIds);

    List<Role> getRoleList(String userId);

    void bindUser(String roleId, String[] userIds);

    void bindUser(String[] roleIds, String userId);

    void unbindUser(String roleId, String[] userIds);

    Role getById(String uuid);
}
