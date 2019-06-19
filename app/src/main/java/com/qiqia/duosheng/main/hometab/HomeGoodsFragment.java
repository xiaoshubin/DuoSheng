package com.qiqia.duosheng.main.hometab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.GoodsList;
import com.qiqia.duosheng.bean.GoodsListAll;
import com.qiqia.duosheng.custom.GoodsSortLinearLayout;
import com.qiqia.duosheng.custom.OffsetGridLayoutManager;
import com.qiqia.duosheng.main.adapter.GoodsTypeAdapter;
import com.qiqia.duosheng.main.adapter.RecommandGoodsAdapter;
import com.qiqia.duosheng.main.bean.IndexClassfiy;
import com.qiqia.duosheng.search.SearchFragment;
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
public class HomeGoodsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "HomeGoodsFragment";
    @BindView(R.id.sort_line)
    GoodsSortLinearLayout sortLine;
    @BindView(R.id.recycler_view_recommand)
    RecyclerView recyclerViewRecommand;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    RecommandGoodsAdapter recommandGoodsAdapter;
    OffsetGridLayoutManager gridLayoutManager;
    String KeyWord;
    int Order;
    int Pn = 1;
    int AllPn = 1;
    int StartPrice;
    int EndPrice;
    int Cid;
    int Type;
    int totalPage;


    public static HomeGoodsFragment newInstance(IndexClassfiy classfiy) {
        Bundle args = new Bundle();
        args.putSerializable("classfiy", classfiy);
        HomeGoodsFragment fragment = new HomeGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home_goods;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        IndexClassfiy classfiy = (IndexClassfiy) getArguments().getSerializable("classfiy");
        KeyWord = classfiy.getMainName();
        Cid = classfiy.getCid();
        initMainRecommand();
        initGoodsType(classfiy.getData());
        searchByWord();
        onEvent();
    }

    private void initMainRecommand() {
        refresh.setOnRefreshListener(this);
//        recyclerViewRecommand.setHasFixedSize(true);
//        recyclerViewRecommand.setNestedScrollingEnabled(false);
        gridLayoutManager = new OffsetGridLayoutManager(_mActivity, 2);
        recyclerViewRecommand.setLayoutManager(gridLayoutManager);
        recommandGoodsAdapter = new RecommandGoodsAdapter();
        recyclerViewRecommand.setAdapter(recommandGoodsAdapter);
    }

    private void initGoodsType(List<IndexClassfiy.DataBean> dataBeans) {
        RecyclerView recyclerViewType = new RecyclerView(_mActivity);
        recyclerViewType.setHasFixedSize(true);
        recyclerViewType.setNestedScrollingEnabled(false);
        recyclerViewType.setLayoutManager(new GridLayoutManager(getContext(), 4));
        GoodsTypeAdapter mAdaper = new GoodsTypeAdapter();
        recyclerViewType.setAdapter(mAdaper);
        mAdaper.setNewData(dataBeans);
        mAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IndexClassfiy.DataBean item = (IndexClassfiy.DataBean) adapter.getItem(position);
                String name = item.getName();
                goWhiteBarActivity(SearchFragment.class.getSimpleName(), "keyWord", name);

            }
        });
        View space = new View(_mActivity);
        space.setBackgroundColor(ContextCompat.getColor(_mActivity, R.color.line_gray));
        space.setMinimumHeight(DpPxUtils.dp2px(8));
        //sortLine
//        GoodsSortLinearLayout goodsSortLinearLayout = (GoodsSortLinearLayout) LayoutInflater.from(_mActivity).inflate(R.layout.goods_sort_linearlayout, null);
//        goodsSortLinearLayout.setVisibility(View.INVISIBLE);
        //bannber
        View viewBanner = LayoutInflater.from(_mActivity).inflate(R.layout.banner_layout, null);
        Banner banner = viewBanner.findViewById(R.id.banner);
//        Banner banner = new Banner(_mActivity);
        banner.setVisibility(View.VISIBLE);
        banner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpPxUtils.dp2px(160)));


        recommandGoodsAdapter.addHeaderView(viewBanner);//1.添加小类目分类列表项
        recommandGoodsAdapter.addHeaderView(recyclerViewType);//1.添加小类目分类列表项
        recommandGoodsAdapter.addHeaderView(space);//2.添加分割间隙
//        recommandGoodsAdapter.addHeaderView(goodsSortLinearLayout);//3.添加排序选择项
        recyclerViewRecommand.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int menuHeight = recyclerViewType.getMeasuredHeight();//顶部菜单高度+间隙高度
                int scrollHeight = gridLayoutManager.getScollYDistance();
                L.e("menuHeight==" + menuHeight + "  scrollHeight==" + scrollHeight);
                sortLine.setVisibility(scrollHeight >= menuHeight ? View.VISIBLE : View.GONE);
            }
        });
        //每页的Banner
        int position = Integer.parseInt("1" + Cid);
        BannerUtils.addImageToBanner(_mActivity, dataProvider.shop, position, banner);

    }


    private void searchByWord() {
        dataProvider.shop.search(KeyWord, Order, Pn, AllPn, StartPrice, EndPrice, Cid, Type)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsListAll>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsListAll> baseResponse) {
                        GoodsListAll data = baseResponse.getData();
                        List<GoodsInfo> results = data.getData();
                        GoodsList all = data.getAll();
                        if (results != null && results.size() > 0) {
                            loadData(results);
                            Pn = data.getMinId();
                        }
                        if (all != null) {
                            List<GoodsInfo> dataAll = all.getData();
                            loadData(dataAll);
                            AllPn = all.getMinId();
                            totalPage = all.getPage();
                            Pn = 0;
                        }
                    }

                });
    }

    private void loadData(List<GoodsInfo> results) {
        //如果没有数据，没有更多
        if (results == null || results.size() == 0) {
            ToastUtil.showShort("没有更多相关商品了！");
            recommandGoodsAdapter.loadMoreEnd();
            return;
        }
        //添加下一页数据
        recommandGoodsAdapter.addData(results);
        recommandGoodsAdapter.loadMoreComplete();
    }

    private void onEvent() {
        //加载更多
        recommandGoodsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (totalPage==0){
                    searchByWord();
                }else if (AllPn<totalPage){
                    searchByWord();
                }else {
                    recommandGoodsAdapter.loadMoreEnd();
                }
            }
        }, recyclerViewRecommand);
        //点击跳转到商品详情页
        recommandGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                jumpToActivity(item);
            }
        });
        //RecyclerView的列表在最上方的时候，SwipeRefreshLayout可以响应下拉刷新,其他时候是由RecyclerView响应向下滑动
        recyclerViewRecommand.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refresh.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        //筛选条件排序
        sortLine.setListener(new GoodsSortLinearLayout.OnSortClickListener() {
            @Override
            public void onItemClick(int order) {
                Order = order;
                Pn = 1;
                recommandGoodsAdapter.setNewData(null);
                searchByWord();
            }

            @Override
            public void onFilterClick(int lowPrice, int highPrice) {
                if (highPrice > 0) {
                    StartPrice = lowPrice;
                    EndPrice = highPrice;
                }
                Pn = 1;
                recommandGoodsAdapter.setNewData(null);
                searchByWord();
            }
        });
    }

    @Override
    public void onRefresh() {
        Pn = 1;
        recommandGoodsAdapter.setNewData(null);
        searchByWord();
    }

}
