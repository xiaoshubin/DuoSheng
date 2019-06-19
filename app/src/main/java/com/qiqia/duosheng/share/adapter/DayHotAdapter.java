package com.qiqia.duosheng.share.adapter;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.search.bean.GoodsInfo;

import cn.com.smallcake_utils.SpannableStringUtils;

public class DayHotAdapter extends BaseQuickAdapter<GoodsInfo, BaseViewHolder> {
    public DayHotAdapter() {
        super(R.layout.item_day_hot);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo item) {
        String guideArticle = item.getGuideArticle();
        SpannableStringBuilder goodsInfoDesc = SpannableStringUtils.getBuilder(TextUtils.isEmpty(guideArticle)?item.getItemDesc():guideArticle)
                .append("   查看详情").setForegroundColor(Color.parseColor("#D89C2A")).create();

        helper.setText(R.id.tv_goods_info_desc, goodsInfoDesc)
                .setText(R.id.tv_commission_price, "预估佣金￥" + item.getCommision())
                .setText(R.id.tv_share, item.getItemSale())
                .setText(R.id.tv_goods_desc, item.getItemShortTitle())
                .addOnClickListener(R.id.tv_share).addOnClickListener(R.id.tv_goods_info_desc)
                .addOnClickListener(R.id.tv_share).addOnClickListener(R.id.tv_copy_comment);
        //设置图片
        ImageView ivGoodsPic = helper.getView(R.id.iv_goods_pic);
        Glide.with(mContext).load(item.getItemPicList().get(0)).into(ivGoodsPic);

    }
}
