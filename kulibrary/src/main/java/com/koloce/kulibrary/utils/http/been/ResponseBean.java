package com.koloce.kulibrary.utils.http.been;

import java.io.Serializable;

/**
 * https://github.com/jeasonlzy
 */

public class ResponseBean<T> implements Serializable {

    public int code;
    public String msg;
    public T data;
}