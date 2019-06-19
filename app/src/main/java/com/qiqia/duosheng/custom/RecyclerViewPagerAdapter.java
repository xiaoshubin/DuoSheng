package com.qiqia.duosheng.custom;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerViewPagerAdapter extends PagerAdapter {


    public abstract RecyclerView getItem(int var1);
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((RecyclerView)object).getRootView() == view;
    }
}
