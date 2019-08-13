package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.GoodsList;
import com.qiqia.duosheng.ranking.adapter.RankingGoodsAdapter;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import java.util.List;

import butterknife.BindView;
//榜单里面，tab-->女装，男装等子页面
public class ItemPracticalListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private RankingGoodsAdapter mAdapter = new RankingGoodsAdapter(false);
    private int type = 1, page = 1, cid,sort,startPrice,endPrice;

    public static ItemPracticalListFragment newInstance(int type,int cid, int sort) {
        Bundle args = new Bundle();
        args.putInt("type",type);
        args.putInt("cid",cid);
        args.putInt("sort",sort);
        ItemPracticalListFragment fragment = new ItemPracticalListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).statusBarColor(R.color.transparent).autoDarkModeEnable(true).init();
    }
    @Override
    public int setLayout() {
        return R.layout.fragment_item_ranking_list;
    }
    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        refresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        assert getArguments() != null;
        type = getArguments().getInt("type");
        cid = getArguments().getInt("cid");
        sort = getArguments().getInt("sort");
        getRankingData();
        onEvent();

    }

    private void onEvent() {
        //精选商品
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsInfo item = (GoodsInfo) adapter.getItem(position);
            goGoodsInfoFragmentByActivity(item);
        });
        mAdapter.setOnLoadMoreListener(this::getRankingData, recyclerView);

    }
    private void getRankingData() {
        dataProvider.shop.practicalList(type, cid,page,sort,startPrice,endPrice)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsList>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsList> goodsListBaseResponse)  {
                        GoodsList data = goodsListBaseResponse.getData();
                        if (data == null) { mAdapter.loadMoreEnd();return; }
                        List<GoodsInfo> datas = data.getData();
                        mAdapter.addData(datas);
                        mAdapter.loadMoreComplete();
                        page = data.getMinId();
                    }

                });
    }
    @Override
    public void onRefresh() {
        page = 1;
        mAdapter.setNewData(null);
        getRankingData();
    }

    /**
     * @param sort 外层筛选回调
     */
     void onRefreshByFilter(int sort) {
        this.sort = sort;
        page = 1;
        mAdapter.setNewData(null);
        getRankingData();
    }
     void onRefreshByFilter(int startPrice,int endPrice) {
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        page = 1;
        mAdapter.setNewData(null);
        getRankingData();
    }
}
