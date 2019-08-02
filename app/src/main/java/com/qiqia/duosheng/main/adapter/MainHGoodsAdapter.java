package com.qiqia.duosheng.main.adapter;

import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemMainGoodsBinding;
import com.qiqia.duosheng.databinding.ItemMainTypeGoodsBinding;
import com.qiqia.duosheng.main.bean.MainSectionGoods;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

import java.util.List;

import cn.com.smallcake_utils.ScreenUtils;

public class MainHGoodsAdapter extends BaseSectionQuickAdapter<MainSectionGoods, DataBindBaseViewHolder> {
    LinearLayout.LayoutParams layoutParams;
    public MainHGoodsAdapter(List<MainSectionGoods> data) {
        super(R.layout.item_main_goods, R.layout.item_main_type_goods, data);
         layoutParams = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 10 * 3, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void convertHead(DataBindBaseViewHolder helper, MainSectionGoods item) {
        ItemMainTypeGoodsBinding binding = (ItemMainTypeGoodsBinding) helper.getBinding();

    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, MainSectionGoods item) {
        helper.itemView.setLayoutParams(layoutParams);
        ItemMainGoodsBinding binding = (ItemMainGoodsBinding) helper.getBinding();
        GoodsInfo itemGoods = (GoodsInfo) item.t;
        binding.setItem(itemGoods);
    }
}
