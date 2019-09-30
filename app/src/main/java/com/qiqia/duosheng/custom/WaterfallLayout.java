package com.qiqia.duosheng.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class WaterfallLayout extends ViewGroup {
    private int columns = 3;
    private int hSpace = 20;
    private int vSpace = 20;
    private int childWidth = 0;
    private int top[];
    public WaterfallLayout(Context context) {
        super(context);
    }

    public WaterfallLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }
}
