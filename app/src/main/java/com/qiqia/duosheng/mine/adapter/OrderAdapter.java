package com.qiqia.duosheng.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.mine.bean.OrderBean;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<OrderBean, DataBindBaseViewHolder> {
    public OrderAdapter(List<OrderBean> datas) {
        super(R.layout.item_order,datas);
    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, OrderBean item) {
    }
}
