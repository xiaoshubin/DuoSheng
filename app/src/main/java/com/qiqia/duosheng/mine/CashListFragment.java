package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.mine.adapter.CashListAdapter;
import com.qiqia.duosheng.mine.bean.CashList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CashListFragment extends BaseBarFragment {

    CashListAdapter mAdapter = new CashListAdapter();
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @Override
    public int setLayout() {
        return R.layout.fragment_cash_list;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("提现明细");
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(mAdapter);
        mAdapter.addData(getData());
        onEvent();
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
                        if (page<5){
                            page++;
                            mAdapter.addData(getData());
                            mAdapter.loadMoreComplete();
                        }else {
                            mAdapter.loadMoreEnd();
                        }
                    }
                },300);

            }
        },recyclerView);
    }

    @NotNull
    private List<CashList> getData() {
        List<CashList> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new CashList());
        }
        return datas;
    }
}
