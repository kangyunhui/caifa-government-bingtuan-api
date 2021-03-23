package com.junyi.permission.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

/** 用户角色关系 */
@Data
@ToString
@TableName(resultMap = "userRole")
public class UserRoleResult {
    /** 用户名 */
    @TableField("user_id")
    private String userId;

    /** 角色编号 */
    @TableField("role_id")
    private String guid;

    /** 角色名称 */
    @TableField("role_name")
    private String name;

    /** 角色描述 */
    @TableField("role_description")
    private String description;
}
