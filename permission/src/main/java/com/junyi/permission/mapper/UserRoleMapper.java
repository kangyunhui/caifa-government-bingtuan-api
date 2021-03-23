package com.junyi.permission.mapper;

import com.junyi.permission.entity.Role;
import com.junyi.permission.entity.UserRole;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleMapper extends MyBaseMapper<UserRole> {

    @Select(
            "SELECT\n"
                    + "\tb.*\n"
                    + "FROM\n"
                    + "\tt_user_role a\n"
                    + "\tINNER JOIN t_role b ON a.role_id = b.guid  where a.user_id= #{userId}")
    List<Role> selectUserRole(String userId);
}
