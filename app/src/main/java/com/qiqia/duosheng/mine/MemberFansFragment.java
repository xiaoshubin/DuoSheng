package com.qiqia.duosheng.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.custom.MemberFansSortLinearLayout;
import com.qiqia.duosheng.mine.adapter.MemberFansAdapter;
import com.qiqia.duosheng.mine.bean.MemberFans;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.smallcake_utils.SpannableStringUtils;

public class MemberFansFragment extends BaseBarFragment {
    @BindView(R.id.tv_today_invite)
    TextView tvTodayInvite;
    @BindView(R.id.tv_yestoday_invite)
    TextView tvYestodayInvite;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    MemberFansAdapter mAdapter;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.sort_line)
    MemberFansSortLinearLayout sortLine;
    @BindView(R.id.layout_bar)
    LinearLayout layoutBar;
    Unbinder unbinder;

    @Override
    public int setLayout() {
        return R.layout.fragment_member_fans;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("会员粉丝");
        tvTitle.setTextColor(Color.WHITE);
        ivBack.setImageResource(R.mipmap.icon_backwhite);
        layoutBar.setBackgroundColor(Color.TRANSPARENT);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));


        mAdapter = new MemberFansAdapter(getData(1));
        recyclerView.setAdapter(mAdapter);
        initData();
        onEvent();
    }

    @NotNull
    private List<MemberFans> getData(int type) {
        List<MemberFans> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new MemberFans(type));
        }
        return datas;
    }

    int itemTyp = 1;
    int page;

    private void onEvent() {
        sortLine.setListener(new MemberFansSortLinearLayout.OnSortClickListener() {
            @Override
            public void onItemClick(int order) {

            }

            @Override
            public void onFilterClick(int lowPrice, int highPrice) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                itemTyp = position + 1;
                page = 1;
                mAdapter.setNewData(null);
                List<MemberFans> data = getData(itemTyp);
                mAdapter.setNewData(data);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                            mAdapter.addData(getData(itemTyp));
                            mAdapter.loadMoreComplete();
                        } else {
                            mAdapter.loadMoreEnd();
                        }
                    }
                }, 300);

            }
        }, recyclerView);
    }

    private void initData() {
        tvTodayInvite.setText(getBuilder("今日邀请人数\n\n", "1"));
        tvYestodayInvite.setText(getBuilder("昨日邀请人数\n\n", "3"));

    }

    private SpannableStringBuilder getBuilder(String desc, String num) {
        return SpannableStringUtils.getBuilder(desc)
                .append(num).setForegroundColor(ContextCompat.getColor(_mActivity, R.color.orangered)).setProportion(2f)
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
