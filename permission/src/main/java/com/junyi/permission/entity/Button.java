package com.junyi.permission.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author generator
 * @since 2021-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_button")
@ApiModel(value = "Button对象", description = "")
public class Button {

    @TableId("guid")
    private String guid;

    @ApiModelProperty(value = "按钮名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "按钮描述")
    @TableField("label")
    private String label;

    @ApiModelProperty(value = "父按钮id")
    @TableField("parent_id")
    private String parentId;

    @ApiModelProperty(value = "排序")
    @TableField("index")
    private int index;

    @ApiModelProperty(value = "类型")
    @TableField("type")
    private int type;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
