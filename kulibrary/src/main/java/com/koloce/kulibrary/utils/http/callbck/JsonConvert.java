package com.koloce.kulibrary.utils.http.callbck;



import com.google.gson.stream.JsonReader;
import com.koloce.kulibrary.base.BaseApp;
import com.koloce.kulibrary.utils.http.been.BaseResponseBean;
import com.koloce.kulibrary.utils.http.been.ResponseBean;
import com.koloce.kulibrary.utils.http.exception.MyException;
import com.lzy.okgo.convert.Converter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by koloces on 2019/5/8
 */
public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            T t = Convert.fromJson(jsonReader, rawType);
            response.close();
            if (t instanceof ResponseBean){
                if (((ResponseBean) t).code == 1){
                    return t;
                } else {
                    throw new MyException("{\"result\":"+((ResponseBean) t).code+",\"msg\":\""+((ResponseBean) t).msg+"\"}"); //直接抛自定义异常  会出现在 callback的onError中
                }
            } else {
                throw new MyException("{\"result\":"+ "-101" +",\"msg\":\""+ "解析错误" +"\"}"); //直接抛自定义异常  会出现在 callback的onError中
            }
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = Convert.fromJson(jsonReader, type);
        response.close();

        if (t instanceof ResponseBean){
            if (((ResponseBean) t).code == 1){
                return t;
            } else {
                throw new MyException("{\"result\":"+((ResponseBean) t).code+",\"msg\":\""+((ResponseBean) t).msg+"\"}"); //直接抛自定义异常  会出现在 callback的onError中
            }
        } else {
            throw new MyException("{\"result\":"+ "-101" +",\"msg\":\""+ "解析错误" +"\"}"); //直接抛自定义异常  会出现在 callback的onError中
        }
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType != ResponseBean.class) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = Convert.fromJson(jsonReader, type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<ResponseBean<Void>>(this)
                BaseResponseBean baseResponseBean = Convert.fromJson(jsonReader, BaseResponseBean.class);
                response.close();
                //noinspection unchecked
                return (T) baseResponseBean.toResponseBean();
            } else {
                // 泛型格式如下： new JsonCallback<ResponseBean<内层JavaBean>>(this)
                ResponseBean responseBean = null;
                try {
                    responseBean = Convert.fromJson(jsonReader, type);
                } catch (Exception e){
                    responseBean = Convert.fromJson(jsonReader,ResponseBean.class);
                } finally {
                    response.close();
                    int code = -101;
                    String msg = "";
                    if (responseBean == null){
                        code = -101;
                        msg = "解析出错";
                    } else {
                        code = responseBean.code;
                        msg = responseBean.msg;
                    }
                    if (code == BaseApp.SUCCESS_CODE) { //约定 正确返回码
                        return (T) responseBean;
                    } else{
                        throw new MyException("{\"result\":"+code+",\"msg\":\""+msg+"\"}"); //直接抛自定义异常  会出现在 callback的onError中
                    }
                }
            }
        }
    }
}
