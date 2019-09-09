package com.koloce.utilslib;

import android.view.View;

import com.koloce.kulibrary.base.UIActivity;
import com.koloce.kulibrary.utils.LogUtils;
import com.koloce.kulibrary.utils.http.encryption.HttpEncryptionUtils;


/**
 * Created by koloces on 2019/9/7
 */
public class TestActivity extends UIActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.k_empty_layout;
    }

    @Override
    protected void initView() {
        String random = HttpEncryptionUtils.getRandomStr();
        LogUtils.e(random);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void afterInitView() {

    }

    @Override
    protected void initBeforeSetContentView() {

    }

    @Override
    protected void initBeforeInitView() {

    }

    public void clickView(View view) {
    }
}
