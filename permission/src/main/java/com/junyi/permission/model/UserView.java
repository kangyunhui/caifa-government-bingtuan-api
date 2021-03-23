package com.junyi.permission.model;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user")
public class UserView {

    @TableId(value = "guid")
    private String guid;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

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
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    @TableField(
            value = "update_time",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER)
    private Date updateTime;
}
