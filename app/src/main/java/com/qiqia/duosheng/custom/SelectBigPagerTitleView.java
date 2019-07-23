package com.qiqia.duosheng.custom;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

public class SelectBigPagerTitleView extends ColorTransitionPagerTitleView {
    public SelectBigPagerTitleView(Context context) {
        super(context);
    }
    @Override
    public void onSelected(int index, int totalCount) {
       setTextSize(16);
    }
    @Override
    public void onDeselected(int index, int totalCount) {
        setTextSize(14);
    }
}
