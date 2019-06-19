package com.qiqia.duosheng.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.adapter.DayHotAdapter;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import java.util.List;

import butterknife.BindView;
import cn.com.smallcake_utils.ClipboardUtils;

public class DayHotFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    DayHotAdapter dayHotAdapter = new DayHotAdapter();
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @Override
    public int setLayout() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(dayHotAdapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDayHotData();
            }
        });
        getDayHotData();
        onEvent();
    }

    private void onEvent() {
        //每日爆款的分享，和查看详情
        
        dayHotAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_share:
                        goCreateShareFragment(item);
                        break;
                    case R.id.tv_copy_comment:
                        ClipboardUtils.copy(item.getComment());
                        break;
                    case R.id.tv_goods_info_desc:
                        jumpToActivity(item);
                        break;
                }
            }
        });
        dayHotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo item = (GoodsInfo) adapter.getItem(position);
                jumpToActivity(item);
            }
        });
    }

    private void getDayHotData() {
        dataProvider.shop.getDayFieryList()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<List<GoodsInfo>>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<List<GoodsInfo>> listBaseResponse) {
                        dayHotAdapter.setNewData(listBaseResponse.getData());
                    }

                });

    }
}
