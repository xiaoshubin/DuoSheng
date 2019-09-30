package com.qiqia.duosheng.main.hometab;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.WebViewShopActivity;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.bean.GoodsList;
import com.qiqia.duosheng.impl.ShopImpl;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.main.adapter.MainGoodsAdapter;
import com.qiqia.duosheng.main.adapter.MainMenuAdapter;
import com.qiqia.duosheng.main.bean.IndexResponse;
import com.qiqia.duosheng.main.bean.MainTypeGoods;
import com.qiqia.duosheng.main.bean.MenuItem;
import com.qiqia.duosheng.mine.InviteFriendFragment;
import com.qiqia.duosheng.ranking.RankingListFragment;
import com.qiqia.duosheng.search.adapter.GoodsVAdapter;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.BannerUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import hugo.weaving.DebugLog;

public class HomeSelectFragment extends BaseFragment {

    @BindView(R.id.recycler_view_recommand)
    RecyclerView recyclerViewRecommand;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private Banner banner;
    private Banner bannerMiddle;
    private RecyclerView recyclerViewMainMenu;
    private RecyclerView recyclerViewMainGoods;
    private GoodsVAdapter recommandGoodsAdapter;//底部推荐
    private MainGoodsAdapter mainSectionGoodsAdapter;//中间今日人气，今日推荐，9.9

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
        refresh.setOnRefreshListener(() -> {
            getMiddleGoods();
            page = 1;
            recommandGoodsAdapter.setNewData(null);
            getRecommonGoods();
        });
        initView();


    }
    View emptyView;
    @DebugLog//耗时80ms
    private void initView() {
        //头部
        View headerView = LayoutInflater.from(this.getContext()).inflate(R.layout.home_first_page_header, null);
        banner = headerView.findViewById(R.id.banner);
        bannerMiddle = headerView.findViewById(R.id.banner_middle);
        recyclerViewMainMenu = headerView.findViewById(R.id.recycler_view_main_menu);
        recyclerViewMainGoods = headerView.findViewById(R.id.recycler_view_main_goods);
        headerView.findViewById(R.id.iv_invite_friends).setOnClickListener((v -> goWhiteBarActivity(InviteFriendFragment.class.getSimpleName())));
        //屏蔽到banner的焦点获取事件，避免因为滚动后获取焦点而自动滑动到banner位置
        banner.setFocusable(false);
        banner.setFocusableInTouchMode(false);
        bannerMiddle.setFocusable(false);
        bannerMiddle.setFocusableInTouchMode(false);
        //底部为你精选商品
        recyclerViewRecommand.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recommandGoodsAdapter = new GoodsVAdapter();
        //空布局文件
        emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.empty_view, null);
        View loadView = LayoutInflater.from(_mActivity).inflate(R.layout.loading_view, null);
        recommandGoodsAdapter.setEmptyView(loadView);
        emptyView.setOnClickListener(v -> {
            getMiddleGoods();
            page = 1;
            recommandGoodsAdapter.setNewData(null);
            getRecommonGoods();
        });

        recommandGoodsAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclerViewRecommand.setAdapter(recommandGoodsAdapter);
        //添加头部
        recommandGoodsAdapter.addHeaderView(headerView);
        //榜单，推荐，9.9
        recyclerViewMainGoods.setHasFixedSize(true);
        recyclerViewMainGoods.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        recyclerViewMainGoods.setLayoutManager(linearLayoutManager);
        mainSectionGoodsAdapter = new MainGoodsAdapter();
        recyclerViewMainGoods.setAdapter(mainSectionGoodsAdapter);

        onEvent();//跳转点击事件
        initMainMenu();//主页八个固定菜单
        initBanner();
        //延迟加载处理商品
        new Handler().postDelayed(this::getRecommonGoods,300);
        new Handler().postDelayed(this::getMiddleGoods,800);


    }

    private void onEvent() {
        //首页推荐商品：更多点击事件
        mainSectionGoodsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MainTypeGoods item = (MainTypeGoods) adapter.getItem(position);
            assert item != null;
            switch (item.getHeadTitle()) {
                case "9.9包邮":
                    goPracticalListFragment(0);
                    break;
                case "今日人气榜单":
                    try {
                        MainViewPagerFragment.rgBottomTab.check(R.id.rb3);
                        RankingListFragment.viewPager.setCurrentItem(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });
        //首页推荐商品：商品点击事件
        mainSectionGoodsAdapter.setOnItemGoodsClickListener(this::goGoodsInfoFragmentByActivity);
        //为你精选商品:商品点击事件
        recommandGoodsAdapter.setOnItemClickListener((adapter, view, position) ->
                goGoodsInfoFragmentByActivity((GoodsInfo) adapter.getItem(position))
        );
        //为你精选商品:加载更多商品事件
        recommandGoodsAdapter.setOnLoadMoreListener(this::getRecommonGoods, recyclerViewRecommand);
    }

    /**
     * 底部
     * 为你精选商品部分
     */
    private int page = 1;
    private void getRecommonGoods() {
        dataProvider.shop.leaderboard(1, page, 0)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsList>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsList> goodsListBaseResponse) {
                        GoodsList data = goodsListBaseResponse.getData();
                        if (data == null) {
                            recommandGoodsAdapter.loadMoreEnd();
                            return;
                        }
                        List<GoodsInfo> datas = data.getData();
                        recommandGoodsAdapter.addData(datas);
                        recommandGoodsAdapter.loadMoreComplete();
                        page = data.getMinId();
                    }

                    @Override
                    protected void onErr(String err) {
                        recommandGoodsAdapter.setEmptyView(emptyView);
                    }
                });

    }

    /**
     * 中部
     * 今日人气榜单
     * 今日必买推荐
     * 9.9包邮
     */
    @DebugLog
    private void getMiddleGoods() {
        dataProvider.shop.index()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<IndexResponse>>() {
                    @Override
                    protected void onSuccess(BaseResponse<IndexResponse> indexResponseBaseResponse) {
                        IndexResponse data = indexResponseBaseResponse.getData();
                        List<GoodsInfo> hour = data.getHour();
                        List<GoodsInfo> day = data.getDay();
                        List<GoodsInfo> list9 = data.getList9();
                        List<MainTypeGoods> datas = new ArrayList<>();
                        if (day != null) datas.add(new MainTypeGoods(R.mipmap.icon_hot, "今日人气榜单", day));
                        if (hour != null) datas.add(new MainTypeGoods(R.mipmap.icon_rec, "今日必买推荐", hour));
                        if (list9 != null) datas.add(new MainTypeGoods(R.mipmap.icon_sale, "9.9包邮", list9));
                        mainSectionGoodsAdapter.setNewData(datas);
                    }

                    @Override
                    protected void onErr(String msg) {

                    }
                });


    }
    //八大菜单项
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
        mainMenuAdapter.setOnItemClickListener((adapter, view, position) -> {
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
//                    goPracticalListFragment(0);
                    goTaoAuth();
                    break;
            }
        });
    }

    private void goTaoAuth(){
        String title = "淘宝授权";
        String url = "https://oauth.taobao.com/authorize?response_type=code&client_id="+ Contants.ALI_BAICHUAN_APP_KEY+"&redirect_uri=http://www.zaoxingwu.cn/v4.php?ctr=App_Profit.code&state=1212&view=wap";
        goWebViewFragment(title,url);
    }

   private String[] urls = new String[]{
            "https://www.taobao.com",
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
    //页面停止时，停止banner滚动，降低性能消耗
    @Override
    public void onPause() {
        super.onPause();
        if (banner!=null)banner.stopAutoPlay();
        if (bannerMiddle!=null)bannerMiddle.stopAutoPlay();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (banner!=null)banner.startAutoPlay();
        if (bannerMiddle!=null)bannerMiddle.startAutoPlay();
    }

    /**
     * 1 APP首页顶部轮banner,
     * 2 APP首页中部轮banner
     */
    OnBannerChangeListener listener;
    public interface OnBannerChangeListener {
        void onPageSelected(int position);
    }

    public void setListener(OnBannerChangeListener listener) {
        this.listener = listener;
    }
    private void initBanner() {
        ShopImpl shop = dataProvider.shop;
        BannerUtils.addImageToBanner(_mActivity, shop, 1, banner);
        BannerUtils.addImageToBanner(_mActivity, shop, 2, bannerMiddle);

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (listener!=null)listener.onPageSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
