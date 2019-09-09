package com.koloce.kulibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import com.qmuiteam.qmui.util.QMUIDirection;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;

import static com.qmuiteam.qmui.util.QMUIDrawableHelper.createBitmapSafely;

/**
 * Created on 2019/3/30
 */
public class QMUIUtils {

    //节省每次创建时产生的开销，但要注意多线程操作synchronized
    private static final Canvas sCanvas = new Canvas();

    /**
     *
     * @param radius        圆角大小
     * @param startColor    渐变开始色
     * @param endColor      渐变结束色
     * @return              返回所创建的渐变图片。
     */
    public static GradientDrawable createCircleGradientDrawable(int radius, @ColorInt int startColor, @ColorInt int endColor){
        return QMUIDrawableHelper.createCircleGradientDrawable(startColor, endColor, radius, 0.5f, 0.5f);
    }

    /**
     * 创建一张指定大小的纯色图片，支持圆角
     *
     * @param context       Resources对象，用于创建BitmapDrawable
     * @param width         图片的宽度
     * @param height        图片的高度
     * @param radius        图片的圆角，不需要则传0
     * @param color         图片的填充色
     * @return              指定大小的纯色图片
     */
    public static BitmapDrawable createDrawableWithSize(Context context, int width, int height, int radius, @ColorInt int color){
        return QMUIDrawableHelper.createDrawableWithSize(context.getResources(), width, height, radius, color);
    }

    /**
     * 设置Drawable的颜色
     * 这里不对Drawable进行mutate()，会影响到所有用到这个Drawable的地方，如果要避免，请先自行mutate()
     * @param drawable
     * @param color
     * @return
     */
    public static void changeDrawableColor(Drawable drawable, @ColorInt int color){
        QMUIDrawableHelper.setDrawableTintColor(drawable, color);
    }


    public static int dp2px(Context context, int dp) {
        return QMUIDisplayHelper.dp2px(context,dp);
    }

    public static int px2dp(Context context, int px){
        return QMUIDisplayHelper.px2dp(context,px);
    }

    public static int sp2px(Context context, int sp){
        return QMUIDisplayHelper.sp2px(context,sp);
    }

    public static int px2sp(Context context, int px){
        return QMUIDisplayHelper.px2sp(context,px);
    }

    /**
     * 从一个view创建Bitmap。
     * 注意点：绘制之前要清掉 View 的焦点，因为焦点可能会改变一个 View 的 UI 状态。
     * 来源：https://github.com/tyrantgit/ExplosionField
     *
     * @param view  传入一个 View，会获取这个 View 的内容创建 Bitmap。
     * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
     */
    public static Bitmap getBitmapFromView(View view, float scale){
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }
        view.clearFocus();
        Bitmap bitmap = createBitmapSafely((int) (view.getWidth() * scale),
                (int) (view.getHeight() * scale), Bitmap.Config.ARGB_8888, 1);
        if (bitmap != null) {
            synchronized (sCanvas) {
                Canvas canvas = sCanvas;
                canvas.setBitmap(bitmap);
                canvas.save();
                canvas.drawColor(Color.WHITE); // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
                canvas.scale(scale, scale);
                view.draw(canvas);
                canvas.restore();
                canvas.setBitmap(null);
            }
        }
        return bitmap;
    }

    /**
     * QMUI Slide进场动画
     * @param view
     * @param duration
     * @param listener
     */
    public static void slideIn(View view, int duration, Animation.AnimationListener listener){
        QMUIViewHelper.slideIn(view, duration, listener, true, QMUIDirection.TOP_TO_BOTTOM);
    }

    public static void slideIn(View view){
        QMUIViewHelper.slideIn(view, 500, null, true, QMUIDirection.TOP_TO_BOTTOM);
    }

    /**
     * QMUI Slide出场动画
     * @param view
     * @param duration
     * @param listener
     */
    public static void slideOut(View view, int duration, Animation.AnimationListener listener){
        QMUIViewHelper.slideOut(view, duration, listener, true, QMUIDirection.TOP_TO_BOTTOM);
    }

    public static void slideOut(View view){
        QMUIViewHelper.slideOut(view, 500, null, true, QMUIDirection.TOP_TO_BOTTOM);
    }

    /**
     * QMUI Fade进场动画
     */
    public static void fadeIn(View view, int duration, Animation.AnimationListener listener){
        QMUIViewHelper.fadeIn(view, duration, listener, true);
    }

    public static void fadeIn(View view){
        QMUIViewHelper.fadeIn(view, 500, null, true);
    }

    /**
     * QMUI Fade出场动画
     */
    public static void fadeOut(View view, int duration, Animation.AnimationListener listener){
        QMUIViewHelper.fadeOut(view, duration, listener, true);
    }

    public static void fadeOut(View view){
        fadeOut(view,500,null);
    }

    /**
     * 隐藏键盘
     * @param view
     */
    public static void hideKeyboard(View view){
        QMUIKeyboardHelper.hideKeyboard(view);
    }

    /**
     * 显示键盘
     * @param editText
     * @param delay 延迟时间（不要写0）
     */
    public static void showKeyboard(EditText editText, int delay){
        QMUIKeyboardHelper.showKeyboard(editText,delay);
    }

    /**
     * 添加软键盘的显示隐藏监听
     * @param activity
     * @param listener
     */
    public static void setVisibilityEventListener(final Activity activity,
                                                  final QMUIKeyboardHelper.KeyboardVisibilityEventListener listener){
        QMUIKeyboardHelper.setVisibilityEventListener(activity,listener);
    }
    /**
     * 软键盘是否隐藏
     * @param activity
     */
    public static boolean isKeyboardVisible(Activity activity){
        return QMUIKeyboardHelper.isKeyboardVisible(activity);
    }
}
