package com.koloce.kulibrary.listener;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by koloces on 2019/4/16
 */
public interface OnOpenAlbumResultListener {
    void onResult(int requestCode, List<LocalMedia> result);
}
