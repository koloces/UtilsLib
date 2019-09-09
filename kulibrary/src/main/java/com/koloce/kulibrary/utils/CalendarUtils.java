package com.koloce.kulibrary.utils;

import java.util.Calendar;

/**
 * Created by koloces on 2019/5/29
 */
public class CalendarUtils {

    /**
     * 功能描述：返回年份
     *
     * @return 返回日份
     */
    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 因为 java 的month是从0开始到11
     * 按照习惯 我做了 +1 处理
     *
     * @return
     */
    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }


    /**
     * 功能描述：返回日期(每月几号)
     *
     * @return 返回日份
     */
    public static int getDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当月的天数
     * @param year
     * @param month
     * @return
     */
    public static int getMonthMaxDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
