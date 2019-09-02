package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBindFragment;
import com.qiqia.duosheng.databinding.FragmentCoordinatorlayoutTestBinding;


public class CoordinatorLayoutTestFragment extends BaseBindFragment<FragmentCoordinatorlayoutTestBinding> {
    public static CoordinatorLayoutTestFragment newInstance() {
        Bundle args = new Bundle();
        CoordinatorLayoutTestFragment fragment = new CoordinatorLayoutTestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int setLayout() {
        return R.layout.fragment_coordinatorlayout_test;
    }
    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {

    }

}
