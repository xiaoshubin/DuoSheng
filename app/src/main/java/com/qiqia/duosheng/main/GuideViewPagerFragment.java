package com.qiqia.duosheng.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager(),0) {
            @Override
            public int getCount() {
                return 2;
            }
            @Override
            public Fragment getItem(int i) {
                return i == 0 ? new GuideFragment1() : new GuideFragment2();
            }
        };
        viewPagerGuide.setAdapter(fragmentPagerAdapter);
        viewPagerGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                selectIndicator(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void selectIndicator(int selectPosition) {
        for (int i = 0; i < layoutIndicator.getChildCount(); i++) {
            View view = layoutIndicator.getChildAt(i);
            if (i==selectPosition) animPot(view);
            else view.setAlpha(0.3f);

        }
    }
    private void animPot(View view){
        PropertyValuesHolder anim1 = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,anim1);
        animator.setDuration(300);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

}
