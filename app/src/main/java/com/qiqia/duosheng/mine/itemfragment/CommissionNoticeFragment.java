package com.qiqia.duosheng.mine.itemfragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.mine.adapter.CommissionNoticeAdapter;
import com.qiqia.duosheng.mine.bean.CommissionNotice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CommissionNoticeFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    CommissionNoticeAdapter mAdapter = new CommissionNoticeAdapter();
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
        recyclerView.setAdapter(mAdapter);
        mAdapter.addData(getData());
        onEvent();
    }

    @NotNull
    private List<CommissionNotice> getData() {
        List<CommissionNotice> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new CommissionNotice());
        }
        return datas;
    }
    int page=1;
    private void onEvent() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                dialog.show();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (page < 5) {
                            page++;
                            mAdapter.addData(getData());
                            mAdapter.loadMoreComplete();
                        } else {
                            mAdapter.loadMoreEnd();
                        }
                    }
                }, 300);

            }
        }, recyclerView);
    }


}
