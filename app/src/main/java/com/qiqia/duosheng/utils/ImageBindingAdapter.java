package com.qiqia.duosheng.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.custom.CornerTransform;

import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.SmallUtils;

public class ImageBindingAdapter {
    public  static  RequestOptions getImgOptions(){
        return new RequestOptions().placeholder(R.drawable.load_img).error(R.drawable.no_banner);
    }
    //图片加载绑定为：普通图片
    @BindingAdapter("url")
    public static void bindUrl(ImageView view, String imageUrl){
        Glide.with(view)
                .load(imageUrl)
                .into(view);
    }
    //图片加载绑定为:圆形裁剪图片
    @BindingAdapter("imageCircleUrl")
    public static void bindImageCircleUrl(ImageView view, String imageUrl){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.load_img)
                .error(R.drawable.no_banner)
                        .circleCrop();
        Glide.with(view)
                .load(imageUrl)
                .apply(options)
                .into(view);
    }
    //图片加载绑定为:圆角图片,圆角系数
    @BindingAdapter(value = {"imageRoundUrl","roundingRadius"}, requireAll = false)
    public static void bindImageRoundUrl(ImageView view, String imageRoundUrl,int roundingRadius){
        RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.load_img)
                        .error(R.drawable.no_banner)
                        .transform(new RoundedCorners((int) DpPxUtils.dp2pxFloat(roundingRadius==0?9:roundingRadius)));
        Glide.with(view)
                .load(imageRoundUrl)
                .apply(options)
                .into(view);
    }


    /**
     * 上部为圆角的图片样式，
     * @param view ImageView
     * @param imageRoundUrl  要加载的图片url
     * @param roundingRadius 圆角弧度 默认9
     */
    @BindingAdapter(value = {"imageTopRoundUrl","roundingTopRadius"}, requireAll = false)
    public static void bindImageTopRoundUrl(ImageView view, String imageRoundUrl,float roundingRadius){
        CornerTransform transformation = new CornerTransform(SmallUtils.getApp(), DpPxUtils.dp2pxFloat(roundingRadius==0?9:roundingRadius));
        transformation.setExceptCorner(true, true, false, false);
        Glide.with(SmallUtils.getApp()).load(imageRoundUrl).apply(new RequestOptions().transform(transformation)).into(view);
    }
}
