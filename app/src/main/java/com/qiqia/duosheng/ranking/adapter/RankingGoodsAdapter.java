package com.qiqia.duosheng.ranking.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemRankingGoodsBinding;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

public class RankingGoodsAdapter extends BaseQuickAdapter<GoodsInfo, DataBindBaseViewHolder> {
    public RankingGoodsAdapter() {
        super(R.layout.item_ranking_goods);
    }


    @Override
    protected void convert(DataBindBaseViewHolder helper, GoodsInfo item){
        ItemRankingGoodsBinding binding = (ItemRankingGoodsBinding) helper.getBinding();
        binding.setItem(item);
        binding.couponPrice.setCouponText(item.getCouponMoney()+"元");
        //根据排行不同，显示不同Tag
        if (helper.getLayoutPosition()==0){
            binding.ivTag.setImageResource(R.mipmap.top1);
        }else if (helper.getLayoutPosition()==1){
            binding.ivTag.setImageResource(R.mipmap.top2);
        } else if (helper.getLayoutPosition()==2){
            binding.ivTag.setImageResource(R.mipmap.top3);
        }else {
            binding.ivTag.setImageResource(0);
        }

    }
}
