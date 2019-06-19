package com.qiqia.duosheng.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class IndexAd extends LitePalSupport implements Serializable {

    /**
     * type : 0
     * url :
     * id : 123456789
     * title : asdasdasd
     * img : asdasdasda
     * frequency : 1
     * endPrice : 12.33
     */
    private int adId;
    private int time;
    private int showTime;//展示的时间
    private int type;
    private String url;
    private long id;
    private String title;
    private String img;
    private int frequency;
    private String endPrice;


    @Override
    public String toString() {
        return "IndexAd{" +
                "adId=" + adId +
                ", time=" + time +
                ", showTime=" + showTime +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", frequency=" + frequency +
                ", endPrice='" + endPrice + '\'' +
                '}';
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
    }
}
