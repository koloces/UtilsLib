package com.koloce.app;

import com.koloce.kulibrary.base.BaseApp;
import com.koloce.kulibrary.utils.http.been.BaseResponseBean;
import com.koloce.kulibrary.utils.http.exception.OnErrorListener;

/**
 * Created by koloces on 2019/9/18
 */
public class App extends BaseApp {
    @Override
    protected void init() {

    }

    @Override
    protected boolean isDebug() {
        return false;
    }

    @Override
    protected OnErrorListener getErrorListener() {
        return new OnErrorListener() {
            @Override
            public void onError(BaseResponseBean error) {

            }
        };
    }

    @Override
    protected int getSuccessCode() {
        return 1;
    }
}
