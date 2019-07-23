package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindBarFragment;
import com.qiqia.duosheng.custom.BottomDatePickerPop;
import com.qiqia.duosheng.databinding.FragmentOrderBinding;
import com.qiqia.duosheng.mine.adapter.OrderAdapter;
import com.qiqia.duosheng.mine.bean.OrderBean;
import com.qiqia.duosheng.utils.TabCreateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import cn.com.smallcake_utils.ToastUtil;

public class OrderFragment extends BaseBindBarFragment<FragmentOrderBinding> {

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
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mAdapter = new OrderAdapter(getData());
        mBinding.recyclerView.setAdapter(mAdapter);
        String[] names = getResources().getStringArray(R.array.order_tab_names);
        TabCreateUtils.setOrangeTab(this.getContext(), mBinding.magicIndicator, names, index -> ToastUtil.showShort(names[index]));
        initDatePicker();
        onEvent();
    }

    int page = 1;

    private void onEvent() {
        mAdapter.setOnLoadMoreListener(() -> {
            dialog.show();
            mBinding.recyclerView.postDelayed(() -> {
                dialog.dismiss();
                if (page < 5) {
                    page++;
                    mAdapter.addData(getData());
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.loadMoreEnd();
                }
            }, 300);

        }, mBinding.recyclerView);
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
    public void doClicks(View view) {
        basePopupView.show();
    }

    BasePopupView basePopupView;

    private void initDatePicker() {
        BottomDatePickerPop bottomDatePickerPop = new BottomDatePickerPop(_mActivity);
        bottomDatePickerPop.setListener((year, month) -> tvTitle.setText("订单明细(" + year + "年" + month + "月）"));
        basePopupView = new XPopup.Builder(_mActivity)
                .asCustom(bottomDatePickerPop);
    }
}
