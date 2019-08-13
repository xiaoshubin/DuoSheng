package com.qiqia.duosheng.share;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.utils.TabCreateUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分享赚钱
 */
public class ShareFragment extends BaseBarFragment {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    String[] tabNames = {"每日爆款", "朋友圈素材"};

    public static ShareFragment newInstance() {
        Bundle args = new Bundle();
        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_share;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("分享赚钱");
        initViewPager();
    }

    private void initViewPager() {
        TabCreateUtils.setOrangeTab(this.getContext(),magicIndicator,viewPager, tabNames);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }
            @Override
            public Fragment getItem(int i) {
                return i == 0 ? new DayHotFragment() : new FriendCircleFragment();
            }
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabNames[position];
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).init();
    }
    @OnClick({R.id.iv_back})
    public void doClicks(View view) {
        MainViewPagerFragment.rgBottomTab.check(R.id.rb3);
    }
}
