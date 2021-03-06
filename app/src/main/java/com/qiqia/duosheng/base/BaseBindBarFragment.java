package com.qiqia.duosheng.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import butterknife.ButterKnife;
import butterknife.Unbinder;
/**
 * 必须引入此头部
 * <include layout="@layout/action_common_bar"/>或者
 * <include layout="@layout/action_black_common_bar"/>
 */
public abstract class BaseBindBarFragment<VB extends ViewDataBinding> extends BaseBarFragment {
    protected VB mBinding;
    public abstract int setLayout();
    protected abstract void onBindView(View view, ViewGroup container, Bundle savedInstanceState);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(this.getContext()).inflate(setLayout(), null);
        mBinding = DataBindingUtil.bind(view);
        unbinder = ButterKnife.bind(this,view);
        onBindView(view, container, savedInstanceState);
        return view;
    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null)unbinder.unbind();
    }
}
