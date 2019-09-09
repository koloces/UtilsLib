package com.koloce.kulibrary.utils;


import com.koloce.kulibrary.R;
import com.koloce.kulibrary.base.BaseApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 2019/4/7
 */
public class TimeUtils {
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间（固定格式yyyy-MM-dd）
     */
    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd");
    }

    /**
     * 获取当前时间
     * 样式自己写:yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return df.format(getCurrentTime());
    }

    /**
     * SimpleDateFormat
     *
     * @return
     */
    public static String getFormat(long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return df.format(time);
    }

    /**
     * SimpleDateFormat
     *
     * @return
     */
    public static String getFormat(String time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return df.format(Long.parseLong(time));
    }

    /**
     * SimpleDateFormat
     *
     * @return
     */
    public static String getFormat(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(Long.parseLong(time));
    }

    /**
     * SimpleDateFormat
     *
     * @return
     */
    public static String getFormat(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(time);
    }

    /**
     * 获取今天，昨天，前天，等等
     *
     * @param otherTime 时间戳
     * @return
     */
    public static String getTimeStr(String otherTime) {
        return getTimeStr(Long.parseLong(otherTime));
    }

    public static String getTimeStr(String otherTime, String format) {
        return getTimeStr(Long.parseLong(otherTime), format);
    }

    public static String getTimeStr(long otherTime) {
        return getTimeStr(otherTime, "yyyy-MM-dd HH:mm");
    }

    /**
     * 获取今天，昨天，前天，等等
     *
     * @param otherTime 时间戳
     * @return
     */
    public static String getTimeStr(long otherTime, String format) {
        if (String.valueOf(otherTime).length() < 13) {
            otherTime = otherTime * 1000;
        }

        long nowTime = getCurrentTime();
        long difference = nowTime - otherTime;
        if (difference < 1000 * 60 * 60) {//小于半小时
            int count = (int) (difference / (1000 * 60));
            if (count <= 0) {
                count = 1;
            }
            return String.valueOf(count) + BaseApp.getContext().getResources().getString(R.string.before_minute);
        }

        if (difference < 1000 * 60 * 60 * 24) {//一天之内
            int count = (int) (difference / (1000 * 60 * 60));
            if (count <= 0) {
                count = 1;
            }
            return String.valueOf(count) + BaseApp.getContext().getResources().getString(R.string.before_hours);
        }

        if (difference < 1000 * 60 * 60 * 24 * 2) {//两天之内
            return BaseApp.getContext().getResources().getString(R.string.yesterday);
        }

        if (difference < 1000 * 60 * 60 * 24 * 3) {//两天之内
            return BaseApp.getContext().getResources().getString(R.string.before_yesterday);
        }
        return getFormat(otherTime, format);
    }

    /*
     * 将时间转换为时间戳
     * "yyyy-MM-dd HH:mm:ss"
     */
    public static String dateToStamp(String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return String.valueOf(ts);
    }
}
