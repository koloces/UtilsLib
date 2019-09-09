package com.koloce.kulibrary.view.nineGridView;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述：单张图片信息类
 */

public class ImageBeen implements Parcelable {


    /**
     * img_url : http://postpic.xiaomatv.cn/FtKjYUrtErxd5pSlKrLJruASxtBB
     * width : 640
     * height : 853
     * img_url_small : http://postpic.xiaomatv.cn/FtKjYUrtErxd5pSlKrLJruASxtBB_small_2802
     * small_width : 110
     * small_height : 587
     */

    private String img_url;
    private int width;
    private int height;
    private String img_url_small;
    private String small_width;
    private String small_height;

    protected ImageBeen(Parcel in) {
        img_url = in.readString();
        width = in.readInt();
        height = in.readInt();
        img_url_small = in.readString();
        small_width = in.readString();
        small_height = in.readString();
    }

    public ImageBeen() {
    }

    public static final Creator<ImageBeen> CREATOR = new Creator<ImageBeen>() {
        @Override
        public ImageBeen createFromParcel(Parcel in) {
            return new ImageBeen(in);
        }

        @Override
        public ImageBeen[] newArray(int size) {
            return new ImageBeen[size];
        }
    };

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImg_url_small() {
        return img_url_small;
    }

    public void setImg_url_small(String img_url_small) {
        this.img_url_small = img_url_small;
    }

    public String getSmall_width() {
        return small_width;
    }

    public void setSmall_width(String small_width) {
        this.small_width = small_width;
    }

    public String getSmall_height() {
        return small_height;
    }

    public void setSmall_height(String small_height) {
        this.small_height = small_height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img_url);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(img_url_small);
        parcel.writeString(small_width);
        parcel.writeString(small_height);
    }
}
