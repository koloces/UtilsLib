package com.koloce.kulibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.koloce.kulibrary.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


public class ImageLoaderGlide {


    /**
     * 是否允许加载图片
     *
     * @param context
     * @param imgUrl
     * @param imageView
     * @return
     */
    private static boolean canLoad(Context context, Object imgUrl, ImageView imageView) {
        if (context == null) return false;
        if (imgUrl == null) return false;
        if (imageView == null) return false;
        return true;
    }


    /**
     * 获取加载图片的公共参数
     *
     * @return
     */
    private static RequestOptions getLoadRequestOptions() {
        return new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.white);
    }

    /**
     * 获取加载图片的公共参数
     *
     * @return
     */
    private static RequestOptions getLoadRequestOptions(int placeHolder) {
        return new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .placeholder(R.color.white);
    }

    /**
     * 获取加载图片的公共参数
     *
     * @return
     */
    private static RequestOptions getLoadRequestOptions(int placeHolder, int error) {
        return new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .error(error)
                .placeholder(R.color.white);
    }

    public static void loadImage(Context context, Object imgUrl, ImageView imageView) {
        if (!canLoad(context, imgUrl, imageView)) return;
        Glide.with(context)
                .load(imgUrl)
                .apply(getLoadRequestOptions())
                .into(imageView);
    }


    public static void loadImage(Context context, Object imgUrl, ImageView imageView, RequestListener<Drawable> listener) {
        if (!canLoad(context, imgUrl, imageView)) return;
        Glide.with(context)
                .load(imgUrl)
                .apply(getLoadRequestOptions())
                .listener(listener)
                .into(imageView);
    }

    public static void loadImage(Context context, Object imgUrl, ImageView imageView, int placeHolderResId) {
        if (!canLoad(context, imgUrl, imageView)) return;
        Glide.with(context)
                .load(imgUrl)
                .apply(getLoadRequestOptions(placeHolderResId))
                .into(imageView);
    }


    public static void loadImage(Context context, Object imgUrl, ImageView imageView, int placeHolderResId, int error) {
        if (!canLoad(context, imgUrl, imageView)) return;
        Glide.with(context)
                .load(imgUrl)
                .apply(getLoadRequestOptions(placeHolderResId, error))
                .into(imageView);
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param cornerRadius 弧度數
     */
    public static void loadCornerRadiusImage(Context context, Object imgUrl, ImageView imageView, int cornerRadius) {
        if (!canLoad(context, imgUrl, imageView)) return;
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(cornerRadius);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)/*.override(300, 300)*/;
        Glide.with(context)
                .load(imgUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param imgUrl
     * @param imageView
     */
    public static void loadCircleImg(Context context, Object imgUrl, ImageView imageView) {
        if (!canLoad(context, imgUrl, imageView)) return;
        Glide.with(context).load(imgUrl).apply(RequestOptions.bitmapTransform(new CircleCrop()).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(imageView);
    }


    /**
     * 加载视频第一帧
     *
     * @param context
     * @param uri
     * @param imageView
     */
    public static void loadVideoScreenshot(final Context context, Object uri, ImageView imageView) {
        if (!canLoad(context, uri, imageView)) return;
        RequestOptions requestOptions = RequestOptions.frameOf(1);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

    /**
     * 图片加载
     *
     * @param mContext
     * @param imgPath
     * @param imageView
     */
    public static void loadAsBitmap(Context mContext, Object imgPath, final ImageView imageView) {
        if (!canLoad(mContext, imgPath, imageView)) return;
        loadAsBitmap(mContext, imgPath, imageView, 0, 0);
    }

    public static void loadImgWithCallBack(Context mContext, Object imgPath, SimpleTarget<Bitmap> target) {
        if (mContext == null) return;
        if (imgPath == null) return;
        if (target == null) return;
        Glide.with(mContext)
                .asBitmap()
                .load(imgPath)
                .apply(new RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.white))
                .into(target);
    }

    /**
     * 图片加载
     *
     * @param mContext
     * @param imgPath
     * @param imageView
     */
    public static void loadAsBitmap(Context mContext, Object imgPath, final ImageView imageView, final int width, final int height) {
        if (!canLoad(mContext, imgPath, imageView)) return;
        imageView.setWillNotDraw(true);
        Glide.with(mContext)
                .asBitmap()
                .load(imgPath)
                .apply(new RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.white))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (width > 0 || height > 0) {
                            int imgHeight = resource.getHeight();
                            int imgWidth = resource.getWidth();
                            float sy = 0;
                            if (width > 0 && height > 0) {
                                if (imgHeight > imgWidth) {
                                    sy = (float) height / (float) imgHeight;
                                } else {
                                    sy = (float) width / (float) imgWidth;
                                }
                            } else if (width > 0) {
                                sy = (float) width / (float) imgWidth;
                            } else {
                                sy = (float) height / (float) imgHeight;
                            }
                            if (sy != 0) {
                                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                                layoutParams.height = (int) (sy * imgHeight);
                                layoutParams.width = (int) (sy * imgWidth);
                                imageView.setLayoutParams(layoutParams);
                            }
                        }
                        imageView.setWillNotDraw(false);
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        String s = errorDrawable.toString();
                        LogUtils.e("========>" + s);
                    }
                });
    }


    /**
     * 图片加载视频封面图片(如果视频宽度大于高度则FIT_CENTER,高度大于宽度则FIT_XY)
     *
     * @param mContext
     * @param imgPath
     * @param imageView
     */
    public static void loadVideoImg(Context mContext, Object imgPath, final ImageView imageView) {
        if (!canLoad(mContext, imgPath, imageView)) return;
        imageView.setWillNotDraw(true);
        Glide.with(mContext)
                .asBitmap()
                .load(imgPath)
                .apply(new RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.white))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        if (height > width) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        } else {
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                        imageView.setWillNotDraw(false);
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        String s = errorDrawable.toString();
                        LogUtils.e("========>" + s);
                    }
                });
    }


    /**
     * 加载从相册里拿出来的图片
     * @param context
     * @param iv
     * @param media
     */
    public static void loadAlbumImage(Context context, ImageView iv, LocalMedia media) {
        if (context == null)return;
        if (iv == null)return;
        if (media == null)return;
        int mimeType = media.getMimeType();
        String path = "";
        if (media.isCut() && !media.isCompressed()) {
            // 裁剪过
            path = media.getCutPath();
        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            path = media.getCompressPath();
        } else {
            // 原图
            path = media.getPath();
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.white)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(iv);
    }
}
