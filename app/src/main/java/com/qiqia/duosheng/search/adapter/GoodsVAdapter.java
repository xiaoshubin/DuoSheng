package com.qiqia.duosheng.search.adapter;

import android.support.constraint.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemGoodsinfoVerticalBinding;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.ScreenUtils;
//垂直布局
public class GoodsVAdapter extends BaseQuickAdapter<GoodsInfo, DataBindBaseViewHolder> {
    ConstraintLayout.LayoutParams layoutParams ;
    public GoodsVAdapter() {
        super(R.layout.item_goodsinfo_vertical);
        int screenWidth = ScreenUtils.getScreenWidth();
        int imgWidth = (screenWidth - DpPxUtils.dp2px(16))/2;
        layoutParams = new ConstraintLayout.LayoutParams(imgWidth,imgWidth);
    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, GoodsInfo item) {
        ItemGoodsinfoVerticalBinding binding = (ItemGoodsinfoVerticalBinding) helper.getBinding();
        binding.setItem(item);
        binding.couponPrice.setCouponText(item.getCouponMoney()+"元");
        binding.ivPictUrl.setLayoutParams(layoutParams);
    }
}
