package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.main.guide.GuideFragment1;
import com.qiqia.duosheng.main.guide.GuideFragment2;

import butterknife.BindView;

/**
 *
 */
public class GuideViewPagerFragment extends BaseFragment {

    public static ViewPager viewPagerGuide;
    @BindView(R.id.layout_indicator)
    LinearLayout layoutIndicator;
    public static GuideViewPagerFragment newInstance() {
        Bundle args = new Bundle();
        GuideViewPagerFragment fragment = new GuideViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_guide_viewpager;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        viewPagerGuide = view.findViewById(R.id.view_pager_guide);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int i) {
                return i==0? new GuideFragment1():new GuideFragment2();
            }
        };
        viewPagerGuide.setAdapter(fragmentPagerAdapter);
        viewPagerGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectIndicator(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    private void selectIndicator(int selectPosition){
        for (int i = 0; i < layoutIndicator.getChildCount(); i++) layoutIndicator.getChildAt(i).setAlpha(i==selectPosition ? 1f : 0.4f);
    }
}
