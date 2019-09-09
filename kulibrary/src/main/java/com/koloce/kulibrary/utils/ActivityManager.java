package com.koloce.kulibrary.utils;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created on 2019/4/7
 */
public class ActivityManager {
    private static ActivityManager instance;
    private Stack<Activity> activityStack;// activity栈

    private ActivityManager() {
        activityStack = new Stack<>();
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取栈顶的activity，先进后出原则
     * @return
     */
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * 移除一个activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 移除一个activity
     * @param clz
     */
    public void finishActivity(Class clz) {
        if (activityStack != null && activityStack.size() > 0) {
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                Activity activity = activityStack.get(i);
                if (clz.getName().equals(activity.getClass().getName())) {
                    activity.finish();
                    activityStack.remove(activity);
                    activity = null;
                    return;
                }
            }
        }
    }

    /**
     * 移除一个activity
     * @param clz
     */
    public void finishActivity(Class... clz) {
        for (Class aClass : clz) {
            finishActivity(aClass);
        }
    }

    /**
     * finish指定的activity之上所有的activity
     *
     * @param actCls
     * @param isIncludeSelf 是否包含自己(自己是只当前界面,而不是指定的activity)
     * @return
     */
    public boolean finishToActivity(Class<? extends Activity> actCls, boolean isIncludeSelf) {
        List<Activity> buf = new ArrayList<Activity>();
        int size = activityStack.size();
        Activity activity = null;
        for (int i = size - 1; i >= 0; i--) {
            activity = activityStack.get(i);
            if (activity.getClass().isAssignableFrom(actCls)) {
                for (Activity a : buf) {
                    a.finish();
                }
                return true;
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity);
            } else if (i != size - 1) {
                buf.add(activity);
            }
        }
        return false;
    }

    public void finishAllActivity(){
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * activity是否存在(目标页面是否打开)
     * @param cls
     * @return true 存在 false 不存在
     */
    public boolean activityExists(Class cls){
        for (Activity activity : activityStack) {
            if (activity.getClass().getSimpleName().equals(cls.getSimpleName())){
                return true;
            }
        }
        return false;
    }

    public void finishAllActivity(Class cls){
        ArrayList<Activity> closeActivity = new ArrayList<>();

        for (Activity activity : activityStack) {
            if (!activity.getClass().getSimpleName().equals(cls.getSimpleName())){
                closeActivity.add(activity);
            }
        }

        for (int i = 0,len = closeActivity.size(); i < len; i++) {
            Activity activity = closeActivity.get(i);
            activity.finish();
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e){
            System.exit(0);
        }
    }
}
