package com.koloce.kulibrary.utils.http.exception;

import com.google.gson.Gson;
import com.koloce.kulibrary.utils.http.been.BaseResponseBean;

/**
 * Created by koloces on 2019/5/8
 */

public class MyException extends IllegalStateException {

    private BaseResponseBean errorBean;

    public MyException(String s) {
        super(s);
        errorBean = new Gson().fromJson(s, BaseResponseBean.class);
    }

    public BaseResponseBean getErrorBean() {
        return errorBean;
    }
}
