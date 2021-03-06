package com.qiqia.duosheng.main.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.custom.CornerTransform;
import com.qiqia.duosheng.custom.CouponBg;
import com.qiqia.duosheng.search.bean.GoodsInfo;

/**
 * 推荐商品适配器
 */
public class RecommandGoodsAdapter extends BaseQuickAdapter<GoodsInfo, BaseViewHolder>{
    public RecommandGoodsAdapter() {
        super(R.layout.item_recommand_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo item) {
        try {
            helper.setText(R.id.tv_title,item.getItemTitle())
                    .setText(R.id.tv_shop_title,item.getShopName())
                    .setText(R.id.tv_volume,"已售"+item.getItemSale())
                    .setText(R.id.tv_commission_price,"预估收益￥"+item.getCommision())
                    .setText(R.id.tv_reserve_price,"￥"+item.getItemPrice())
                    .setText(R.id.tv_zk_final_price,item.getItemEndPrice());

            CouponBg couponBg = helper.getView(R.id.custom_coupon);
            couponBg.setCouponText(item.getCouponMoney()+"元");

            //加入删除线
            TextView tvReservePrice = helper.getView(R.id.tv_reserve_price);
            tvReservePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //是否显示推荐价格
            String reserve_price = item.getItemPrice();
            String zk_final_price = item.getItemEndPrice();
            helper.getView(R.id.tv_reserve_price).setVisibility(reserve_price.equals(zk_final_price)? View.GONE:View.VISIBLE);
            //显示商品图片
            ImageView ivPic = helper.getView(R.id.iv_pict_url);

            CornerTransform transformation = new CornerTransform(mContext, 9);
            transformation.setExceptCorner(false, false, true, true);

            Glide.with(mContext).load(item.getItemPic()).apply(new RequestOptions().transform(transformation)).into(ivPic);
            //根据类型，显示不同的小图标，淘宝或天猫
            ImageView ivUserType = helper.getView(R.id.iv_user_type);
            boolean isTmall = item.getIsTmall()==1;
            Glide.with(mContext).load(isTmall?R.mipmap.icon_tmall:R.mipmap.icon_tb).into(ivUserType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
