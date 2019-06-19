package com.qiqia.duosheng.main.bean;

import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.io.Serializable;
import java.util.List;

public class MainTypeGoods  implements Serializable {
    private Integer headIconRes;
    private String headTitle;
    List<GoodsInfo> goodsList;

    public MainTypeGoods(Integer headIconRes, String headTitle, List<GoodsInfo> goodsList) {
        this.headIconRes = headIconRes;
        this.headTitle = headTitle;
        this.goodsList = goodsList;
    }

    public Integer getHeadIconRes() {
        return headIconRes;
    }

    public void setHeadIconRes(Integer headIconRes) {
        this.headIconRes = headIconRes;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public List<GoodsInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsInfo> goodsList) {
        this.goodsList = goodsList;
    }
}
