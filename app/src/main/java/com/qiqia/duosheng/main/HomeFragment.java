package com.qiqia.duosheng.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.login.LoginService;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.WebViewShopActivity;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.IndexTextAd;
import com.qiqia.duosheng.custom.GoodsTypePopWindow;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.main.bean.IndexClassfiy;
import com.qiqia.duosheng.main.hometab.HomeGoodsFragment;
import com.qiqia.duosheng.main.hometab.HomeSelectFragment;
import com.qiqia.duosheng.mine.NoticeFragment;
import com.qiqia.duosheng.utils.GuideUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.SPUtils;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.ToastUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.layout_search_bar)
    LinearLayout layoutSearchBar;
    @BindView(R.id.iv_show_all_type)
    ImageView ivShowAllType;
    @BindView(R.id.layout_search_msg)
    LinearLayout layoutSearchMsg;
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
    @BindView(R.id.ele_bar)
    View eleBar;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.layout_guide)
    LinearLayout layoutGuide;
    @BindView(R.id.tv_index_ad)
    TextView tvIndexAd;

    //    @Override
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
        initTopShowHide();//顶部操作指南显示和隐藏
        initTabClassfiy();//顶部子项分类
        allPopClassfiy();//所有的弹窗分类
        onEvent();//选择其他页面就隐藏顶部指南
        GuideUtils.showGuide(this, layoutSearchRed);//引导层
        IndexTextAd();//首页底部文字广告
    }
    private void initTopShowHide() {
        float deltaY = DpPxUtils.dp2px(78);//234 Toolbar高度，同时也是搜索栏位移高度
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float barHeight = appBarLayout.getHeight() + verticalOffset;//354
                float dy = barHeight - deltaY;
                dy = dy < 0 ? 0 : dy;
                float alpha = 1 - (dy / deltaY);
                layoutSearchMsg.setAlpha(alpha);
            }
        });

    }

    private void IndexTextAd() {
        dataProvider.shop.indexTextAd().subscribe(new OnSuccessAndFailListener<BaseResponse<IndexTextAd>>() {
            @Override
            protected void onSuccess(BaseResponse<IndexTextAd> indexTextAdBaseResponse) {
                tvIndexAd.setVisibility(View.VISIBLE);
                IndexTextAd data = indexTextAdBaseResponse.getData();
                String content = data.getContent();
                int time = data.getTime();
                tvIndexAd.setText(TimeUtils.tsToYMD(time)+" : "+content);
                //10秒后隐藏
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideIndexAdwithAnim();
                    }
                },10000);
            }
        });
    }

    /**
     * 隐藏广告，动画结束后完全隐藏
     */
    private void hideIndexAdwithAnim() {
        if (tvIndexAd==null)return;
        tvIndexAd.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvIndexAd.setVisibility(View.GONE);
            }
        }).start();
    }

    private void onEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() > 0) {
//                    layoutGuide.setVisibility(View.GONE);
//                } else {
//                    layoutGuide.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 顶部Tab分类
     */
    private void initTabClassfiy() {
        //io线程读取本地Tab分类数据
        Observable.create(new Observable.OnSubscribe<List<IndexClassfiy>>() {
            @Override
            public void call(Subscriber<? super List<IndexClassfiy>> subscriber) {
                List<IndexClassfiy> dataCache = SPUtils.readObject(SPStr.INDEX_CLASSFIY_LIST);
                if (dataCache == null || dataCache.size() == 0) {
                    getIndexClassfiyData(true);
                    return;
                }
                subscriber.onNext(dataCache);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<IndexClassfiy>>() {
                    @Override
                    public void call(List<IndexClassfiy> dataCache) {
                        initTabLayout(dataCache);
                        getIndexClassfiyData(false);
                    }
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
        Observable.create(new Observable.OnSubscribe<List<Classfiy>>() {
            @Override
            public void call(Subscriber<? super List<Classfiy>> subscriber) {
                List<Classfiy> allClassfiyCache = SPUtils.readObject(SPStr.CLASSFIYS);
                if (allClassfiyCache == null || allClassfiyCache.size() == 0) {
                    getPopClassfiy(null);
                    return;
                }
                subscriber.onNext(allClassfiyCache);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Classfiy>>() {
                    @Override
                    public void call(List<Classfiy> allClassfiyCache) {
                        initAllGoodsTypePop(allClassfiyCache);
                        getPopClassfiy(allClassfiyCache);
                    }
                });


    }

    int queryIndex;

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

    BasePopupView allTypePop;

    private void initAllGoodsTypePop(List<Classfiy> classfiys) {
        if (classfiys == null) {
            classfiys = new ArrayList<>();
        }
        Classfiy startClassfiy = new Classfiy(R.mipmap.img_jx, "精选");
        Classfiy endClassfiy = new Classfiy(R.mipmap.img_all, "全部");
        classfiys.add(0, startClassfiy);
        classfiys.add(classfiys.size(), endClassfiy);
        allTypePop = new XPopup.Builder(getContext())
                .atView(layoutSearchBar)
                .asCustom(new GoodsTypePopWindow(getContext(), classfiys));
    }


    private void initTabLayout(List<IndexClassfiy> classfiys) {
        if (classfiys == null || classfiys.size() == 0) return;
        if (tabLayout.getChildCount() > 2) return;
        classfiys.add(0, new IndexClassfiy("精选"));
        for (int i = 0; i < classfiys.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setText(classfiys.get(i).getMainName());
        }
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
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_search, R.id.layout_search_red, R.id.iv_show_all_type, R.id.tv_look_guide_video, R.id.iv_msg,R.id.tv_index_ad})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.layout_search:
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
            case R.id.iv_msg:
                goTransparentBarActivity(NoticeFragment.class.getSimpleName());
                break;
        }

    }


    private void tbMemberLogin() {
        LoginService loginService = MemberSDK.getService(LoginService.class);
        if (loginService != null) {
            loginService.auth(_mActivity, new LoginCallback() {
                @Override
                public void onSuccess(Session session) {
                }

                @Override
                public void onFailure(int code, String msg) {
                }
            });
        }
    }

    private void getCode() {
        String url = "https://oauth.taobao.com/authorize?response_type=code&client_id=" + Contants.ALI_BAICHUAN_APP_KEY + "&redirect_uri=" + Contants.REDIRECT_URI + "&state=1212&view=wap";
        Bundle bundle = new Bundle();
        bundle.putString("title", "淘宝登录授权");
        bundle.putString("url", url);
        goActivity(WebViewShopActivity.class, bundle);
    }

    private void getToken(String code) {
        String url = "https://oauth.taobao.com/token?client_id=" + Contants.ALI_BAICHUAN_APP_KEY + "&client_secret=" + Contants.ALI_BAICHUAN_APP_SECRET + "&grant_type=authorization_code&code=" + code + "redirect_uri=" + Contants.REDIRECT_URI + "&state=1212&view=wap";
        Bundle bundle = new Bundle();
        bundle.putString("title", "淘宝登录授权");
        bundle.putString("url", url);
        goActivity(WebViewShopActivity.class, bundle);
    }

}
