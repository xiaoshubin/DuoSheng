package com.qiqia.duosheng.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.interfaces.XPopupImageLoader;

import java.io.File;

import cn.com.smallcake_utils.FileUtils;

public class BigPicPopImageLoader implements XPopupImageLoader {
    @Override
    public void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView) {
        Glide.with(imageView).load(uri).into(imageView);
    }

    @Override
    public File getImageFile(@NonNull Context context, @NonNull Object uri) {
        try {
            if (uri instanceof Bitmap){
                return FileUtils.saveImage((Bitmap) uri);
            }
            return Glide.with(context).downloadOnly().load(uri).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
