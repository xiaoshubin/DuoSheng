package com.qiqia.duosheng.main.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.main.bean.IndexClassfiy;

public class GoodsTypeAdapter extends BaseQuickAdapter<IndexClassfiy.DataBean, BaseViewHolder> {
    public GoodsTypeAdapter() {
        super(R.layout.item_goods_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexClassfiy.DataBean item) {
        helper.setText(R.id.tv_name,item.getName());
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        Glide.with(mContext).load(item.getUrl()).into(ivIcon);
    }
}
