package com.qiqia.duosheng.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

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
