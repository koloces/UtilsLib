package com.koloce.kulibrary.listener;

import android.view.View;

/**
 * Created by koloces on 2019/4/12
 * 防止多次连点的ClickListener
 */
public abstract class NotFastClickListener implements View.OnClickListener {
    private long lastClickTime;

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastClickTime > 300){
            onNotFastClick(v);
        }
        lastClickTime = System.currentTimeMillis();
    }

    public abstract void onNotFastClick(View view);
}
