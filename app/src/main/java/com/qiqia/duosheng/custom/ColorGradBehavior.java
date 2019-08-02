package com.qiqia.duosheng.custom;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.qiqia.duosheng.R;
//设置页面，顶部滚动
public class ColorGradBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "ColorGradBehavior";
    private int PAGE_COLOR_ONE;
    private int PAGE_COLOR_TWO;
    public ColorGradBehavior() {
    }
    public ColorGradBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        PAGE_COLOR_ONE = ContextCompat.getColor(context, R.color.transparent);
        PAGE_COLOR_TWO = ContextCompat.getColor(context, R.color.colorAccent);
    }
    //所依赖的联动对象
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        float dy = dependency.getHeight()+dependency.getY();
        float y = dy / child.getHeight();
        y = y>1?1:y;
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();//渐变色计算类
        int currentLastColor = (int) (argbEvaluator.evaluate(1-y, PAGE_COLOR_ONE, PAGE_COLOR_TWO));
        child.setBackgroundColor(currentLastColor);
        return true;
    }
}
