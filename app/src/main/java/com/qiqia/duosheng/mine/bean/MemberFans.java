package com.qiqia.duosheng.mine.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MemberFans implements MultiItemEntity {
    public static final int UNDER = 1;//直属
    public static final int FISSION = 2;//裂变
    private int itemType;
    public MemberFans(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
