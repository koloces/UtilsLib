package com.koloce.kulibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created on 2019/1/3 19:49
 */
public class ScrollLayout extends ScrollView {

    private boolean canScroll = true;


    public ScrollLayout(@NonNull Context context) {
        super(context);
    }

    public ScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * 判断是否可以滑动
         */
        if (isCanScroll()) {
            //可以滑动
            return super.onInterceptTouchEvent(ev);
        } else {
            //不可以滑动
            return false;
        }
    }


    public boolean isCanScroll() {
        return canScroll;
    }

    public ScrollLayout setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
        return this;
    }
}
