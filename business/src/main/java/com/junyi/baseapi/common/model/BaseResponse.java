package com.junyi.baseapi.common.model;

import lombok.Data;

/**
 * @program: BaseResponse
 * @author: shuangbin Zhao
 * @create: 2018-11-30 16:48
 * @description: 基本的返回类：包含公共信息
 */
@Data
public class BaseResponse {

    /*private String status;//返回状态 200 ：成功 202 ：失败

    private String errorMessage;//错误信息*/

    private String message; // 提示信息
}
