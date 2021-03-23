package com.junyi.permission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

/** 页面信息 */
@Data
@ToString
@TableName("s_view")
public class View {

    @TableId("guid")
    private String guid;

    /** 页面名称 */
    @TableField("name")
    private String name;

    /** 页面 */
    @TableField("path")
    private String path;

    /** 图标 */
    @TableField("icon")
    private String icon;

    /** 选中的图标 */
    @TableField("active_icon")
    private String activeIcon;

    /** 所属模块 */
    @TableField("parent_id")
    private String parentId;

    /** 排序 */
    @TableField("index")
    private Integer index;
}
