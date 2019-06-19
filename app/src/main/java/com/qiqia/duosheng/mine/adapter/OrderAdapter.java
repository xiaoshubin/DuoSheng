package com.qiqia.duosheng.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.mine.bean.OrderBean;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    public OrderAdapter(List<OrderBean> datas) {
        super(R.layout.item_order,datas);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {

    }
}
