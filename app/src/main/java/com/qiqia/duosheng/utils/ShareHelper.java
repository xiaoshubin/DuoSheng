package com.qiqia.duosheng.utils;

import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.custom.CouponBg;
import com.qiqia.duosheng.search.bean.GoodsInfo;

import cn.com.smallcake_utils.BitmapUtils;
import cn.com.smallcake_utils.SpannableStringUtils;

/**
 * 分享帮助类
 */
public class ShareHelper {
    /**
     *
     * @param view R.layout.share_layout_img 中的id为layout_img的LinearLayout
     * @param item 商品详情
     * @param bitmap 要展示的商品图片bitmap
     * @param imageView 装载合成图片的ImageView
     */
    public static void createCode2BitmapToImageView(View view, GoodsInfo item, Bitmap bitmap, ImageView imageView) {
        //设置对应商品信息
        ImageView ivIcon = view.findViewById(R.id.iv_icon);
        ImageView ivGoodsPic = view.findViewById(R.id.iv_goods_pic);
        ImageView ivCode2 = view.findViewById(R.id.iv_code2);

        TextView tvTitle = view.findViewById(R.id.tv_goods_title);
        TextView tvPrice = view.findViewById(R.id.tv_price);
        TextView tvOldPreceSales = view.findViewById(R.id.tv_old_price_sales);
        CouponBg couponPrice = view.findViewById(R.id.coupon_price);
        TextView tvRecommendDesc = view.findViewById(R.id.tv_recommend_desc);
        ivIcon.setImageResource(item.getIsTmall() == 1 ? R.mipmap.icon_tmall : R.mipmap.icon_tb);
        String superCode = "123456";
        try {
            superCode = DataLocalUtils.getUser().getSuperCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //分享的二维码图片
        Bitmap qrCode = Code2Utils.createQRCode("http://192.168.1.158:9000/share.html?id="+item.getItemId()+"&superCode="+superCode, 200,1);
        ivCode2.setImageBitmap(qrCode);


        tvTitle.setText(item.getItemShortTitle());
        SpannableStringBuilder priceSpan = SpannableStringUtils.getBuilder("￥").setBold().append(item.getItemEndPrice()).setBold().setProportion(1.6f).append("券后价").create();
        tvPrice.setText(priceSpan);
        SpannableStringBuilder oldPriceSales = SpannableStringUtils.getBuilder("￥" + item.getItemPrice() + "原价").setStrikethrough()
                .append("    销量" + item.getItemSale()).create();
        tvOldPreceSales.setText(oldPriceSales);
        couponPrice.setCouponText(item.getCouponMoney() + "元");
        tvRecommendDesc.setText(item.getItemTitle());
        ivGoodsPic.setImageBitmap(bitmap);

        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Bitmap bitmap= BitmapUtils.loadBitmapFromView(view);
                imageView.setImageBitmap(bitmap);
            }
        });


    }
}
