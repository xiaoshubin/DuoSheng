package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.custom.BottomDatePickerPop;
import com.qiqia.duosheng.mine.adapter.OrderAdapter;
import com.qiqia.duosheng.mine.bean.OrderBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderFragment extends BaseBarFragment {

    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    OrderAdapter mAdapter;
    @Override
    public int setLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("订单明细");
        ivRight.setImageResource(R.mipmap.day_select_icon);
        ivRight.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mAdapter = new OrderAdapter(getData());
        recyclerView.setAdapter(mAdapter);
        initDatePicker();
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
    private List<OrderBean> getData() {
        List<OrderBean> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new OrderBean());
        }
        return datas;
    }

    @OnClick(R.id.iv_right)
    public void doClicks(View view){
        basePopupView.show();
    }
    BasePopupView basePopupView;
    private void initDatePicker() {
        BottomDatePickerPop bottomDatePickerPop = new BottomDatePickerPop(_mActivity);
        bottomDatePickerPop.setListener(new BottomDatePickerPop.onSelectYearMonth() {
            @Override
            public void selectYearMonth(int year, int month) {
                tvTitle.setText("订单明细("+year+"年"+month+"月）");
            }
        });
        basePopupView = new XPopup.Builder(_mActivity)
                .asCustom(bottomDatePickerPop);

    }


}
