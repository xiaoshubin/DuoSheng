package com.qiqia.duosheng.custom;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerViewPagerAdapter extends PagerAdapter {


    public abstract RecyclerView getItem(int var1);
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((RecyclerView)object).getRootView() == view;
    }
}
