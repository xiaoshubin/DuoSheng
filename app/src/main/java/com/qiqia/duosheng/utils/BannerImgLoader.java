package com.qiqia.duosheng.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.qiqia.duosheng.R;
import com.youth.banner.loader.ImageLoader;

public class BannerImgLoader extends ImageLoader {
    @SuppressLint("CheckResult")
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.transform(new RoundedCorners(1));
        options.placeholder(R.drawable.load_banner);
        options.error(R.drawable.no_banner);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(path)
                .apply(options)
                .thumbnail(0.1f)
                .into(imageView);
    }

}
