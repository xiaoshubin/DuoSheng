package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;

public class AboutUsFragment extends BaseBarFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_about_us;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("关于我们");


    }
}
