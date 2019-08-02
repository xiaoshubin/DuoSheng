package com.qiqia.duosheng.main.bean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.io.Serializable;

public class MainSectionGoods extends SectionEntity implements Serializable {
    private Integer headIconRes;

    public MainSectionGoods(Integer headIconRes, String headTitle) {
        super(true,headTitle);
        this.headIconRes = headIconRes;
    }
    public MainSectionGoods(GoodsInfo goodsInfo) {
       super(goodsInfo);
    }


}
