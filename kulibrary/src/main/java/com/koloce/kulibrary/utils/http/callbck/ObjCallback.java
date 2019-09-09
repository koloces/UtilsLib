package com.koloce.kulibrary.utils.http.callbck;


import com.koloce.kulibrary.utils.http.been.ResponseBean;
import com.lzy.okgo.model.Response;

/**
 * Created by koloces on 2019/4/29
 */
public abstract class ObjCallback<T> implements MyCallBack<T> {
    private JsonCallback<ResponseBean<T>> callback;

    public ObjCallback(final Class cls) {
        callback = new JsonCallback<ResponseBean<T>>() {
            @Override
            public void onResult(ResponseBean<T> result) {
                ObjCallback.this.onResult(result.data);
            }

            @Override
            public ResponseBean<T> convertResponse(okhttp3.Response response) throws Throwable {
                ResponseBean<T> tResponseBean = super.convertResponse(response);
                if (tResponseBean.data != null) {
                    T t = (T) Convert.fromJson(Convert.toJson(tResponseBean.data), cls);
                    tResponseBean.data = t;
                }
                return tResponseBean;
            }

            @Override
            public void onError(Response<ResponseBean<T>> response) {
                super.onError(response);
                ResponseBean<T> body = response.body();
                if (body != null) {
                    ObjCallback.this.onError(response.body());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ObjCallback.this.onFinish();
            }
        };
    }

    public JsonCallback getCallBack() {
        return callback;
    }

    protected abstract void onResult(T result);

    protected void onError(ResponseBean<T> response) {
    }

    protected void onFinish() {
    }

}
