package com.qiqia.duosheng.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
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

/**
 * 疯抢榜单 -- 子页面
 */
public class ItemRankingListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    RankingGoodsAdapter mAdapter = new RankingGoodsAdapter();
    int type = 1, page = 1, cid;

    public static ItemRankingListFragment newInstance(int type,int cid) {
        Bundle args = new Bundle();
        args.putInt("type",type);
        args.putInt("cid",cid);
        ItemRankingListFragment fragment = new ItemRankingListFragment();
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
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        type = getArguments().getInt("type");
        cid = getArguments().getInt("cid");
        getRankingData();
        onEvent();

    }

    private void onEvent() {
        //精选商品
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                jumpToActivity(item);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getRankingData();
            }
        }, recyclerView);
    }
    private void getRankingData() {
        dataProvider.shop.leaderboard(type, page, cid)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsList>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsList> goodsListBaseResponse) {
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
}
