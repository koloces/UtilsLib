package com.koloce.kulibrary.utils.http.callbck;
import com.koloce.kulibrary.utils.http.been.ResponseBean;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Created by koloces on 2019/4/29
 */
public abstract class ListCallback<T> implements MyCallBack<T> {
    private JsonCallback callback;

    public ListCallback() {
        callback = new JsonCallback<ResponseBean<List<T>>>() {
            @Override
            public void onResult(ResponseBean<List<T>> result) {
                ListCallback.this.onResult(result.data);
            }

            @Override
            public void onError(Response<ResponseBean<List<T>>> response) {
                super.onError(response);
                ListCallback.this.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ListCallback.this.onFinish();
            }
        };
    }

    public JsonCallback getCallBack() {
        return callback;
    }

    protected abstract void onResult(List<T> result);

    protected void onError(Response<ResponseBean<List<T>>> response) {
    }

    protected void onFinish() {
    }
}
