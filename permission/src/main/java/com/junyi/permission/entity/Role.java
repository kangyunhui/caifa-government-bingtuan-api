package com.junyi.permission.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/** 角色 */
@Data
@ToString
@TableName("t_role")
public class Role {

    @TableId("guid")
    private String guid;

    /** 角色名称 */
    @TableField("name")
    private String name;

    /** 角色描述 */
    @TableField("description")
    private String description;

    /** 创建者 */
    @TableField("create_user")
    private String createUser;

    /** 修改者 */
    @TableField("update_user")
    private String updateUser;

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
