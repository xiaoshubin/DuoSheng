package com.qiqia.duosheng.main.hometab;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.GoodsList;
import com.qiqia.duosheng.bean.GoodsListAll;
import com.qiqia.duosheng.custom.GoodsSortLinearLayout;
import com.qiqia.duosheng.custom.GoodsSortLinearLayoutFixed;
import com.qiqia.duosheng.custom.OnSortClickListener;
import com.qiqia.duosheng.main.adapter.GoodsTypeAdapter;
import com.qiqia.duosheng.main.bean.IndexClassfiy;
import com.qiqia.duosheng.search.SearchFragment;
import com.qiqia.duosheng.search.adapter.GoodsVAdapter;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.BannerUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.youth.banner.Banner;

import java.util.List;

import butterknife.BindView;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;

/**
 * 女装，男装子项商品列表
 */
public class HomeGoodsFragment2 extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.sort_line)
    GoodsSortLinearLayout sortLine;//滑动超过一定高度的悬浮排序器
    @BindView(R.id.recycler_view_recommand)
    RecyclerView recyclerViewRecommand;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private GoodsVAdapter mAdapter;
    private String KeyWord;
    private int Order;
    private int Pn = 1;
    private int AllPn = 1;
    private int StartPrice;
    private int EndPrice;
    private int Cid;
    private int totalPage;
    private GoodsSortLinearLayoutFixed sortLineFixed;//RecyclerView中的固定的排序器


    public static HomeGoodsFragment2 newInstance(IndexClassfiy classfiy) {
        Bundle args = new Bundle();
        args.putSerializable("classfiy", classfiy);
        HomeGoodsFragment2 fragment = new HomeGoodsFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home_goods2;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        assert getArguments() != null;
        IndexClassfiy classfiy = (IndexClassfiy) getArguments().getSerializable("classfiy");
        assert classfiy != null;
        KeyWord = classfiy.getMainName();
        Cid = classfiy.getCid();

        initMainRecommand();
        initGoodsType(classfiy.getData());
        searchByWord(false);
        onEvent();
    }

    private void initMainRecommand() {
        refresh.setOnRefreshListener(this);
        //获取RecyclerView滚动距离，来显示和隐藏排序Tab
        recyclerViewRecommand.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        mAdapter = new GoodsVAdapter();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclerViewRecommand.setAdapter(mAdapter);
    }

    @SuppressLint("InflateParams")
    private void initGoodsType(List<IndexClassfiy.DataBean> dataBeans) {
        //bannber
        @SuppressLint("InflateParams") View viewBanner = LayoutInflater.from(_mActivity).inflate(R.layout.banner_layout, null);
        Banner banner = viewBanner.findViewById(R.id.banner);
        banner.setVisibility(View.VISIBLE);

        //二级分类菜单栏
        RecyclerView recyclerViewType = new RecyclerView(_mActivity);
        recyclerViewType.setBackgroundColor(Color.WHITE);
        recyclerViewType.setHasFixedSize(true);
        recyclerViewType.setNestedScrollingEnabled(false);
        recyclerViewType.setLayoutManager(new GridLayoutManager(getContext(), 4));
        GoodsTypeAdapter menuAdapter = new GoodsTypeAdapter();
        recyclerViewType.setAdapter(menuAdapter);
        menuAdapter.setNewData(dataBeans);

        //分割线
        View space = new View(_mActivity);
        space.setBackgroundColor(ContextCompat.getColor(_mActivity, R.color.line_gray));
        space.setMinimumHeight(DpPxUtils.dp2px(8));
        //排序
        sortLineFixed = (GoodsSortLinearLayoutFixed) LayoutInflater.from(_mActivity).inflate(R.layout.goods_sort_tab_fixed, null);

        mAdapter.addHeaderView(viewBanner);//0.添加banner
        mAdapter.addHeaderView(recyclerViewType);//1.添加小类目分类列表项
        mAdapter.addHeaderView(space);//2.添加分割间隙
        mAdapter.addHeaderView(sortLineFixed);//3.添加固定排序

        //显示排序置顶：需要等待banner是否显示后来定位高度隐藏和显示排序器
        BannerUtils.addImageToBanner(_mActivity, dataProvider.shop, Integer.parseInt("1" + Cid), banner, new BannerUtils.onShowBannerListener() {
            int scrollHeight;
            @Override
            public void show() {
                recyclerViewRecommand.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {//dy <0 表示 下滑， dy>0 表示上滑
                        super.onScrolled(recyclerView, dx, dy);
//                        int scrollHeight = gridLayoutManager.getScollYDistance();
                         scrollHeight += dy;
                        int menuHeight = recyclerViewType.getMeasuredHeight()+banner.getMeasuredHeight()+space.getMeasuredHeight();//顶部菜单高度+banner高度+间隙高度
                        L.e("menuHeight=="+menuHeight+"  scrollHeight=="+scrollHeight+"  dy=="+dy);
                        boolean upShow = scrollHeight >= menuHeight&&dy>0;
                        boolean downHide = scrollHeight < menuHeight&&dy<0;
                        if (upShow){
                            sortLine.setVisibility(View.VISIBLE);
                        }else if (downHide){
                            sortLine.setVisibility(View.GONE);
                        }
                    }
                });
            }
            @Override
            public void hide() {
                recyclerViewRecommand.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {//dy <0 表示 下滑， dy>0 表示上滑
                        super.onScrolled(recyclerView, dx, dy);
                        scrollHeight += dy;
                        int menuHeight = recyclerViewType.getMeasuredHeight()+space.getMeasuredHeight();//顶部菜单高度+间隙高度
                        L.e("menuHeight=="+menuHeight+"  scrollHeight=="+scrollHeight+"  dy=="+dy);
                        boolean upShow = scrollHeight >= menuHeight&&dy>0;
                        boolean downHide = scrollHeight < menuHeight&&dy<0;
                        if (upShow){
                            sortLine.setVisibility(View.VISIBLE);
                        }else if (downHide){
                            sortLine.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        //二级分类点击事件
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            IndexClassfiy.DataBean item = (IndexClassfiy.DataBean) adapter.getItem(position);
            assert item != null;
            String name = item.getName();
            goWhiteBarActivity(SearchFragment.class.getSimpleName(), "keyWord", name);
        });


    }

    /**
     * @param needClean 是否需要清除数据后请求
     */
    private void searchByWord(boolean needClean) {
        //默认
        int type = 0;//默认0 搜索类型 0天猫 1京东 2拼多多
        dataProvider.shop.search(KeyWord, Order, Pn, AllPn, StartPrice, EndPrice, Cid, type)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsListAll>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsListAll> baseResponse) {
                        GoodsListAll data = baseResponse.getData();
                        List<GoodsInfo> results = data.getData();
                        GoodsList all = data.getAll();
                        if (results != null && results.size() > 0) {
                            loadData(results,needClean);
                            Pn = data.getMinId();
                        }
                        if (all != null) {
                            List<GoodsInfo> dataAll = all.getData();
                            loadData(dataAll, needClean);
                            AllPn = all.getMinId();
                            totalPage = all.getPage();
                            Pn = 0;
                        }
                    }
                    @Override
                    protected void onErr(String msg) {
                        if (msg.contains("HTTP 500")) mAdapter.loadMoreEnd();
                    }
                });
    }

    private void loadData(List<GoodsInfo> results, boolean needClean) {
        //如果没有数据，没有更多
        if (results == null || results.size() == 0) {
            ToastUtil.showShort("没有更多相关商品了！");
            mAdapter.loadMoreEnd();
            return;
        }
        //添加下一页数据
        if (needClean) mAdapter.setNewData(null);
        mAdapter.addData(results);
        mAdapter.loadMoreComplete();
    }

    private void onEvent() {
        //加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            if (totalPage==0){
                searchByWord(false);
            }else if (AllPn<totalPage){
                searchByWord(false);
            }else {
                mAdapter.loadMoreEnd();
            }
        }, recyclerViewRecommand);
        //点击跳转到商品详情页
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsInfo item = (GoodsInfo) adapter.getItem(position);
            goGoodsInfoFragmentByActivity(item);
        });
        //RecyclerView的列表在最上方的时候，SwipeRefreshLayout可以响应下拉刷新,其他时候是由RecyclerView响应向下滑动
        recyclerViewRecommand.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refresh.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        OnSortClickListener onSortClickListener = new OnSortClickListener() {
            @Override
            public void onItemClick(int order) {
//                if (order == 14 || order == 5 || order == 8) { //综合，默认
//
//                }
                Order = order;
                Pn = 1;
                searchByWord(true);
            }

            @Override
            public void onFilterClick(int lowPrice, int highPrice) {
                if (highPrice > 0) {
                    StartPrice = lowPrice;
                    EndPrice = highPrice;
                }
                Pn = 1;
                searchByWord(true);
            }
        };
        //悬浮：筛选条件排序
        sortLine.setListener(onSortClickListener);
        //固定：筛选条件排序
        sortLineFixed.setListener(onSortClickListener);
        //综合弹出事件：showCom需要每次从新创建Pop，定位才会准确
        sortLineFixed.layoutCom.setOnClickListener(v -> {
            sortLineFixed.showCom();
        });
        //价格弹出事件：showPriceSelect需要每次从新创建Pop，定位才会准确
        sortLineFixed.layoutSelect.setOnClickListener(v -> {
            sortLineFixed.showSelect();
        });
    }

    @Override
    public void onRefresh() {
        Pn = 1;
        searchByWord(true);
    }

}
