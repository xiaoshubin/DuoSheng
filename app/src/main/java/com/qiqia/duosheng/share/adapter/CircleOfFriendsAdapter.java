package com.qiqia.duosheng.share.adapter;

import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.share.bean.ShareList;

import cn.com.smallcake_utils.SpannableStringUtils;

/**
 * 朋友圈
 */
public class CircleOfFriendsAdapter extends BaseQuickAdapter<ShareList.DataBean, BaseViewHolder> {
    public CircleOfFriendsAdapter() {
        super(R.layout.item_circle_of_friends);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareList.DataBean item) {
        TextView tvGoodsInfoDesc = helper.getView(R.id.tv_goods_info_desc);
        SpannableStringBuilder goodsInfoDesc = SpannableStringUtils.getBuilder(item.getItemTitle())
                .append("   查看详情").setForegroundColor(Color.parseColor("#D89C2A")).create();
        tvGoodsInfoDesc.setText(goodsInfoDesc);
        helper.addOnClickListener(R.id.tv_share).addOnClickListener(R.id.tv_goods_info_desc);
        helper.setText(R.id.tv_commission_price,"预估佣金￥"+item.getCommision())
        .setText(R.id.tv_share,item.getClick())
        .setText(R.id.tv_goods_desc,  Html.fromHtml(Html.fromHtml(item.getCopyContent()).toString()));
        ImageView ivGoodsPic = helper.getView(R.id.iv_goods_pic);
        Glide.with(mContext).load(item.getItemPic()).into(ivGoodsPic);

    }
}
