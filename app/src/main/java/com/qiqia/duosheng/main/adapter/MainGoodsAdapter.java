package com.qiqia.duosheng.main.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.main.bean.MainTypeGoods;
import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.util.List;

import cn.com.smallcake_utils.ScreenUtils;

public class MainGoodsAdapter extends BaseQuickAdapter<MainTypeGoods, BaseViewHolder> {
    private OnItemGoodsClickListener onItemGoodsClickListener;
    public interface OnItemGoodsClickListener {
        void onItemGoodsClick(GoodsInfo goods);
    }

    public void setOnItemGoodsClickListener(OnItemGoodsClickListener onItemGoodsClickListener) {
        this.onItemGoodsClickListener = onItemGoodsClickListener;
    }

    public MainGoodsAdapter() {
        super(R.layout.item_main_type_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainTypeGoods item) {
        helper.setText(R.id.tv_head_title,item.getHeadTitle())
                .setImageResource(R.id.iv_head_icon,item.getHeadIconRes())
                .addOnClickListener(R.id.tv_more);
        if (item.getHeadTitle().equals("今日必买推荐"))helper.getView(R.id.tv_more).setVisibility(View.GONE);
        addGoods(helper,item.getGoodsList());
    }

    private void addGoods(BaseViewHolder helper, List<GoodsInfo> goodsList) {
        if (goodsList==null)return;
        LinearLayout view = helper.getView(R.id.layout_goods);
        view.removeAllViews();
        for (int i = 0; i < goodsList.size(); i++) {
            GoodsInfo goods = goodsList.get(i);
            //控件获取
            View layoutGoods = LayoutInflater.from(mContext).inflate(R.layout.item_main_goods, null);
            layoutGoods.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth()/3, LinearLayout.LayoutParams.MATCH_PARENT));
            ImageView ivIcon = layoutGoods.findViewById(R.id.iv_icon);
            TextView tvTitle = layoutGoods.findViewById(R.id.tv_title);
            TextView tvPrice = layoutGoods.findViewById(R.id.tv_price);
            TextView tvCoupon = layoutGoods.findViewById(R.id.tv_coupon);
            TextView tvOldPrice = layoutGoods.findViewById(R.id.tv_old_price);
            //样式设置
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //数据加载
            RequestOptions options = new RequestOptions();
            options.transform(new RoundedCorners(9)).placeholder(R.mipmap.logo).error(R.mipmap.logo);
            Glide.with(mContext).load(goods.getItemPic()).apply(options).into(ivIcon);
            tvTitle.setText(goods.getItemShortTitle());
            tvPrice.setText("￥"+goods.getItemEndPrice());
            tvCoupon.setText(goods.getCouponMoney()+"元券");
            tvOldPrice.setText("￥"+goods.getItemPrice());
            //点击事件
            layoutGoods.setOnClickListener(v -> {
                if (onItemGoodsClickListener!=null)onItemGoodsClickListener.onItemGoodsClick(goods);
            });
            view.addView(layoutGoods);
        }


    }
}
