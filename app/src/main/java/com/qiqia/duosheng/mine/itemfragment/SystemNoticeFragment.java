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
import com.qiqia.duosheng.mine.adapter.SystemNoticeAdapter;
import com.qiqia.duosheng.mine.bean.SystemNotice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SystemNoticeFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    SystemNoticeAdapter mAdapter = new SystemNoticeAdapter();
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
    private List<SystemNotice> getData() {
        List<SystemNotice> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new SystemNotice());
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
