package com.qiqia.duosheng.bean;

import java.io.Serializable;

public class IndexTextAd  implements Serializable {


    /**
     * content : asdasdasda
     * time : 1234567890
     */

    private String content;
    private int time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
