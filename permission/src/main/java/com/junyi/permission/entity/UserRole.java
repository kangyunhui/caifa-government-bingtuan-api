package com.junyi.permission.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/** 用户角色关系 */
@Data
@ToString
@TableName("t_user_role")
public class UserRole {

    @TableId("guid")
    private String guid;

    /** 用户名 */
    @TableField("user_id")
    private String userId;

    /** 角色编号 */
    @TableField("role_id")
    private String roleId;

    @TableField(
            value = "create_time",
            fill = FieldFill.INSERT,
            insertStrategy = FieldStrategy.NOT_NULL,
            updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    @TableField(
            value = "update_time",
            fill = FieldFill.INSERT_UPDATE,
            insertStrategy = FieldStrategy.NOT_NULL,
            updateStrategy = FieldStrategy.NOT_NULL)
    private Date updateTime;
}
