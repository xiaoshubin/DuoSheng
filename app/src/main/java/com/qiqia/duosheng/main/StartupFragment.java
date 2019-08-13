package com.qiqia.duosheng.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.MainActivity;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.SPStr;

import cn.com.smallcake_utils.SPUtils;

public class StartupFragment extends BaseFragment {
    public static StartupFragment newInstance() {
        Bundle args = new Bundle();
        StartupFragment fragment = new StartupFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int setLayout() {
        return R.layout.fragment_startup;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        new Handler().postDelayed(() -> {
            int index = (int) SPUtils.get(SPStr.GUIDE_VIEWPAGER_FRAGMENT, 0);
//             SPUtils.put(SPStr.GUIDE_VIEWPAGER_FRAGMENT, 0);
            if (index==0){
                start(GuideViewPagerFragment.newInstance());
            }else{
                startActivity(new Intent(_mActivity, MainActivity.class));
                _mActivity.finish();
            }
        },3000);
    }
}
