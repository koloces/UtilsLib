package com.koloce.app;

import android.view.View;

import com.koloce.kulibrary.base.UIActivity;
import com.koloce.kulibrary.listener.OnOpenAlbumResultListener;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;


public class MainActivity extends UIActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
        findViewById(R.id.Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum(1, null, 101, new OnOpenAlbumResultListener() {
                    @Override
                    public void onResult(int requestCode, List<LocalMedia> result) {

                    }
                });
            }
        });
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
}
