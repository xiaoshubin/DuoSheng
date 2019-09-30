package com.qiqia.duosheng.utils;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import org.jetbrains.annotations.Nullable;

import cn.com.smallcake_utils.SpannableStringUtils;

public class TextBindingAdapter {
    //1.去掉最后的0和00,价格设置
    @BindingAdapter("price")
    public static void bindPrice(TextView view, String price){
        price = trimZero(price);
        SpannableStringBuilder priceSpan = SpannableStringUtils.getBuilder("￥").append(price).setProportion(1.6f).create();
        view.setText(priceSpan);
    }
    //2.去掉最后的0和00,价格有小数位，小数位的数字变小
    @BindingAdapter("priceEndLittle")
    public static void bindPriceEndLittle(TextView view, String price){
        if (TextUtils.isEmpty(price))return;
        SpannableStringBuilder priceSpan;
        if (price.endsWith(".00")){//19.00
            price = price.substring(0,price.length()-3);
             priceSpan = SpannableStringUtils.getBuilder("￥").append(price).setProportion(1.6f).create();

        }else if (price.endsWith("0")&&price.contains(".")){//19.90
            price = price.substring(0,price.length()-1);
            if (price.endsWith("."))price=price.substring(0,price.length()-1);//19.0-->19
            if (price.contains(".")){
                String priceBig = price.substring(0,price.indexOf("."));
                String priceLittle = price.substring(price.indexOf("."));
                priceSpan = SpannableStringUtils.getBuilder("￥").append(priceBig).setProportion(1.6f).append(priceLittle).setProportion(1.2f).create();
            }else {
                priceSpan = SpannableStringUtils.getBuilder("￥").append(price).setProportion(1.6f).create();
            }

        }else if (price.contains(".")){//19.99
            String priceBig = price.substring(0,price.indexOf("."));
            String priceLittle = price.substring(price.indexOf("."));
             priceSpan = SpannableStringUtils.getBuilder("￥").append(priceBig).setProportion(1.6f).append(priceLittle).setProportion(1.2f).create();

        }else {//19
             priceSpan = SpannableStringUtils.getBuilder("￥").append(price).setProportion(1.6f).create();

        }
        view.setText(priceSpan);
    }

    //1.去掉最后的0和00，并添加删除线
    @BindingAdapter("oldPrice")
    public static void bindOldPrice(TextView view, String price){
        price = trimZero(price);
        SpannableStringBuilder priceSpan = SpannableStringUtils.getBuilder("￥"+price).setStrikethrough().create();
        view.setText(priceSpan);
    }
    //预估收益
    @BindingAdapter("predictIncome")
    public static void bindPredictIncome(TextView view, String price){
        price = trimZero(price);
        view.setText("预估收益 ￥"+price);
    }
    //券后价格
    @BindingAdapter("couponUsePrice")
    public static void bindCouponUsePrice(TextView view, String price){
        price = trimZero(price);
        SpannableStringBuilder priceSpan = SpannableStringUtils.getBuilder("券后").append("￥").setBold().append(price).setBold().setProportion(1.6f).create();
        view.setText(priceSpan);
    }
    //券后价格: 券后两字在末尾
    @BindingAdapter("couponUsePrice2")
    public static void bindCouponUsePrice2(TextView view, String price){
        price = trimZero(price);
        if (price == null) return;
        SpannableStringBuilder priceSpan = SpannableStringUtils.getBuilder("￥").setBold().append(price).setBold().setProportion(1.6f).append("券后").create();
        view.setText(priceSpan);
    }
    /**
     *  去除尾部小数点后多余的0,
     *  19.00-->19
     *  19.0-->19
     *  19.90-->19.9
     *
     */
    @Nullable
    private static String trimZero(String price) {
        if (TextUtils.isEmpty(price)) return "";
        if (price.endsWith(".00")) {
            price = price.substring(0, price.length() - 3);
        } else if (price.endsWith("0") && price.contains(".")) {
            price = price.substring(0, price.length() - 1);
            if (price.endsWith(".")) price = price.substring(0, price.length() - 1);//19.0-->19
        }
        return price;
    }
}
