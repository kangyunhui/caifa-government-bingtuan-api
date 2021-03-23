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
@TableName("t_button_interface")
@ApiModel(value = "ButtonInterface对象", description = "")
public class ButtonInterface {

    @TableId("guid")
    private String guid;

    @ApiModelProperty(value = "按钮id")
    @TableField("button_id")
    private String buttonId;

    @ApiModelProperty(value = "接口id")
    @TableField("interface_id")
    private String interfaceId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
