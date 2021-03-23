package com.junyi.baseapi.common.constant;

/**
 * @author zhangxianshuai
 * @description 公共常量类
 */
public class Constants {

    /*短信相关说明*/
    public static final Integer NEXT_EXECUTE_INTERVAL = 5; // 同一事件失败后下一次扫描周期
    public static final Integer TYPE_ACCEPTED = 1; // 受理事件
    public static final Integer TYPE_FINISHED = 2; // 办结事件
    public static final Integer STATUS_INIT = 0; // 初始化
    public static final Integer STATUS_SEND_SUCCESS = 1; // 短信发送成功
    public static final Integer STATUS_SEND_FAIL = 2; // 短信发送失败

    // 文件最大50M
    public static final int FILE_SIZE_MAX = 52428800;
    public static final int ROW_COUNT_MAX = 50;

    // sql关键字，用来防止sql注入
    public static final String SQL_KEY =
            "select,insert,delete,drop table,update,truncate,netlocalgroup,administrators,:,net"
                    + " user";

    // 分页默认参数
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_PAGE_SIZE = "15";
    public static final String SORT = "creation_time";
    public static final String ORDER = "desc";

    // 密码强度校验（8-20 位，字母、数字、字符）
    public static final String REGSTR =
            "^(?![A-z0-9]+$)(?![A-z\\W]+$)(?![0-9\\W]+$)[A-z0-9\\W]{8,20}$";
}
