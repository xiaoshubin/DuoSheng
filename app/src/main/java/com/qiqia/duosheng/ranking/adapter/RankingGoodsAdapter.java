package com.qiqia.duosheng.ranking.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemRankingGoodsBinding;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

public class RankingGoodsAdapter extends BaseQuickAdapter<GoodsInfo, DataBindBaseViewHolder> {
    private boolean isShowTop;
    public RankingGoodsAdapter() {
        this(true);
    }
    public RankingGoodsAdapter(boolean isShowTop) {
        super(R.layout.item_ranking_goods);
        this.isShowTop = isShowTop;
    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, GoodsInfo item){
        ItemRankingGoodsBinding binding = (ItemRankingGoodsBinding) helper.getBinding();
        binding.setItem(item);
        binding.couponPrice.setCouponText(item.getCouponMoney()+"元");

        if (isShowTop){
            switch (helper.getLayoutPosition()){
                case 0:binding.ivTag.setImageResource(R.mipmap.top1);break;
                case 1:binding.ivTag.setImageResource(R.mipmap.top2);break;
                case 2:binding.ivTag.setImageResource(R.mipmap.top3);break;
                default:binding.ivTag.setImageResource(0);
            }
        }

    }
}
