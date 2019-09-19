package com.koloce.kulibrary.utils.http.been;

import java.io.Serializable;

/**
 * Created by koloces on 2019/5/8
 */
public class BaseResponseBean implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int result = -1;
    public String msg;

    public ResponseBean toResponseBean() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.code = result;
        responseBean.msg = msg;
        return responseBean;
    }
}
