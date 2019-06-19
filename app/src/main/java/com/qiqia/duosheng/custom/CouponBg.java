package com.qiqia.duosheng.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiqia.duosheng.R;

/**
 * 券背景图片
 */
public class CouponBg extends RelativeLayout {

    TextView tvCouponPrice;
    public CouponBg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View rootView = LayoutInflater.from(this.getContext()).inflate(R.layout.coupon_bg_layout, this);
         tvCouponPrice = rootView.findViewById(R.id.tv_coupon_price);


        TypedArray attributes  = this.getContext().obtainStyledAttributes(attrs, R.styleable.CouponBg);
        if (attributes!=null){
            String couponPrice = attributes .getString(R.styleable.CouponBg_coupon_price);
            tvCouponPrice.setText(couponPrice);
            attributes .recycle();
        }

    }



    public final void setCouponText(CharSequence text) {
        tvCouponPrice.setText(text);
    }


}
