package com.qiqia.duosheng.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.IndexTextAd;
import com.qiqia.duosheng.custom.GoodsTypePopWindow;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.main.bean.IndexClassfiy;
import com.qiqia.duosheng.main.hometab.HomeGoodsFragment;
import com.qiqia.duosheng.main.hometab.HomeSelectFragment;
import com.qiqia.duosheng.utils.GuideUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.qiqia.duosheng.utils.TabCreateUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.SPUtils;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.ToastUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_show_all_type)
    ImageView ivShowAllType;
    @BindView(R.id.toolbar_search)
    Toolbar toolbarSearch;
    @BindView(R.id.layout_root)
    CoordinatorLayout layoutRoot;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.layout_tab)
    LinearLayout layoutTab;
    @BindView(R.id.layout_search_red)
    LinearLayout layoutSearchRed;
    @BindView(R.id.tv_look_guide_video)
    TextView tvLookGuideVideo;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.layout_guide)
    LinearLayout layoutGuide;
    @BindView(R.id.tv_index_ad)
    TextView tvIndexAd;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).statusBarColor(R.color.transparent).autoDarkModeEnable(true).init();
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTabClassfiy();//顶部子项分类
        allPopClassfiy();//所有的弹窗分类
        IndexTextAd();//首页底部文字广告
        GuideUtils.showGuide(this, layoutSearchRed);//引导层
    }

    //首页底部文字广告
    private void IndexTextAd() {
        dataProvider.shop.indexTextAd().subscribe(new OnSuccessAndFailListener<BaseResponse<IndexTextAd>>() {
            @Override
            protected void onSuccess(BaseResponse<IndexTextAd> indexTextAdBaseResponse) {
                tvIndexAd.setVisibility(View.VISIBLE);
                IndexTextAd data = indexTextAdBaseResponse.getData();
                String content = data.getContent();
                int time = data.getTime();
                tvIndexAd.setText(TimeUtils.tsToYMD(time) + " : " + content);
                //10秒后隐藏
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideIndexAdwithAnim();
                    }
                }, 10000);
            }

            @Override
            protected void onErr(String msg) {
            }
        });
    }


    /**
     * 隐藏广告，动画结束后完全隐藏
     */
    private void hideIndexAdwithAnim() {
        if (tvIndexAd == null) return;
        tvIndexAd.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvIndexAd.setVisibility(View.GONE);
            }
        }).start();
    }

    /**
     * 顶部Tab分类
     */
    private void initTabClassfiy() {
        //io线程读取本地Tab分类数据
        Observable.create((Observable.OnSubscribe<List<IndexClassfiy>>) subscriber -> {
            List<IndexClassfiy> dataCache = SPUtils.readObject(SPStr.INDEX_CLASSFIY_LIST);
            if (dataCache == null || dataCache.size() == 0) {
                getIndexClassfiyData(true);
                return;
            }
            subscriber.onNext(dataCache);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataCache -> {
                    initTabLayout(dataCache);
                    getIndexClassfiyData(false);
                });
    }
    /**
     * 是否需要添加
     *
     * @param isAdd
     */
    private void getIndexClassfiyData(boolean isAdd) {
        dataProvider.shop.indexClassfiy().subscribe(new OnSuccessAndFailListener<BaseResponse<List<IndexClassfiy>>>() {
            @Override
            protected void onSuccess(BaseResponse<List<IndexClassfiy>> listBaseResponse) {
                List<IndexClassfiy> data = listBaseResponse.getData();
                SPUtils.saveObject(SPStr.INDEX_CLASSFIY_LIST, data);
                if (isAdd) initTabLayout(data);
            }


        });
    }
    /**
     * 弹出POP所有分类
     * io线程读取本地Pop分类数据
     */
    private void allPopClassfiy() {
        //io线程读取本地Tab分类数据
        Observable.create((Observable.OnSubscribe<List<Classfiy>>) subscriber -> {
            List<Classfiy> allClassfiyCache = SPUtils.readObject(SPStr.CLASSFIYS);
            if (allClassfiyCache == null || allClassfiyCache.size() == 0) {
                getPopClassfiy(null);
                return;
            }
            subscriber.onNext(allClassfiyCache);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allClassfiyCache -> {
                    initAllGoodsTypePop(allClassfiyCache);
                    getPopClassfiy(allClassfiyCache);
                });
    }

    int queryIndex;//请求的次数
    private void getPopClassfiy(List<Classfiy> allClassfiyCache) {
        dataProvider.shop.classfiy()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<List<Classfiy>>>() {
                    @Override
                    protected void onSuccess(BaseResponse<List<Classfiy>> listBaseResponse) {
                        List<Classfiy> classfiys = listBaseResponse.getData();
                        SPUtils.saveObject(SPStr.CLASSFIYS, new ArrayList<>(classfiys));
                        if (allClassfiyCache == null) initAllGoodsTypePop(classfiys);
                    }

                    @Override
                    protected void onErr(String msg) {
                        //如果失败再请求一次
                        if (queryIndex < 2) {
                            allPopClassfiy();
                            queryIndex++;
                        }
                    }
                });
    }

    BasePopupView allTypePop;//顶部弹出pop
    private void initAllGoodsTypePop(List<Classfiy> classfiys) {
        if (classfiys == null) {
            classfiys = new ArrayList<>();
        }
        Classfiy startClassfiy = new Classfiy(R.mipmap.img_jx, "精选");
        Classfiy endClassfiy = new Classfiy(R.mipmap.img_all, "全部");
        classfiys.add(0, startClassfiy);
        classfiys.add(classfiys.size(), endClassfiy);
        allTypePop = new XPopup.Builder(getContext())
                .atView(magicIndicator)
                .offsetY(-DpPxUtils.dp2px(32))
                .asCustom(new GoodsTypePopWindow(getContext(), classfiys));
    }

    //顶部横条Tab
    private void initTabLayout(List<IndexClassfiy> classfiys) {
        if (classfiys == null || classfiys.size() == 0) return;
        if (magicIndicator.getChildCount() > 2) return;
        classfiys.add(0, new IndexClassfiy("精选"));
        TabCreateUtils.setDefaultTab2(this.getContext(), magicIndicator,viewPager,classfiys);
        initViewPager(classfiys);
    }

    private void initViewPager(List<IndexClassfiy> classfiys) {
        FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return classfiys.size();
            }

            @Override
            public Fragment getItem(int i) {
                if (i == 0) return HomeSelectFragment.newInstance();
                else return HomeGoodsFragment.newInstance(classfiys.get(i));
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return classfiys.get(position).getMainName();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(classfiys.size());
    }

    @OnClick({R.id.layout_search_red, R.id.iv_show_all_type, R.id.tv_look_guide_video, R.id.tv_index_ad})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.layout_search_red:
                goActivity(WhiteBarActivity.class);
                break;
            case R.id.tv_look_guide_video:
                ToastUtil.showShort("开发中...");
                break;
            case R.id.iv_show_all_type:
                if (allTypePop != null) allTypePop.show();
                break;
            case R.id.tv_index_ad:
                hideIndexAdwithAnim();
                break;
        }
    }
}
