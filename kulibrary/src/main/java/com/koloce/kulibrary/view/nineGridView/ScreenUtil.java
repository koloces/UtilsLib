package com.koloce.kulibrary.view.nineGridView;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

class ScreenUtil {

    private static int widthPixels;
    private static int heightPixels;

    public static int getScreenWidth(Context context){
        if (widthPixels == 0){
            widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        }
        return widthPixels;
    }

    public static int getScreenHeight(Context context){
        if (heightPixels == 0){
            heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        }
        return heightPixels;
    }

}
