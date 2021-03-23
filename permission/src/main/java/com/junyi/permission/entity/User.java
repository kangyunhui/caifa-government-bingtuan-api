package com.junyi.permission.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user")
public class User {

    @TableId("guid")
    private String guid;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @JsonIgnoreProperties
    @TableField("salt")
    private String salt;

    @JsonIgnoreProperties
    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("xzcode")
    private String xzcode;

    @TableField("is_enable")
    private Boolean isEnable;

    @TableField("level")
    private Integer level;

    @TableField("company")
    private String company;

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
