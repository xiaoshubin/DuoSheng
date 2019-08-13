package com.qiqia.duosheng.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
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
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SPUtils;
import cn.com.smallcake_utils.ScreenUtils;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.ToastUtil;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment implements HomeSelectFragment.OnBannerChangeListener{
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

    private List<Classfiy> allClassfiys;//所有分类：包含精选和全部，的弹出pop窗口数据


    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }
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
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        //是全面屏，设置顶部电量栏高度
        if (ScreenUtils.isAllScreenDevice(this.getContext())) {
            int statusHeight = ScreenUtils.getStatusHeight();
            CollapsingToolbarLayout.LayoutParams layoutParams = new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusHeight);
            toolbarSearch.setLayoutParams(layoutParams);
        }
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
        dataProvider.shop.indexTextAd()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<IndexTextAd>>() {
                    @Override
                    protected void onSuccess(BaseResponse<IndexTextAd> indexTextAdBaseResponse) {
                        tvIndexAd.setVisibility(View.VISIBLE);
                        IndexTextAd data = indexTextAdBaseResponse.getData();
                        String content = data.getContent();
                        int time = data.getTime();
                        tvIndexAd.setText(String.format("%s : %s", TimeUtils.tsToYMD(time), content));
                        //10秒后隐藏
                        new Handler().postDelayed(HomeFragment.this::hideIndexAdwithAnim, 10000);
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
     * @param isAdd 是否需要添加数据到当前顶部的TabLayout菜单中
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
     * 弹出POP【所有分类】
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

    private int queryIndex;//请求的次数

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


    private void initAllGoodsTypePop(List<Classfiy> classfiys) {
        if (classfiys == null) {
            classfiys = new ArrayList<>();
        }
        Classfiy startClassfiy = new Classfiy(R.mipmap.img_jx, "精选");
        Classfiy endClassfiy = new Classfiy(R.mipmap.img_all, "全部");
        classfiys.add(0, startClassfiy);
        classfiys.add(classfiys.size(), endClassfiy);
        allClassfiys = classfiys;

    }

    private void showAllGoodsTypePop() {
        if (allClassfiys != null && allClassfiys.size() > 0) new XPopup.Builder(getContext())
                .atView(collapsingToolbarLayout)
                .asCustom(new GoodsTypePopWindow(getContext(), allClassfiys)).show();
    }

    //顶部横条Tab
    private void initTabLayout(List<IndexClassfiy> classfiys) {
        if (classfiys == null || classfiys.size() == 0) return;
        if (magicIndicator.getChildCount() > 2) return;
        classfiys.add(0, new IndexClassfiy("精选"));
        TabCreateUtils.setDefaultTab2(this.getContext(), magicIndicator, viewPager, classfiys);
        //延迟处理，可以让用户更快的看到首页的Tab选项栏
        new Handler().postDelayed(() -> initViewPager(classfiys), 1);

    }

    /**
     * 【1108ms】 -->viewPager.setOffscreenPageLimit(classfiys.size()【840ms】延迟处理后，此方法变为【180ms】
     * 默认缓存2个页面，所以getItem方法返回的第三个页面会在设置了此方法后的大概800ms后返回打印
     */
    @DebugLog
    private void initViewPager(List<IndexClassfiy> classfiys) {

        FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager(), 0) {
            @Override
            public int getCount() {
                return classfiys.size();
            }

            @Override //180ms
            public Fragment getItem(int i) {
//                L.e(i + "t==" + System.currentTimeMillis());
                if (i == 0){
                    HomeSelectFragment homeSelectFragment = HomeSelectFragment.newInstance();
                    homeSelectFragment.setListener(HomeFragment.this);
                    return homeSelectFragment;
                }
                else return HomeGoodsFragment.newInstance(classfiys.get(i));
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return classfiys.get(position).getMainName();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);

        new Handler().postDelayed(() -> {
            viewPager.setOffscreenPageLimit(classfiys.size());//耗时840ms
        }, 800);
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
                showAllGoodsTypePop();
                break;
            case R.id.tv_index_ad:
                hideIndexAdwithAnim();
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        L.e("banner=="+position);
//        if (position%2==0) ObjectAnimUtils.transColor(layoutTab);
//        else ObjectAnimUtils.transColor2(layoutTab);
    }
}
