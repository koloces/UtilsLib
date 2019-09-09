package com.koloce.kulibrary.utils.http.exception;

import com.google.gson.Gson;
import com.koloce.kulibrary.utils.http.been.BaseResponseBean;

/**
 * https://github.com/jeasonlzy
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
