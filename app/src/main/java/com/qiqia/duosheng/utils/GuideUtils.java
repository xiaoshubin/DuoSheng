package com.qiqia.duosheng.utils;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.custom.guidecomponent.GoodsInfoFragmentComponent;
import com.qiqia.duosheng.custom.guidecomponent.HomeFragmentComponent;
import com.qiqia.duosheng.custom.guidecomponent.MineFragmentComponent;
import com.qiqia.duosheng.custom.guidecomponent.OnComponentClickListener;
import com.qiqia.duosheng.custom.guidecomponent.SearchFragmentComponent;

import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SPUtils;

public class GuideUtils {


    public static void showGuide(BaseFragment baseFragment,View view){
        switch (baseFragment.getClass().getSimpleName()){
            case "HomeFragment":
                if (checkFirstCom(SPStr.GUIDE_HOME_FRAGMENT))return;
                layoutListener(baseFragment,view);
                break;
            case "MineFragment":
                if (checkFirstCom(SPStr.GUIDE_MINE_FRAGMENT))return;
                layoutListener(baseFragment,view);
                break;
            case "SearchFragment":
                if (checkFirstCom(SPStr.GUIDE_SEARCH_FRAGMENT))return;
                newUserGuideSearchFragment(baseFragment,view);
                break;
        }
    }

    private static boolean checkFirstCom(String key){
//        SPUtils.put(key, 0);//调试开启，每次都显示
        int  index = (int) SPUtils.get(key, 0);
        return index>0;
    }
    public static void showGuide(BaseFragment baseFragment,View view,View view2){
        if (checkFirstCom(SPStr.GUIDE_GOODSINFO_FRAGMENT))return;
        switch (baseFragment.getClass().getSimpleName()){
            case "GoodsInfoFragment":
                layoutListener(baseFragment,view,view2);
                break;
        }
    }

    static int indexHomeFragment = 0;
    static int indexMineFragment = 0;
    private static void layoutListener(BaseFragment baseFragment, View view){
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                switch (baseFragment.getClass().getSimpleName()){
                    case "HomeFragment":
                        if (indexHomeFragment==0){ newUserGuideHomeFragment(baseFragment,view);indexHomeFragment++;}
                        break;
                    case "MineFragment":
                        if (indexMineFragment==0){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    newUserGuideMineFragment(baseFragment,view);
                                }
                            },800);
                           indexMineFragment++;
                        }
                        break;
                }

            }
        });
    }
    static int indexGoodsInfoFragment = 0;
    private static void layoutListener(BaseFragment baseFragment, View view,View view2){
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                        if (indexGoodsInfoFragment==0){
                            //由于立即绘制高亮区域会有误差，采用延迟一秒绘制，以保证精确定位
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    newUserGuideGoodsInfoFragment(baseFragment,view,view2);
                                }
                            },800);

                            indexGoodsInfoFragment++;
                        }
            }
        });
    }

    /**
     * 新手引导1.首页搜索引导
     */
    private static void newUserGuideHomeFragment(BaseFragment baseFragment, View view) {
        Guide guide;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(DpPxUtils.dp2px(3))
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        L.e("view.getHeight=="+view.getHeight());
        HomeFragmentComponent homeFragmentComponent = new HomeFragmentComponent();
        builder.addComponent(homeFragmentComponent);
        builder.setEnterAnimationId(R.anim.fade_in);
        builder.setExitAnimationId(R.anim.fade_out);
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(baseFragment.getActivity());
        Guide finalGuide = guide;
        String keyWord = "langtian浪天电动牙刷男女成人";
        homeFragmentComponent.setListener(new OnComponentClickListener() {
            @Override
            public void onClick() {
                baseFragment.goSearchFragment(keyWord);
                finalGuide.dismiss();
                SPUtils.put(SPStr.GUIDE_HOME_FRAGMENT, 1);
            }
        });
    }

    /**
     * 新手引导2.搜索列表页
     */
    private static void newUserGuideSearchFragment(BaseFragment baseFragment, View view) {
        Guide guide;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(DpPxUtils.dp2px(-2))
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        SearchFragmentComponent fragmentComponent = new SearchFragmentComponent(view);
        builder.addComponent(fragmentComponent);
        builder.setEnterAnimationId(R.anim.fade_in);
        builder.setExitAnimationId(R.anim.fade_out);
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(baseFragment.getActivity());
        Guide finalGuide = guide;
        fragmentComponent.setListener(new OnComponentClickListener() {
            @Override
            public void onClick() {
                finalGuide.dismiss();
                SPUtils.put(SPStr.GUIDE_SEARCH_FRAGMENT, 1);
            }
        });
        //对生命周期的监听，如果当期Fragment已经关闭，则关系当前的蒙版
        baseFragment.getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                finalGuide.dismiss();
            }
        });
    }
    /**
     * 新手引导3.商品详情页
     */
    private static void newUserGuideGoodsInfoFragment(BaseFragment baseFragment, View view,View view2) {
        Guide guide;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(0)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);

        int dotLineRectHeight = view.getMeasuredHeight() + DpPxUtils.dp2px(8);
        GoodsInfoFragmentComponent fragmentComponent = new GoodsInfoFragmentComponent(dotLineRectHeight);
        builder.addComponent(fragmentComponent);

        builder.setEnterAnimationId(R.anim.fade_in);
        builder.setExitAnimationId(R.anim.fade_out);
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(baseFragment.getActivity());
        Guide finalGuide1 = guide;



        fragmentComponent.setListener(new OnComponentClickListener(){
            @Override
            public void onClick() {
                finalGuide1.dismiss();
                SPUtils.put(SPStr.GUIDE_GOODSINFO_FRAGMENT, 1);
            }
        });



    }
    /**
     * 新手引导4.主页，我的页面
     */
    private static void newUserGuideMineFragment(BaseFragment baseFragment, View view) {
        Guide guide;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(DpPxUtils.dp2px(3.5f))
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        MineFragmentComponent fragmentComponent = new MineFragmentComponent();
        builder.addComponent(fragmentComponent);
        builder.setEnterAnimationId(R.anim.fade_in);
        builder.setExitAnimationId(R.anim.fade_out);
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(baseFragment.getActivity());
        Guide finalGuide = guide;
        fragmentComponent.setListener(new OnComponentClickListener() {
            @Override
            public void onClick() {
                finalGuide.dismiss();
                SPUtils.put(SPStr.GUIDE_MINE_FRAGMENT, 1);
            }
        });

    }


}
