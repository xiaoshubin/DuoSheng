package com.qiqia.duosheng.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.com.smallcake_utils.ScreenUtils;

//底部固定View
public class BottomTabPinBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "HomeTitleBehavior";
    // 列表顶部和title底部重合时，列表的滑动距离。
    private float deltaY;

    public BottomTabPinBehavior() {
    }

    public BottomTabPinBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof RecyclerView;
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
        int transY = ScreenUtils.getRealHight(parent.getContext()) - child.getHeight();
        child.setTranslationY(transY);
        return true;
    }
}
