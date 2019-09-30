package com.qiqia.duosheng.share;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.adapter.DayHotAdapter;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import java.util.List;

import butterknife.BindView;
import cn.com.smallcake_utils.ClipboardUtils;

//每日爆款
public class DayHotFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    DayHotAdapter mAdapter = new DayHotAdapter();

    @Override
    public int setLayout() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {}

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        refresh.setOnRefreshListener(() -> getDayHotData());
        getDayHotData();
        onEvent();
    }

    private void onEvent() {
        //每日爆款的分享，和查看详情
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            GoodsInfo item = (GoodsInfo) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.tv_share:
                    goCreateShareFragment(item);
                    break;
                case R.id.tv_copy_comment:
                    ClipboardUtils.copy(item.getComment());
                    break;
                case R.id.tv_goods_info_desc:
                    goGoodsInfoFragmentByActivity(item);
                    break;
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsInfo item = (GoodsInfo) adapter.getItem(position);
            goGoodsInfoFragmentByActivity(item);
        });
    }
    private void getDayHotData() {
        dataProvider.shop.getDayFieryList()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<List<GoodsInfo>>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<List<GoodsInfo>> listBaseResponse) {
                        mAdapter.setNewData(listBaseResponse.getData());
                    }
                });

    }
}
