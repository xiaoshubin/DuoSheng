package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindFragment;
import com.qiqia.duosheng.databinding.FragmentCoordinatorlayoutTestBinding;
import com.qiqia.duosheng.main.bean.MenuItem;

//这里的FragmentCoordinatorlayoutTestBinding就是布局文件fragment_coordinatorlayout_test去下划线，然后用骆驼命名法再加上Binding组成
public class CoordinatorLayoutTestFragment extends BaseBindFragment<FragmentCoordinatorlayoutTestBinding> {
    @Override
    public int setLayout() {
        return R.layout.fragment_coordinatorlayout_test;
    }
    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        MenuItem item = new MenuItem(R.mipmap.ic_launcher_round, "SmallCake");
        mBinding.setItem(item);
    }


}
