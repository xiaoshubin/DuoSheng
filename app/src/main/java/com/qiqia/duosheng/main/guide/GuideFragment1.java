package com.qiqia.duosheng.main.guide;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.main.GuideViewPagerFragment;

import butterknife.OnClick;

public class GuideFragment1 extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.guide_fragment1;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {

    }
    @OnClick(R.id.btn_next)
    public void doClick(){
        GuideViewPagerFragment.viewPagerGuide.setCurrentItem(1);
    }
}
