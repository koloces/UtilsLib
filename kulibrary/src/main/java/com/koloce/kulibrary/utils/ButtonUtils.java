package com.koloce.kulibrary.utils;

/**
 * 作者：hgs
 * 时间：2017/12/26:17:24
 * 描述：点击事件的帮助类
 */

public class ButtonUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static final int CARD_MIN_CLICK_DELAY_TIME = 1500;
    private static long lastClickTime;

    /**
     * 是否是快速点击(点击间隔1秒)
     * @return true 是快速点击（点击间隔小于1s）
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isCardClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= CARD_MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean canClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastClickTime < 500) {
            return false;
        }
        lastClickTime = curTime;
        return true;
    }

}
