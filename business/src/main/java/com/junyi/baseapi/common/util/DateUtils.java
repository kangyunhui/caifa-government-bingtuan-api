package com.junyi.baseapi.common.util;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lijy
 * @create 2018-12-14 16:14
 * @description 日期处理类
 */
@Slf4j
public class DateUtils {
    /*
     * 转换时间格式
     * return ：  Date 类型
     * 时间格式： yyyy-MM-dd HH:mm:ss
     * */
    public static Date formatDate(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(format.format(date));
    }

    /*
     * 获取当前时间
     * return ：  String 类型
     * 时间格式： yyyy-MM-dd HH:mm:ss
     * */
    public static String getDateTime() {
        Date time = new Date(System.currentTimeMillis());
        String dateTime = DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss");
        return dateTime;
    }
    /*
     * 获取当前时间
     * return ：  Date 类型
     * 时间格式： yyyy-MM-dd HH:mm:ss
     * */
    public static Date getDate() {
        Date time = new Date(System.currentTimeMillis());
        String dateTime = DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss");
        return getStringToDate(dateTime);
    }
    /*
     * Date 类型转 String
     * return ：  String 类型
     * 时间格式： yyyy-MM-dd HH:mm:ss
     * */
    public static String getDateToString(Date date) {
        if (date == null) {
            return null;
        }
        String dateTime = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        return dateTime;
    }
    /*
     *  String 类型转 Date
     * return ：  Date 类型
     * 时间格式： yyyy-MM-dd HH:mm:ss
     * */
    public static Date getStringToDate(String date) {
        SimpleDateFormat sdf;
        if (date.contains(":")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date dateTime = null;
        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            log.error(e.toString(), e);
        }
        return dateTime;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy/MM/dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
}
