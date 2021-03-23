package com.junyi.permission.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/** 接口信息 */
@Data
@ToString
@TableName("t_interface")
public class Interface {

    @TableId("guid")
    private String guid;

    /** 接口名称 */
    @TableField("name")
    private String name;

    /** 接口描述 */
    @TableField("notes")
    private String notes;

    /** 接口标签 */
    @TableField("tag")
    private String tag;

    /** 接口路径 */
    @TableField("url")
    private String url;

    /** 请求类型 */
    @TableField("method_type")
    private String methodType;

    @TableField(value = "is_public", jdbcType = JdbcType.BOOLEAN)
    private boolean publicInterface = false;

    @TableField(value = "is_authorized", jdbcType = JdbcType.BOOLEAN)
    private boolean authorized = true;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public boolean isSimilar(Interface other) {
        int otherHash =
                other.getName().hashCode()
                        + other.getTag().hashCode()
                        + other.getNotes().hashCode();
        int thisHash =
                this.getName().hashCode() + this.getTag().hashCode() + this.getNotes().hashCode();
        return otherHash == thisHash
                && this.authorized == other.authorized
                && this.publicInterface == other.publicInterface;
    }
}
