package com.qiqia.duosheng.search;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.GoodsList;
import com.qiqia.duosheng.bean.GoodsListAll;
import com.qiqia.duosheng.bean.SearchKey;
import com.qiqia.duosheng.custom.GoodsSortLinearLayout;
import com.qiqia.duosheng.custom.OnSortClickListener;
import com.qiqia.duosheng.search.adapter.GoodsHAdapter;
import com.qiqia.duosheng.search.adapter.GoodsVAdapter;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.GuideUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.S;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.ToastUtil;

/**
 * 搜索页
 */
public class SearchFragment extends BaseBarFragment implements SwipeRefreshLayout.OnRefreshListener {


    GoodsHAdapter hAdapter = new GoodsHAdapter();
    GoodsVAdapter vAdapter = new GoodsVAdapter();
    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.recycler_view_goods)
    RecyclerView recyclerViewGoods;
    @BindView(R.id.sort_line)
    GoodsSortLinearLayout sortLine;
    @BindView(R.id.recycler_view_goods_grid)
    RecyclerView recyclerViewGoodsGrid;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    public static SearchFragment newInstance(String keyWord) {
        Bundle args = new Bundle();
        args.putString("keyWord", keyWord);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        initView();
        KeyWord = getArguments().getString("keyWord");
        if (!TextUtils.isEmpty(KeyWord)) {
            tvKey.setText(KeyWord);
            search();
        }
    }

    private void initView() {
        hAdapter.bindToRecyclerView(recyclerViewGoods);
        recyclerViewGoods.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerViewGoods.setAdapter(hAdapter);
        recyclerViewGoodsGrid.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recyclerViewGoodsGrid.setAdapter(vAdapter);
        hAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        vAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        refresh.setOnRefreshListener(this);
        onEvent();
    }

    private void onEvent() {
        sortFilterEvent();
        loadMoreSet();
    }


    private void sortFilterEvent() {
        //筛选条件排序
        sortLine.setListener(new OnSortClickListener() {
            @Override
            public void onItemClick(int order) {
                Order = order;
                Pn = 1;
                hAdapter.setNewData(null);
                vAdapter.setNewData(null);
                search();
            }

            @Override
            public void onFilterClick(int lowPrice, int highPrice) {
                if (highPrice > 0) {
                    StartPrice = lowPrice;
                    EndPrice = highPrice;
                }
                Pn = 1;
                hAdapter.setNewData(null);
                vAdapter.setNewData(null);
                search();
            }
        });
    }

    private void loadMoreSet() {
        hAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (totalPage == 0) {
                    search();
                } else if (AllPn < totalPage) {
                    search();
                } else {
                    hAdapter.loadMoreEnd();
                }

            }
        }, recyclerViewGoods);
        vAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                if (totalPage == 0) {
                    search();
                } else if (AllPn < totalPage) {
                    search();
                } else {
                    vAdapter.loadMoreEnd();
                }
            }
        }, recyclerViewGoods);
        hAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                goGoodsInfoFragment(item);
            }
        });
        vAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                goGoodsInfoFragment(item);
            }
        });
    }


    private void switchLayout() {
        String tag = (String) ivRight.getTag();
        if (tag.equals("line")) {
            ivRight.setTag("grid");
            ivRight.setImageResource(R.mipmap.icon_grid_list);
            vAdapter.setNewData(null);
            recyclerViewGoods.setVisibility(View.GONE);
            recyclerViewGoodsGrid.setVisibility(View.VISIBLE);
        } else {
            ivRight.setTag("line");
            ivRight.setImageResource(R.mipmap.icon_line_list);
            hAdapter.setNewData(null);
            recyclerViewGoods.setVisibility(View.VISIBLE);
            recyclerViewGoodsGrid.setVisibility(View.GONE);
        }
        Pn = 1;
        search();
    }


    /**
     * KeyWord	   是	str	关键词
     * Order	   否	int	0.综合（最新），1.券后价(低到高)，2.券后价（高到低），3.券面额（高到低），4.月销量（高到低），5.佣金比例（高到低），6.券面额（低到高），7.月销量（低到高），8.佣金比例（低到高），9.全天销量（高到低），10全天销量（低到高），11.近2小时销量（高到低），12.近2小时销量（低到高），13.优惠券领取量（高到低），14.好单库指数（高到低）
     * Pn	      否	int	分页，用于实现类似分页抓取效果，来源于上次获取后的数据的min_id值，默认开始请求值为1
     * StartPrice 否	int	券后价筛选，筛选大于等于所设置的券后价的商品
     * EndPrice	  否	    int	券后价筛选，筛选小于等于所设置的券后价的商品
     * Cid	      否	int	0全部，1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15车品，16文体
     * Type	      否	int	默认0 搜索类型 0天猫 1京东 2拼多多
     */
    String KeyWord;
    int Order;
    int Pn = 1;
    int AllPn = 1;
    int StartPrice;
    int EndPrice;
    int Cid;
    int Type;
    int totalPage;

    private void search() {
//        if (KeyWord.contains("http")) searchByUrl();
//        else
        searchByWord();

    }

    /**
     * 单个商品链接搜索
     */
    private void searchByUrl() {
        //过滤http前面的文字和点击链接后面的文字,截取出http链接出来
        dataProvider.shop.analysisUrl(KeyWord, Type).subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsList>>() {
            @Override
            protected void onSuccess(BaseResponse<GoodsList> goodsListBaseResponse) {
            }

        });
    }

    /***
     * all不为null,就用all里面的minId
     *
     */
    private void searchByWord() {
        dataProvider.shop.search(KeyWord, Order, Pn, AllPn, StartPrice, EndPrice, Cid, Type)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsListAll>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsListAll> baseResponse) {
                        saveKey(KeyWord);
                        GoodsListAll data = baseResponse.getData();
                        GoodsList all = data.getAll();
                        List<GoodsInfo> results = data.getData();
                        if (all==null&&results==null&&Pn==1){
                            ToastUtil.showShort("暂无相关商品！");
//                            hAdapter.setEmptyView(LayoutInflater.from(_mActivity).inflate(R.layout.fragment_order,null));
                            return;
                        }
                        if (results != null && results.size() > 0) {
                            loadHdkData(results);
                            Pn = data.getMinId();
                        }
                        if (all != null) {
                            List<GoodsInfo> dataAll = all.getData();
                            loadHdkData(dataAll);
                            AllPn = all.getMinId();
                            totalPage = all.getPage();
                            Pn = 0;
                        }
                        showGuide();
                    }

                    @Override
                    protected void onErr(String msg) {
                       S.showShort(SearchFragment.this.getView(),msg);
//                        ToastUtil.showShort(msg);
                        L.e("商品搜索错误=="+msg);
                    }
                });
    }


    private void showGuide() {
        //新用户引导2.搜索商品列表引导
        if (Pn>1)return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = hAdapter.getViewByPosition(recyclerViewGoods, 0, R.id.layout_root);
                if (view != null) GuideUtils.showGuide(SearchFragment.this, view);
            }
        }, 800);
    }

    private void loadHdkData(List<GoodsInfo> results) {
        //如果没有数据，没有更多
        if (results == null || results.size() == 0) {
            ToastUtil.showShort("没有更多相关商品了！");
            String tag = ivRight.getTag().toString();
            if (tag.equals("grid")) {
                vAdapter.loadMoreEnd();
            } else {
                hAdapter.loadMoreEnd();
            }
            return;
        }
        //添加下一页数据
        String tag = ivRight.getTag().toString();
        if (tag.equals("grid")) {
            vAdapter.addData(results);
            vAdapter.loadMoreComplete();
        } else {
            hAdapter.addData(results);
            hAdapter.loadMoreComplete();
        }
    }


    private void saveKey(String key) {
        if (Pn > 1) return;
        if (key.contains("http")) return;//如果是搜索单个商品，不进行关键词保存
        boolean b = new SearchKey.Builder()
                .key(key)
                .time(TimeUtils.getTime())
                .build()
                .saveOrUpdate("key = ?", key);
        L.e("【" + key + "】关键字保存" + (b ? "成功" : "失败"));

    }


    @OnClick({R.id.iv_search, R.id.iv_right, R.id.layout_search})
    void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                search();
                break;
            case R.id.iv_right:
                switchLayout();
                break;
            case R.id.layout_search:
                start(SearchHistoryFragment.newInstance(), SINGLETASK);
                break;
        }
    }

    @Override
    public void onRefresh() {
        String tag = (String) ivRight.getTag();
        Pn = 1;
        if (tag.equals("line")) hAdapter.setNewData(null);
        else vAdapter.setNewData(null);
        search();
    }
}
