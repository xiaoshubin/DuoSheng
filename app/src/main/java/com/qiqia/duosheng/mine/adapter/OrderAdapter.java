package com.qiqia.duosheng.mine.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemOrderBinding;
import com.qiqia.duosheng.databinding.ItemTeamOrderBinding;
import com.qiqia.duosheng.mine.bean.OrderBean;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

import java.util.List;

public class OrderAdapter extends BaseMultiItemQuickAdapter<OrderBean, DataBindBaseViewHolder> {
    public OrderAdapter(List<OrderBean> datas) {
        super(datas);
        addItemType(0,R.layout.item_order);
        addItemType(1,R.layout.item_team_order);
    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, OrderBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                ItemOrderBinding binding0 = (ItemOrderBinding) helper.getBinding();
                break;
            case 1:
                ItemTeamOrderBinding binding1 = (ItemTeamOrderBinding) helper.getBinding();
                break;
        }
    }
}
