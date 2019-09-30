package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;


public class CoordinatorLayoutTestFragment extends BaseFragment {
    private static final String TAG = "CoordinatorLayoutTestFr";

    @Override
    public int setLayout() {
        return R.layout.fragment_coordinatorlayout_test;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
    }

}
