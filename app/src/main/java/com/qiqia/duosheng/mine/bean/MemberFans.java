package com.qiqia.duosheng.mine.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class MemberFans implements MultiItemEntity, Serializable {
    public static final int UNDER = 1;//直属
    public static final int FISSION = 2;//裂变
    public int itemType;
    public MemberFans(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
