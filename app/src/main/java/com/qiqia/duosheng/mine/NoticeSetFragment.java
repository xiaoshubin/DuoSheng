package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;

public class NoticeSetFragment extends BaseBarFragment {

    @Override
    public int setLayout() {
        return R.layout.fragment_notice_set;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("消息通知");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        ImmersionBar.with(this).statusBarColor(R.color.white).autoStatusBarDarkModeEnable(true, 0.2f).init();
    }
}
