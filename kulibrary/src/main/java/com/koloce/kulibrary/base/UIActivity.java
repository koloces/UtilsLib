package com.koloce.kulibrary.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.koloce.kulibrary.dialog.LoadingDialog;
import com.koloce.kulibrary.libbeen.StringKeyValue;
import com.koloce.kulibrary.listener.OnOpenAlbumResultListener;
import com.koloce.kulibrary.utils.ActivityManager;
import com.koloce.kulibrary.utils.LogUtils;
import com.koloce.kulibrary.utils.PermissionsUtils;
import com.koloce.kulibrary.utils.StringUtil;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/3/30
 */
public abstract class UIActivity extends QMUIActivity {
    private String TAG;

    //创建监听权限的接口对象
    private PermissionsUtils.IPermissionsResult permissionsResult;
    public ViewGroup mView;
    private LoadingDialog loadingDialog;
    public UIActivity mActivity;
    private OnOpenAlbumResultListener openAlbumResultListener;

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mActivity = this;
        ActivityManager.getInstance().addActivity(this);

        mView = (ViewGroup) LayoutInflater.from(this).inflate(getLayoutId(), null);

        initBeforeSetContentView();
        setContentView(mView);
        initBeforeInitView();
        initView();
        afterInitView();
        initListener();
    }

    protected abstract void afterInitView();

    protected abstract void initBeforeSetContentView();

    protected abstract void initBeforeInitView();

    protected void toNextActivity(Class clz){
        Intent intent = new Intent(this,clz);
        startActivity(intent);
    }
    protected void toNextActivity(Class clz, StringKeyValue... datas){
        Intent intent = new Intent(this,clz);
        if (datas != null){
            for (int i = 0, len = datas.length; i < len; i++) {
                intent.putExtra(datas[i].key,datas[i].value);
            }
        }
        startActivity(intent);
    }

    /**
     * 跳转
     * @param clz
     * @param valueWithKey value就是key（把value当做key，同时value也是value）
     */
    protected void toNextActivity(Class clz, String... valueWithKey){
        StringKeyValue[] datas = new StringKeyValue[valueWithKey.length];
        for (int i = 0, len = valueWithKey.length; i < len; i++) {
            datas[i] = new StringKeyValue(valueWithKey[i],valueWithKey[i]);
        }
        toNextActivity(clz,datas);
    }

    protected void checkPermissions(PermissionsUtils.IPermissionsResult permissionsResult, String... permissions){
        if (permissions == null || permissions.length == 0){
            return;
        }

        //这里的this不是上下文，是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
    }

    /**
     * 是否允许左滑关闭页面 默认不可以
     * @return
     */
    @Override
    protected boolean canDragBack() {
        return false;
    }

    /**
     * 设置状态栏白色字体图标
     * @return
     */
    protected boolean setStatusTxtWhite(){
        return QMUIStatusBarHelper.setStatusBarDarkMode(this);
    }

    /**
     * 设置状态栏黑色字体图标
     * @return
     */
    protected boolean setStatusTxtBlack(){
        return QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    /**
     * 设置window背景
     * @param resId
     */
    protected void setWindowBg(@DrawableRes int resId){
        getWindow().setBackgroundDrawableResource(resId);
    }

    /**
     * Log.e
     * @param str
     */
    protected void logE(String str){
        LogUtils.e(getClass().getSimpleName(),str);
    }

    protected void toast(int str){
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }

    protected void toast(String str){
        if (StringUtil.isEmpty(str))return;
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    protected ViewGroup getParentView(){
        return mView;
    }

    @Override
    protected void onDestroy() {
        mActivity = null;
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 显示软键盘
     * @param editText
     * @param delay  是否延时显示，如不需要写0
     */
    protected void showKeyboard(EditText editText, int delay){
        QMUIKeyboardHelper.showKeyboard(editText,delay);
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    protected void hideKeyboard(View view){
        QMUIKeyboardHelper.hideKeyboard(view);
    }

    public void showLoading(){
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }
    public void showLoading(String str){
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
        loadingDialog.setText(str);
    }

    public void setLoadingTxt(String str){
        if (loadingDialog != null){
            loadingDialog.setText(str);
        }
    }

    public void dissmissLoading(){
        if (loadingDialog != null){
            try {
                loadingDialog.dismiss();
            } catch (Exception e){

            }
        }
    }

    /**
     * 打开相册
     *
     * @param maxSize
     * @param selectImageList
     * @param requestCode
     * @param listener
     */
    public void openAlbum(int maxSize, List<LocalMedia> selectImageList, int requestCode, OnOpenAlbumResultListener listener) {
        if (selectImageList == null){
            selectImageList = new ArrayList<>();
        }
        this.openAlbumResultListener = listener;
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .compress(true) // 是否压缩 true or false
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .selectionMode(maxSize > 1 ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认 true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1 之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .selectionMedia(selectImageList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true);// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
        if (maxSize > 1) {
            pictureSelectionModel.maxSelectNum(maxSize);
        }
        pictureSelectionModel.forResult(requestCode);//结果回调onActivityResult code
    }

    /**
     * 打开视频 不带拍小视频
     * @param maxSize
     * @param selectImageList
     * @param requestCode
     * @param listener
     */
    public void openVideo(int maxSize, List<LocalMedia> selectImageList, int requestCode, OnOpenAlbumResultListener listener) {
        openVideo(maxSize, true, selectImageList, requestCode, listener);
    }

    /**
     * 打开视频
     *
     * @param maxSize
     * @param selectImageList
     * @param requestCode
     * @param listener
     */
    public void openVideo(int maxSize, boolean withCamera, List<LocalMedia> selectImageList, int requestCode, OnOpenAlbumResultListener listener) {
        if (selectImageList == null){
            selectImageList = new ArrayList<>();
        }
        this.openAlbumResultListener = listener;
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(this)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.minSelectNum(1)// 最小选择数量 int

                .imageSpanCount(3)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(withCamera)// 是否显示拍照按钮 true or false
                .compress(true) // 是否压缩 true or false
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .selectionMode(maxSize > 1 ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认 true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1 之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .selectionMedia(selectImageList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .videoQuality(0)//视频质量
                .videoMaxSecond(20)//最大时常视频
                .recordVideoSecond(10);//默认拍摄时间

        if (maxSize > 1) {
            pictureSelectionModel.maxSelectNum(maxSize);
        }
        pictureSelectionModel.forResult(requestCode);//结果回调onActivityResult code
    }

    /**
     * 拍照
     *
     * @param requestCode
     * @param listener
     */
    public void openCamera(int requestCode, OnOpenAlbumResultListener listener) {
        this.openAlbumResultListener = listener;
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .compress(true) // 是否压缩 true or false
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .forResult(requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (openAlbumResultListener != null) {
                openAlbumResultListener.onResult(requestCode, PictureSelector.obtainMultipleResult(data));
            }
        }
    }

    protected void setTextToTv(TextView tv, String data) {
        if (tv == null || StringUtil.isEmpty(data)) {
            return;
        }
        tv.setText(data);
    }

    /**
     * 获取定位权限
     *
     * @param permissionsResult
     */
    protected void getLocationPermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 获取相机和音频权限
     *
     * @param permissionsResult
     */
    protected void getCameraAndRecordAudioPermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 获取相机权限
     *
     * @param permissionsResult
     */
    protected void getCameraPermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.CAMERA);
    }

    /**
     * 获取音频权限
     *
     * @param permissionsResult
     */
    public void getRecordAudioPermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 获取设备信息权限
     *
     * @param permissionsResult
     */
    protected void getReadPhonePermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 获取基本权限
     *
     * @param permissionsResult
     */
    protected void getInitPermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.WRITE_EXTERNAL_STORAGE//内存读写
                , Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO);//设备信息
    }

    protected void getLoacationPermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * 获取打电话的权限
     *
     * @param permissionsResult
     */
    protected void getCallPermissions(PermissionsUtils.IPermissionsResult permissionsResult) {
        checkPermissions(permissionsResult, Manifest.permission.CALL_PHONE);
    }
}
