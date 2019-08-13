package com.qiqia.duosheng.share;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.adapter.CircleOfFriendsAdapter;
import com.qiqia.duosheng.share.bean.ShareList;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import java.util.List;

import butterknife.BindView;
import cn.com.smallcake_utils.ToastUtil;

/**
 * 朋友圈素材
 */
public class FriendCircleFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    CircleOfFriendsAdapter mAdapter = new CircleOfFriendsAdapter();
    int page=1;
    User user;
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
        user=DataLocalUtils.getUser();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                mAdapter.setNewData(null);
                getShareList();
            }
        });
        getShareList();
        onEvent();
    }

    private void onEvent() {
        //朋友圈素材加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getShareList();
            }
        }, recyclerView);
        //朋友圈素材的分享，和查看详情
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShareList.DataBean item = (ShareList.DataBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_share:
                        getGoodsInfo(item.getItemId());
                        break;
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShareList.DataBean item = (ShareList.DataBean) adapter.getItem(position);
                goGoodsInfoFragment(item.getItemId());
            }
        });
    }

    private void getGoodsInfo(String id) {
        dataProvider.shop.goodsInfo(user.getUid(),id, 0)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsInfo>>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsInfo> listBaseResponse) {
                        GoodsInfo item = listBaseResponse.getData();
                        if (item == null) {
                            ToastUtil.showLong("暂无商品详情信息！");
                            return;
                        }
                        goCreateShareFragment(item);
                    }
                });
    }

    private void getShareList() {
        dataProvider.shop.getShareList(page)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<ShareList>>(refresh) {
                    @Override
                    protected void onSuccess(BaseResponse<ShareList> shareListBaseResponse) {
                        ShareList data = shareListBaseResponse.getData();
                        List<ShareList.DataBean> datas = data.getData();
                        if (datas == null && datas.size() == 0) {
                            mAdapter.loadMoreEnd();
                            return;
                        }
                        mAdapter.addData(datas);
                        mAdapter.loadMoreComplete();
                        page = data.getMinId();
                    }
                });
    }
}
