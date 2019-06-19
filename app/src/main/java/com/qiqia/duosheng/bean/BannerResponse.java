package com.qiqia.duosheng.bean;

import java.io.Serializable;

public class BannerResponse implements Serializable {

    /**
     * type : 0
     * value :
     * title : 1
     * img : http://jxmj-com.oss-cn-qingdao.aliyuncs.com/duosheng/1/16.jpg
     * note : 曲师大所多
     */

    private int type;
    private String value;
    private String title;
    private String img;
    private String note;

    private BannerResponse(Builder builder) {
        setType(builder.type);
        setValue(builder.value);
        setTitle(builder.title);
        setImg(builder.img);
        setNote(builder.note);
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static final class Builder {
        private int type;
        private String value;
        private String title;
        private String img;
        private String note;

        public Builder() {
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder img(String val) {
            img = val;
            return this;
        }

        public Builder note(String val) {
            note = val;
            return this;
        }

        public BannerResponse build() {
            return new BannerResponse(this);
        }
    }
}
