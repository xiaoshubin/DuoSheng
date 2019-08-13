package com.qiqia.duosheng.utils;

import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiqia.duosheng.R;

import cn.com.smallcake_utils.SmallUtils;

public class GoodsStatusBindingAdapter {
    //是否收藏
    @BindingAdapter("isCollect")
    public static void isCollect(TextView tvCollection, int isCollect){
        boolean isCollectBoolean = isCollect==1;
        tvCollection.setText(isCollectBoolean ? "已收藏" : "收藏");
        Drawable img = SmallUtils.getApp().getResources().getDrawable(isCollectBoolean ? R.mipmap.icon_collected : R.mipmap.icon_collect);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        tvCollection.setCompoundDrawables(null, img, null, null);

    }
    @BindingAdapter("isTmall")
    public static void isTmall(ImageView imageView, int isTmall){
        boolean isTmallBoolean = isTmall == 1;
    }
}
