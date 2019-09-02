package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.classfiy.SearchCouponFragment;
import com.qiqia.duosheng.login.LoginFragment;
import com.qiqia.duosheng.mine.MineFragment;
import com.qiqia.duosheng.ranking.RankingListFragment;
import com.qiqia.duosheng.share.ShareFragment;
import com.qiqia.duosheng.utils.DataLocalUtils;

import butterknife.BindView;
import cn.com.smallcake_utils.ToastUtil;

public class MainViewPagerFragment extends BaseFragment {
    public static RadioGroup rgBottomTab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @Override
    public int setLayout() {
        return R.layout.fragment_main_viewpager;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        rgBottomTab = view.findViewById(R.id.rg_bottom_tab);
//        goWebViewFragment("js测试","js");

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initViewPager();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).init();
    }

    private void initViewPager() {
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(_mActivity.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0: return HomeFragment.newInstance();
                    case 1: return SearchCouponFragment.newInstance();
                    case 2: return RankingListFragment.newInstance();
                    case 3: return ShareFragment.newInstance();
                    case 4: return MineFragment.newInstance();
                }
                return HomeFragment.newInstance();
            }

        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        rgBottomTab.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb1:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.rb2:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.rb3:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.rb4:
                    if (!DataLocalUtils.isLogin()) {
                        ToastUtil.showShort("请先登录！");
                        goWhiteBarActivity(LoginFragment.class.getSimpleName());
                        rgBottomTab.check(R.id.rb1);
                        return;
                    }
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.rb5:
                    if (!DataLocalUtils.isLogin()) {
                        ToastUtil.showShort("请先登录！");
                        goWhiteBarActivity(LoginFragment.class.getSimpleName());
                        rgBottomTab.check(R.id.rb1);
                        return;
                    }
                    viewPager.setCurrentItem(4);
                    break;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                RadioButton childAt = (RadioButton) rgBottomTab.getChildAt(i);
                childAt.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
}
