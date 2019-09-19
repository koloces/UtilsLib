package com.koloce.kulibrary.utils.http.been;

import java.io.Serializable;

/**
 * Created by koloces on 2019/5/8
 */

public class ResponseBean<T> implements Serializable {

    public int code;
    public String msg;
    public T data;
}