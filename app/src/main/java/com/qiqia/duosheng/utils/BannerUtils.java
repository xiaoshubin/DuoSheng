package com.qiqia.duosheng.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qiqia.duosheng.activities.WebViewShopActivity;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.bean.BannerResponse;
import com.qiqia.duosheng.impl.ShopImpl;
import com.qiqia.duosheng.search.GoodsInfoFragment;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import cn.com.smallcake_utils.ToastUtil;

public class BannerUtils {
    public static void addImageToBanner(Activity activity, ShopImpl shop, int position, Banner banner) {
       shop.banner(position).subscribe(new OnSuccessAndFailListener<BaseResponse<List<BannerResponse>>>() {
           @Override
           protected void onSuccess(BaseResponse<List<BannerResponse>> listBaseResponse)  {
               loadImage(activity,listBaseResponse.getData(),banner);
           }
           @Override
           protected void onErr(String msg) {
             if (banner!=null)banner.setVisibility(View.GONE);
           }
       });

    }

    /**
     * type
     * 0	‘’	无
     * 1	商品id	跳商品详情
     * 2	链接	webview
     * 3	专题id	跳专题详情
     * @param activity
     * @param datas
     * @param banner
     */
    private static void loadImage(Activity activity, List<BannerResponse> datas, Banner banner) {
        if (datas==null||datas.size()==0){ banner.setVisibility(View.GONE);return; }
        banner.setVisibility(View.VISIBLE);
        List<String> images = new ArrayList<>();
        for (BannerResponse bean : datas) images.add(bean.getImg());
        banner.setImageLoader(new BannerImgLoader());
        banner.setDelayTime(5000);
        banner.setImages(images);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerResponse item = datas.get(position);
                String value = item.getValue();
                switch (item.getType()){
                    case 0:
                        break;
                    case 1:
                        goGoodsInfoFragment(activity,value);
                        break;
                    case 2:
                        goWebActivity(activity,item.getTitle(),value);
                        break;
                    case 3:
                        ToastUtil.showLong("专题开发中....");
                        break;
                }

            }
        });
    }

    private static void goGoodsInfoFragment(Activity activity,String id){
        Intent intent = new Intent(activity, WhiteBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, GoodsInfoFragment.class.getSimpleName());
        intent.putExtra("id",id);
        activity.startActivity(intent);
    }

    private static void goWebActivity(Activity activity, String title,String url){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("url", url);
        Intent intent = new Intent(activity, WebViewShopActivity.class);
        intent.putExtra("bundle",bundle);
        activity.startActivity(intent);
    }
}
