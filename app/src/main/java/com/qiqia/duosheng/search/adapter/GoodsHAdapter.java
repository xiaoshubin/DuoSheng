package com.qiqia.duosheng.search.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemGoodsinfoHorizontalBinding;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

public class GoodsHAdapter extends BaseQuickAdapter<GoodsInfo, DataBindBaseViewHolder> {
    public GoodsHAdapter() {
        super(R.layout.item_goodsinfo_horizontal);
    }
    @Override
    protected void convert(DataBindBaseViewHolder helper, GoodsInfo item){
        ItemGoodsinfoHorizontalBinding binding = (ItemGoodsinfoHorizontalBinding) helper.getBinding();
        binding.setItem(item);
        binding.couponPrice.setCouponText(item.getCouponMoney()+"元");
    }
}
