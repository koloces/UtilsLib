package com.koloce.kulibrary.utils.http.exception;


import com.koloce.kulibrary.utils.http.been.BaseResponseBean;

/**
 * Created by koloces on 2019/5/8
 */
public interface OnErrorListener {
    void onError(BaseResponseBean error);
}
