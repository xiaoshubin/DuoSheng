package com.qiqia.duosheng.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class SearchKey extends LitePalSupport implements Serializable {
    private int time;
    private String key;

    public SearchKey(int time, String key) {
        this.time = time;
        this.key = key;
    }

    @Override
    public String toString() {
        return "SearchKey{" +
                "time=" + time +
                ", key='" + key + '\'' +
                '}';
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private SearchKey(Builder builder) {
        time = builder.time;
        key = builder.key;
    }

    public static final class Builder {
        private int time;
        private String key;

        public Builder() {
        }

        public Builder time(int val) {
            time = val;
            return this;
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public SearchKey build() {
            return new SearchKey(this);
        }
    }
}
