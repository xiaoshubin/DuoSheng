package com.qiqia.duosheng.main.bean;

import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.io.Serializable;
import java.util.List;

public class IndexResponse  implements Serializable {

    private List<GoodsInfo> list9;
    private List<GoodsInfo> hour;
    private List<GoodsInfo> day;

    public List<GoodsInfo> getList9() {
        return list9;
    }

    public void setList9(List<GoodsInfo> list9) {
        this.list9 = list9;
    }

    public List<GoodsInfo> getHour() {
        return hour;
    }

    public void setHour(List<GoodsInfo> hour) {
        this.hour = hour;
    }

    public List<GoodsInfo> getDay() {
        return day;
    }

    public void setDay(List<GoodsInfo> day) {
        this.day = day;
    }
}
