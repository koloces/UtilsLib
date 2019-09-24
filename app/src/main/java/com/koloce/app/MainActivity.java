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
        findViewById(R.id.Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum(1, null, 101, new OnOpenAlbumResultListener() {
                    @Override
                    public void onResult(int requestCode, List<LocalMedia> result) {
                        if (result != null && result.size() > 0){
                            toast(result.get(0).getPath());
                        }
                    }
                });
            }
        });
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
}
