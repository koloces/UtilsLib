package com.koloce.kulibrary.utils.http;


import android.util.ArrayMap;

import com.koloce.kulibrary.base.BaseApp;
import com.koloce.kulibrary.utils.LogUtils;
import com.koloce.kulibrary.utils.http.callbck.Convert;
import com.koloce.kulibrary.utils.http.callbck.MyCallBack;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 网络框架二次封装
 */

public class OkUtil {

    private static ArrayMap<String, String> publicParams;


    public static void setPublicParams(ArrayMap<String, String> params) {
        publicParams = params;
    }

    /**
     * get 请求
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void get(String url, ArrayMap<String, String> map, AbsCallback<T> callback) {
        get(url, null, map, callback);
    }

    public static <T> void get(String url, ArrayMap<String, String> map, MyCallBack<T> callback) {
        get(url, null, map, callback.getCallBack());
    }

    /**
     * get 请求
     *
     * @param url
     * @param header
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void get(String url, ArrayMap<String, String> header, ArrayMap<String, String> map, AbsCallback<T> callback) {
        if (!NetWorkStatesUtils.isNetworkAvalible(BaseApp.getContext())) {
//            Toast.makeText(BaseApp.getContext(), BaseApp.getContext().getResources().getString(com.utils.utils.R.string.isHasNet), Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtils.e("GET", url);
        if (map == null)
            map = new ArrayMap<>();
        GetRequest<T> request = OkGo.<T>get(url)
                .tag(url)
                .params(map);
        if (header != null) {
            request.headers(createHeaders(header));
        }
        request.execute(callback);
    }

    /**
     * post 请求
     *
     * @param url
     * @param header
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void post(String url, ArrayMap<String, String> header, ArrayMap<String, String> map, AbsCallback<T> callback) {
        if (!NetWorkStatesUtils.isNetworkAvalible(BaseApp.getContext())) {
//            Toast.makeText(BaseApp.getContext(), BaseApp.getContext().getResources().getString(com.utils.utils.R.string.isHasNet), Toast.LENGTH_SHORT).show();
            return;
        }
        if (map == null)
            map = new ArrayMap<>();

        PostRequest<T> request = OkGo.<T>post(url)
                .tag(url)
                .params(BaseApp.getContext().addPublicParams(map));
        if (header != null) {
            request.headers(createHeaders(header));
        }
        request.execute(callback);
    }

    /**
     * post 请求
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void post(String url, ArrayMap<String, String> map, AbsCallback<T> callback) {
        post(url, null, map, callback);
    }

    public static <T> void post(String url, ArrayMap<String, String> map, MyCallBack<T> callback) {
        post(url, null, map, callback.getCallBack());
    }

    /**
     * post Json格式
     *
     * @param url
     * @param header
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void postJsonObj(String url, ArrayMap<String, String> header, ArrayMap<String, Object> map, AbsCallback<T> callback) {
        if (!NetWorkStatesUtils.isNetworkAvalible(BaseApp.getContext())) {
//            Toast.makeText(BaseApp.getContext(), BaseApp.getContext().getResources().getString(com.utils.utils.R.string.isHasNet), Toast.LENGTH_SHORT).show();
            return;
        }

        if (map == null)
            map = new ArrayMap<>();

        PostRequest<T> request = OkGo.<T>post(url)
                .tag(url);
        if (header != null) {
            request.headers(createHeaders(header));
        }
        request.upJson(Convert.toJson(BaseApp.getContext().addPublicObjectParams(map))).execute(callback);
    }

    /**
     * post Json格式
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void postJsonObj(String url, ArrayMap<String, Object> map, AbsCallback<T> callback) {
        postJsonObj(url, null, map, callback);
    }

    public static <T> void postJsonObj(String url, ArrayMap<String, Object> map, MyCallBack<T> callback) {
        postJsonObj(url, null, map, callback.getCallBack());
    }

    /**
     * post Json格式
     *
     * @param url
     * @param header
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void postJsonStr(String url, ArrayMap<String, String> header, ArrayMap<String, String> map, AbsCallback<T> callback) {
        if (!NetWorkStatesUtils.isNetworkAvalible(BaseApp.getContext())) {
//            Toast.makeText(BaseApp.getContext(), BaseApp.getContext().getResources().getString(com.utils.utils.R.string.isHasNet), Toast.LENGTH_SHORT).show();
            return;
        }

        if (map == null)
            map = new ArrayMap<>();

        PostRequest<T> request = OkGo.<T>post(url)
                .tag(url);
        if (header != null) {
            request.headers(createHeaders(header));
        }
        request.upJson(Convert.toJson(BaseApp.getContext().addPublicParams(map))).execute(callback);
    }

    /**
     * post Json格式
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     */
    public static <T> void postJsonStr(String url, ArrayMap<String, String> map, AbsCallback<T> callback) {
        postJsonStr(url, null, map, callback);
    }

    public static <T> void postJsonStr(String url, ArrayMap<String, String> map, MyCallBack<T> callback) {
        postJsonStr(url, null, map, callback.getCallBack());
    }

    /**
     * header不支持中文
     *
     * @param headers
     */
    private static HttpHeaders createHeaders(ArrayMap<String, String> headers) {
        HttpHeaders header = new HttpHeaders();
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                header.put(next.getKey(), next.getValue());
            }
        }
        return header;
    }

}
