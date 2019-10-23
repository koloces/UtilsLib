package com.koloce.kulibrary.base;


import android.app.Application;

import com.koloce.kulibrary.utils.LogUtils;
import com.koloce.kulibrary.utils.MobileInfoUtil;
import com.koloce.kulibrary.utils.city.CityDataManager;
import com.koloce.kulibrary.utils.http.exception.OnErrorListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created on 2019/3/30
 */
public abstract class BaseApp extends Application {
    private static BaseApp context;
    public static OnErrorListener errorListener;
    public static boolean isDebug = true;
    public static int SUCCESS_CODE = 1;

    public static BaseApp getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        errorListener = getErrorListener();
        isDebug = isDebug();
        SUCCESS_CODE = getSuccessCode();
        LogUtils.init(isDebug);
        closeAndroidPDialog();
        MobileInfoUtil.init(context);
        QMUISwipeBackActivityManager.init(this);
        CityDataManager.init();
        initOkGo();
        init();
    }

    protected abstract void init();
    protected abstract boolean isDebug();
    protected abstract OnErrorListener getErrorListener();
    protected abstract int getSuccessCode();

    /**
     * 初始化OkGo
     */
    private void initOkGo() {
        OkGo.getInstance().init(this);
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("Http");
            //log打印级别，决定了log显示的详细程度
            loggingInterceptor.setPrintLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            //log颜色级别，决定了log在控制台显示的颜色
            loggingInterceptor.setColorLevel(Level.SEVERE);
            builder.addInterceptor(loggingInterceptor);

            //全局的读取超时时间
            builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
            //全局的写入超时时间
            builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
            //全局的连接超时时间
            builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

            //使用sp保持cookie，如果cookie不过期，则一直有效
//            builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
            //使用数据库保持cookie，如果cookie不过期，则一直有效
//            builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
            //使用内存保持cookie，app退出后，cookie消失
            builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
            OkHttpClient build = builder.build();
            OkGo.getInstance().setOkHttpClient(build);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
