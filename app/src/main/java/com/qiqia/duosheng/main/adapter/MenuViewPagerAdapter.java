package com.qiqia.duosheng.main.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MenuViewPagerAdapter extends PagerAdapter {
    private ArrayList<RecyclerView> mList;
    public MenuViewPagerAdapter(ArrayList<RecyclerView> mList) {
        this.mList = mList;
    }
    @Override
    public int getCount() {
        return mList.isEmpty() ? 0 : mList.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(position));
        return mList.get(position);
    }
}
