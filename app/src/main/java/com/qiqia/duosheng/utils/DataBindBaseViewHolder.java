package com.qiqia.duosheng.utils;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

public  class DataBindBaseViewHolder extends BaseViewHolder {
    private ViewDataBinding binding;
    public DataBindBaseViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
    public ViewDataBinding getBinding() {
        return binding;
    }
}
