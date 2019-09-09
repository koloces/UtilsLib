package com.koloce.kulibrary.utils;

import android.util.Log;


/**
 * Created on 2019/3/30
 */
public class LogUtils {
    private static final String TAG = "LOG_UTILS";

    private static final int LOG_LEVER_0 = 0;
    private static final int LOG_LEVER_V = 1;
    private static final int LOG_LEVER_D = 2;
    private static final int LOG_LEVER_I = 3;
    private static final int LOG_LEVER_W = 4;
    private static final int LOG_LEVER_E = 5;

    private static final int LOG_LEVER = LOG_LEVER_E;

    public static void e(String str) {
        e(TAG, str);
    }

    public static void e(Class clz, String str) {
        e(clz.getSimpleName(), str);
    }

    public static void e(String tag, String str) {
        if (StringUtil.isEmpty(str))return;

        if (LOG_LEVER >= LOG_LEVER_E)
            Log.e(tag, str);
    }

    public static void v(String tag, String str) {
        if (StringUtil.isEmpty(str))return;
        if (LOG_LEVER >= LOG_LEVER_V)
            Log.v(tag, str);
    }

    public static void v(String str){
        v(TAG,str);
    }

    public static void v(Class clz, String str){
        v(clz.getSimpleName(),str);
    }

    public static void i(String tag, String str) {
        if (StringUtil.isEmpty(str))return;
        if (LOG_LEVER >= LOG_LEVER_D)
            Log.i(tag, str);
    }

    public static void d(String tag, String str) {
        if (StringUtil.isEmpty(str))return;
        if (LOG_LEVER >= LOG_LEVER_I)
            Log.d(tag, str);
    }

    public static void w(String tag, String str) {
        if (StringUtil.isEmpty(str))return;
        if (LOG_LEVER >= LOG_LEVER_W)
            Log.w(tag, str);
    }
}
