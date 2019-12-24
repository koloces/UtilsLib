package com.koloce.kulibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koloce.kulibrary.dialog.LoadingDialog;
import com.koloce.kulibrary.libbeen.StringKeyValue;
import com.koloce.kulibrary.listener.OnOpenAlbumResultListener;
import com.koloce.kulibrary.utils.LogUtils;
import com.koloce.kulibrary.utils.StringUtil;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;

import java.util.List;


/**
 * Created on 2019/3/30
 */
public abstract class UIFragment extends Fragment {
    private String TAG;
    protected View mView;
    private LoadingDialog loadingDialog;
    public Context mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName();
        mActivity = getActivity();
        mView = LayoutInflater.from(getActivity()).inflate(getLayoutId(), null);
        initBeforeInitView();
        initView(mView);
        afterInitView();
        initListener();
        return mView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View mView);

    protected abstract void initListener();

    protected abstract void afterInitView();

    protected abstract void initBeforeInitView();

    protected void toNextActivity(Class clz) {
        Intent intent = new Intent(getActivity(), clz);
        startActivity(intent);
    }

    protected void toNextActivity(Class clz, StringKeyValue... datas) {
        Intent intent = new Intent(getActivity(), clz);
        if (datas != null) {
            for (int i = 0, len = datas.length; i < len; i++) {
                intent.putExtra(datas[i].key, datas[i].value);
            }
        }
        startActivity(intent);
    }

    /**
     * 跳转
     *
     * @param clz
     * @param valueWithKey value就是key（把value当做key，同时value也是value）
     */
    protected void toNextActivity(Class clz, String... valueWithKey) {
        StringKeyValue[] datas = new StringKeyValue[valueWithKey.length];
        for (int i = 0, len = valueWithKey.length; i < len; i++) {
            datas[i] = new StringKeyValue(valueWithKey[i], valueWithKey[i]);
        }
        toNextActivity(clz, datas);
    }


    /**
     * @param str
     */
    protected void toast(String str) {
        if (StringUtil.isEmpty(str)) return;
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    /**
     * Log.e
     *
     * @param str
     */
    protected void logE(String str) {
        LogUtils.e(this.getClass(), str);
    }

    /**
     * 显示软键盘
     *
     * @param editText
     * @param delay    是否延时显示，如不需要写0
     */
    protected void showKeyboard(EditText editText, int delay) {
        QMUIKeyboardHelper.showKeyboard(editText, delay);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    protected void hideKeyboard(View view) {
        QMUIKeyboardHelper.hideKeyboard(view);
    }

    public void showLoading(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.show();
    }

    public void dissmissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void openAlbum(int maxSize, List<LocalMedia> selectImageList, int requestCode, OnOpenAlbumResultListener listener) {
        if (mActivity instanceof UIActivity) {
            ((UIActivity) mActivity).openAlbum(maxSize, selectImageList, requestCode, listener);
        }
    }

    public void openVideo(int maxSize, List<LocalMedia> selectImageList, int requestCode, OnOpenAlbumResultListener listener) {
        if (mActivity instanceof UIActivity) {
            ((UIActivity) mActivity).openVideo(maxSize, selectImageList, requestCode, listener);
        }
    }

    public void openVideo(int maxSize, boolean withCamera, List<LocalMedia> selectImageList, int requestCode, OnOpenAlbumResultListener listener) {
        if (mActivity instanceof UIActivity) {
            ((UIActivity) mActivity).openVideo(maxSize, withCamera, selectImageList, requestCode, listener);
        }
    }

    public void openCamera(int requestCode, OnOpenAlbumResultListener listener) {
        if (mActivity instanceof UIActivity) {
            ((UIActivity) mActivity).openCamera(requestCode,listener);
        }
    }
}
