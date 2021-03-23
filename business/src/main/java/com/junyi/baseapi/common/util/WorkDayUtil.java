package com.junyi.baseapi.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangmingyue
 * @description
 */
public class WorkDayUtil {

    public static Date calculate(Date date, int days) {
        String week;
        int millisecond = 86400000; // 一天的毫秒值
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        for (int i = 1; i <= days; i++) {
            date = new Date(date.getTime() + millisecond);
            week = formatter.format(date);
            if (week.contains("六") || week.contains("日")) i--;
        }
        return date;
    }
}
