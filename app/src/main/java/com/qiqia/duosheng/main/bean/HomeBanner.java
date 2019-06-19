package com.qiqia.duosheng.main.bean;

import java.io.Serializable;

public class HomeBanner  implements Serializable {
    private int imgRes;

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public HomeBanner(int imgRes) {
        this.imgRes = imgRes;
    }
}
