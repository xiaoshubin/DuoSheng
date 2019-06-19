package com.qiqia.duosheng.main.bean;

import java.io.Serializable;

public class MenuItem  implements Serializable {
    private int imgRes;
    private String title;

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
}
