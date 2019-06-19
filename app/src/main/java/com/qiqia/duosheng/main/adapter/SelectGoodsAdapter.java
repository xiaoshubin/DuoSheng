package com.qiqia.duosheng.main.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.main.bean.SelectGoods;

import java.util.List;

public class SelectGoodsAdapter extends BaseSectionQuickAdapter<SelectGoods,BaseViewHolder> {

    public SelectGoodsAdapter(List data) {
        super(R.layout.item_select_goods, R.layout.item_select_goods_header, data);
    }
    @Override
    protected void convertHead(BaseViewHolder helper, SelectGoods item) {
        helper.setText(R.id.tv_head_title,item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectGoods item) {

    }
}
