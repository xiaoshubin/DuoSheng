package com.qiqia.duosheng.mine.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class OrderBean implements MultiItemEntity, Serializable {

    private int type;
    @Override
    public int getItemType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
