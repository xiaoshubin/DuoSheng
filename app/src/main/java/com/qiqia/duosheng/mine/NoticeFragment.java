package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.mine.itemfragment.CommissionNoticeFragment;
import com.qiqia.duosheng.mine.itemfragment.SystemNoticeFragment;

import butterknife.BindView;
import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.ScreenUtils;

public class NoticeFragment extends BaseBarFragment {

    @BindView(R.id.layout_bar)
    LinearLayout layoutBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @Override
    public int setLayout() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("系统消息");
        initViewPager();
    }
    private void initViewPager() {
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }
            @Override
            public Fragment getItem(int i) {
                return i==0?new CommissionNoticeFragment():new SystemNoticeFragment();
            }
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return position==0?"我的佣金": "系统消息";
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        int tabWidth = DpPxUtils.px2dp(ScreenUtils.getScreenWidth()) / 2;
        tabLayout.setTabWidth(tabWidth);
        tabLayout.setViewPager(viewPager);
    }
}
