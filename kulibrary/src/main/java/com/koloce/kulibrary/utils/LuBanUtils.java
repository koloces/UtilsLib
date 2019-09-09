package com.koloce.kulibrary.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by koloces on 2019/3/6
 */
public class LuBanUtils {

    public static void compression(Context context, List<String> filePaths, final OnCompressionListener listener) {
        if (filePaths == null || filePaths.size() == 0) {
            listener.onFailed();
            return;
        }
        compressionImages(context, 0, filePaths, new ArrayList<File>(), new OnMoreCompressionListener() {
            @Override
            public void onSuccess(List<File> files) {
                listener.onSuccess(files);
            }
        });
    }

    //压缩图片
    public static void compression(Context context, String filePath, final OnOneCompressionCallBack listener) {
        if (StringUtil.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!isImg(filePath)) {
            listener.onSuccess(file);
            return;
        }
        Luban.with(context)
                .load(file)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        listener.onSuccess(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed();
                    }
                }).launch();    //启动压缩
    }


    private static void compressionImages(final Context context, final int position, final List<String> filePaths,
                                          final List<File> resultFiles, final OnMoreCompressionListener listener) {
        if (position == filePaths.size()) {
            listener.onSuccess(resultFiles);
            return;
        }
        compression(context, filePaths.get(position), new OnOneCompressionCallBack() {
            @Override
            public void onSuccess(File file) {
                resultFiles.add(file);
                compressionImages(context, position + 1, filePaths, resultFiles, listener);
            }

            @Override
            public void onFailed() {
                compressionImages(context, position + 1, filePaths, resultFiles, listener);
            }
        });

    }

    public interface OnCompressionListener {
        void onSuccess(List<File> file);

        void onFailed();
    }

    private interface OnMoreCompressionListener {
        void onSuccess(List<File> files);
    }

    public interface OnOneCompressionCallBack {
        void onSuccess(File file);

        void onFailed();
    }

    public static boolean isImg(String filePath) {
        return (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".jpeg")
                || filePath.endsWith(".JPG") || filePath.endsWith(".PNG") || filePath.endsWith(".JPEG"));
    }
}
