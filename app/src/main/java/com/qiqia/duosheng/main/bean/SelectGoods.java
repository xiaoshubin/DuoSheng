package com.qiqia.duosheng.main.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

public class SelectGoods extends SectionEntity<SelectGoodsItem> {
    public SelectGoods(String header) {
        super(true, header);
    }

    public SelectGoods(SelectGoodsItem selectGoodsItem) {
        super(selectGoodsItem);
    }
}
