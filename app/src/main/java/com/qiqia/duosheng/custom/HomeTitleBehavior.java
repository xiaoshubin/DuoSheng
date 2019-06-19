package com.qiqia.duosheng.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import cn.com.smallcake_utils.L;

public class HomeTitleBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "HomeTitleBehavior";
    // 列表顶部和title底部重合时，列表的滑动距离。
    private float deltaY;

    public HomeTitleBehavior() {
    }

    public HomeTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    /**
     *
     * @param parent 父级CoordinatorLayout
     * @param child  代表使用该Behavior的View
     * @param dependency 代表要监听的View
     * @return
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (deltaY == 0) {
            deltaY = dependency.getY() - child.getHeight();
        }
        float dy = dependency.getY() - child.getHeight();
//        L.e("dy=="+dy+"    dependency.getY()=="+dependency.getY()+"  child.getHeight()=="+child.getHeight());
        dy = dy < 0 ? 0 : dy;
        float y = -(dy / deltaY) * child.getHeight();

        child.setTranslationY(y);

        float alpha = 1 - (dy / deltaY);
        L.e(TAG,"alpha=="+alpha);
        child.setAlpha(alpha);

        return true;
    }
}
