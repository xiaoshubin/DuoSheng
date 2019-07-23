package com.qiqia.duosheng.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuItem  implements Parcelable {
    private String imgUrl="https://upload.jianshu.io/users/upload_avatars/1503465/9d96f64dda43.png?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96";
    private int imgRes;
    private String title;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public MenuItem(int imgRes, String title) {
        this.imgRes = imgRes;
        this.title = title;
    }
    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imgRes);
        dest.writeString(title);
    }

    protected MenuItem(Parcel in) {
        imgRes = in.readInt();
        title = in.readString();
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}
