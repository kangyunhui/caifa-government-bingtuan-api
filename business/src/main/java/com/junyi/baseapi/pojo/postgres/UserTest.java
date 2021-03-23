package com.junyi.baseapi.pojo.postgres;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zhangxianshuai
 * @description 用户信息
 */
@Data
@NoArgsConstructor
@TableName(value = "t_user")
public class UserTest {

    @TableId("uuid")
    private String uuid;

    @TableField("type")
    private String type;

    @TableField("user_name")
    private String userName;

    @TableField("real_name")
    private String realName;

    @TableField("mobile_number")
    private String mobileNumber;

    @TableField("creation_time")
    private Date creationTime;

    @TableField("last_login_time")
    private Date lastLoginTime;

    @TableField("company_name")
    private String companyName;

    @TableField("xz_code")
    private String xzCode;

    @TableField("status")
    private String status;

    @TableField("password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
