package com.qiqia.duosheng.custom;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;

public abstract class VHolder<VH extends BaseViewHolder> {
    protected abstract @NonNull VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);
}
