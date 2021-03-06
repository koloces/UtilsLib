package com.koloce.kulibrary.utils.http.callbck;

import android.util.Log;
import android.widget.Toast;

import com.koloce.kulibrary.R;
import com.koloce.kulibrary.base.BaseApp;
import com.koloce.kulibrary.utils.LogUtils;
import com.koloce.kulibrary.utils.StringUtil;
import com.koloce.kulibrary.utils.http.exception.MyException;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import okhttp3.Response;

/**
 * Created by koloces on 2019/5/8
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;
    private boolean isShowToast;

    public JsonCallback() {
        isShowToast = true;
    }
    public JsonCallback(boolean isShowToast) {
        this.isShowToast = isShowToast;
    }

    public JsonCallback(Type type) {
        this.type = type;
        isShowToast = true;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
        isShowToast = true;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
//        request.headers("header1", "HeaderValue1")
//                .params("params1", "ParamsValue1")
//                .params("token", "3215sdf13ad1f65asd4f3ads1f");
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }
        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        int code = response.code();
        if (code == 404) {
            LogUtils.e("404 当前链接不存在");
        }
        Throwable exception = response.getException();
        if (code == 500 || code == 503){
            toast("服务器繁忙,请稍后再试");
        } else if (exception instanceof SocketTimeoutException) {
            Log.d("JsonCallback", "请求超时");
            toast(BaseApp.getContext().getResources().getString(R.string.netError));
        } else if (exception instanceof SocketException) {
            Log.d("JsonCallback", "服务器异常");
        } else if (exception instanceof MyException) { //个人自定义 异常 根据后台 约定值判断异常雷系
//            toast(((MyException) response.getException()).getErrorBean().msg);
            if (BaseApp.errorListener != null){
                BaseApp.errorListener.onError(((MyException) response.getException()).getErrorBean());
            }
        }
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        onResult(response.body());
    }

    protected abstract void onResult(T result);

    private void toast(String str){
        if (isShowToast && !StringUtil.isEmpty(str)){
            Toast.makeText(BaseApp.getContext(), str, Toast.LENGTH_SHORT).show();
        }
    }
}
