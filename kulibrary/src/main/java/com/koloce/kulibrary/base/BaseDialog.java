package com.koloce.kulibrary.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.koloce.kulibrary.R;

import java.util.List;

/**
 * Created on 2019/4/10
 */
public abstract class BaseDialog extends Dialog implements DialogInterface.OnKeyListener {



    protected Context mContent;
    protected WindowManager windowManager;
    private GravityType type = GravityType.MIDDLE;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.BaseDialogTheme);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initDialog(context);
    }

    protected void setGravityType(GravityType type){
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout());
        findView();
        setListener();
        initView();
    }

    protected abstract int getLayout();

    protected abstract void findView();

    protected abstract void setListener();

    protected abstract void initView();

    /**
     * 初始化
     */
    private void initDialog(Context context) {
        mContent = context;
        //获取手机窗口的管理器
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //取消掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //整个界面的布局
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //默认窗口能关闭
        setCanClose(true);
    }

    @Override
    public void show() {
        super.show();
        if (mContent == null)return;
        Window window = getWindow();

        //获取窗体的宽高,这里主要是为了方式对话框超过手机的状态栏，对齐顶部的位置进行设置
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        if (rect.top == 0) {
            rect.top = getContext().getResources().getDimensionPixelSize(getContext().getResources().getIdentifier("status_bar_height", "dimen", "android"));
        }
        //设置动画
        //window.setWindowAnimations();

        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(0));

        //设置全屏
        //让该window后所有的东西都成暗淡（dim）dimAmount设置变暗程度
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.3f;
        layoutParams.horizontalMargin = 0;
        if (type == GravityType.TOP){
            layoutParams.gravity = Gravity.TOP;
        } else if (type == GravityType.MIDDLE) {
            layoutParams.gravity = Gravity.CENTER;
        } else if (type == GravityType.BOTTOM){
            layoutParams.gravity = Gravity.BOTTOM;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = displayMetrics.widthPixels;
        //如果还想修改布局可以在这个方法里进行
//        onSetLayoutParam(layoutParams, rect);
        window.setAttributes(layoutParams);

    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        //event.getRepeatCount()方法是点击返回键多次  返回back键进行屏蔽
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        }
        return false;
    }

    /**
     * 是否可以点击外部取消
     */
    protected void setCanClose(boolean canClose) {
        if (canClose) {
            setCanceledOnTouchOutside(true);
            this.setOnKeyListener(null);
        } else {
            setCanceledOnTouchOutside(false);
            this.setOnKeyListener(this);
        }
    }

    protected enum GravityType{
        /**
         * 上
         */
        TOP,
        /**
         * 中
         */
        MIDDLE,
        /**
         * 下
         */
        BOTTOM
    }
}
