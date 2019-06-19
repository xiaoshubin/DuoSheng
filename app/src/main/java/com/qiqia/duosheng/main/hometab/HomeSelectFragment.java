package com.qiqia.duosheng.main.hometab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.WebViewShopActivity;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.GoodsList;
import com.qiqia.duosheng.impl.ShopImpl;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.main.adapter.MainGoodsAdapter;
import com.qiqia.duosheng.main.adapter.MainMenuAdapter;
import com.qiqia.duosheng.main.adapter.RecommandGoodsAdapter;
import com.qiqia.duosheng.main.bean.IndexResponse;
import com.qiqia.duosheng.main.bean.MainTypeGoods;
import com.qiqia.duosheng.main.bean.MenuItem;
import com.qiqia.duosheng.mine.InviteFriendFragment;
import com.qiqia.duosheng.ranking.RankingListFragment;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.BannerUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeSelectFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recycler_view_main_menu)
    RecyclerView recyclerViewMainMenu;
    @BindView(R.id.recycler_view_main_goods)
    RecyclerView recyclerViewMainGoods;
    @BindView(R.id.recycler_view_recommand)
    RecyclerView recyclerViewRecommand;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.banner_middle)
    Banner bannerMiddle;

    RecommandGoodsAdapter recommandGoodsAdapter;
    MainGoodsAdapter mainSectionGoodsAdapter;

    public static HomeSelectFragment newInstance() {
        Bundle args = new Bundle();
        HomeSelectFragment fragment = new HomeSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home_select;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        refresh.setOnRefreshListener(this);
        initView();
        onEvent();
        initMainMenu();
        initBanner();//Banner可以延迟加载
        initMainGoods();//有加载圈
        initMainRecommand();//多次请求


    }



    private void initView() {
        recyclerViewRecommand.setHasFixedSize(true);
        recyclerViewRecommand.setNestedScrollingEnabled(false);
        recyclerViewRecommand.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recommandGoodsAdapter = new RecommandGoodsAdapter();
        recyclerViewRecommand.setAdapter(recommandGoodsAdapter);
        //榜单，推荐，9.9
        recyclerViewMainGoods.setHasFixedSize(true);
        recyclerViewMainGoods.setNestedScrollingEnabled(false);
        recyclerViewMainGoods.setLayoutManager(new LinearLayoutManager(_mActivity));
        mainSectionGoodsAdapter = new MainGoodsAdapter();
        recyclerViewMainGoods.setAdapter(mainSectionGoodsAdapter);

    }

    private void onEvent() {
        //更多点击事件
        mainSectionGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MainTypeGoods item = (MainTypeGoods) adapter.getItem(position);
                switch (item.getHeadTitle()) {
                    case "9.9包邮":
                        goPracticalListFragment(0);
                        break;
                    case "今日人气榜单":
                        try {
                            MainViewPagerFragment.rgBottomTab.check(R.id.rb3);
                            RankingListFragment.tabLayout.getTabAt(1).select();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });
        //商品
        mainSectionGoodsAdapter.setOnItemGoodsClickListener(new MainGoodsAdapter.OnItemGoodsClickListener() {
            @Override
            public void onItemGoodsClick(GoodsInfo item) {
                jumpToActivity(item);
            }
        });
        //精选商品
        recommandGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                jumpToActivity(item);
            }
        });
    }

    /**
     * 底部
     * 为你精选商品部分
     */
    int initMainRecommandIndex;

    private void initMainRecommand() {
        dataProvider.shop.leaderboard(1, 1, 0)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsList>>() {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsList> goodsListBaseResponse) {
                        GoodsList data = goodsListBaseResponse.getData();
                        if (data == null && initMainRecommandIndex < 2) {
                            initMainRecommandIndex++;
                            initMainRecommand();
                            return;
                        }
                        List<GoodsInfo> datas = data.getData();
                        recommandGoodsAdapter.setNewData(datas);
                    }


                });

    }

    /**
     * 中部
     * 今日人气榜单
     * 今日必买推荐
     * 9.9包邮
     */
    private void initMainGoods() {
        dataProvider.shop.index()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<IndexResponse>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<IndexResponse> indexResponseBaseResponse) {
                        IndexResponse data = indexResponseBaseResponse.getData();
                        List<GoodsInfo> hour = data.getHour();
                        List<GoodsInfo> day = data.getDay();
                        List<GoodsInfo> list9 = data.getList9();
                        List<MainTypeGoods> datas = new ArrayList<>();
                        if (day != null)
                            datas.add(new MainTypeGoods(R.mipmap.icon_hot, "今日人气榜单", day));
                        if (hour != null)
                            datas.add(new MainTypeGoods(R.mipmap.icon_rec, "今日必买推荐",hour));
                        if (list9 != null)
                            datas.add(new MainTypeGoods(R.mipmap.icon_sale, "9.9包邮", list9));
                        mainSectionGoodsAdapter.setNewData(datas);
                    }


                });


    }




    private void initMainMenu() {
        int[] icons = new int[]{R.mipmap.icon_tao, R.mipmap.icon_smarket, R.mipmap.icon_import, R.mipmap.icon_fresh, R.mipmap.icon_ju,
                R.mipmap.icon_cart, R.mipmap.icon_hotrank, R.mipmap.icon_big_coupon, R.mipmap.icon_heighcomm, R.mipmap.icon_sales};
        String[] names = getResources().getStringArray(R.array.main_menu_names);
        List<MenuItem> datas = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) datas.add(new MenuItem(icons[i], names[i]));
        recyclerViewMainMenu.setHasFixedSize(true);
        recyclerViewMainMenu.setNestedScrollingEnabled(false);
        recyclerViewMainMenu.setLayoutManager(new GridLayoutManager(_mActivity, 5));
        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter();
        recyclerViewMainMenu.setAdapter(mainMenuAdapter);
        mainMenuAdapter.setNewData(datas);
        mainMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MenuItem item = (MenuItem) adapter.getItem(position);
                if (position < 6) goWebView(position, item);//前六项跳网页
                else switch (position) {
                    case 6://疯抢榜单
                        MainViewPagerFragment.rgBottomTab.check(R.id.rb3);
                        break;
                    case 7://大额券
                        goPracticalListFragment(3);
                        break;
                    case 8://高佣榜
                        goPracticalListFragment(5);
                        break;
                    case 9://白菜价
                        goPracticalListFragment(0);
                        break;

                }


            }
        });

    }

    String[] urls = new String[]{
            "https://h5.m.taobao.com",
            "https://chaoshi.m.tmall.com",
            "https://www.tmall.hk",
            "https://miao.tmall.com",
            "https://jhs.m.taobao.com/m/index.htm",
            "https://main.m.taobao.com/cart/index.html",
    };

    private void goWebView(int position, MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("title", item.getTitle());
        bundle.putString("url", urls[position]);
        goActivity(WebViewShopActivity.class, bundle);
    }

    /**
     * 1 APP首页顶部轮banner,
     * 2 APP首页中部轮banner
     */
    private void initBanner() {
        ShopImpl shop = dataProvider.shop;
        BannerUtils.addImageToBanner(_mActivity, shop, 1, banner);
        BannerUtils.addImageToBanner(_mActivity, shop, 2, bannerMiddle);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).init();
    }

    @Override
    public void onRefresh() {
        initMainGoods();
        initMainRecommand();
    }
    @OnClick({R.id.iv_invite_friends})
    public void doClicks(View view){
        switch (view.getId()){
            case R.id.iv_invite_friends:
            goWhiteBarActivity(InviteFriendFragment.class.getSimpleName());
                break;
        }
    }
}
