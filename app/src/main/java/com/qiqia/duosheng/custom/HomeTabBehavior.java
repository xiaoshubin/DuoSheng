package com.qiqia.duosheng.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import cn.com.smallcake_utils.DpPxUtils;

public class HomeTabBehavior extends CoordinatorLayout.Behavior<View> {
    // 列表顶部和title底部重合时，列表的滑动距离。
    private float deltaY;
    int headHeight;
    public HomeTabBehavior() {
    }

    public HomeTabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
         headHeight = DpPxUtils.dp2px(78);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof ViewPager;
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
            deltaY = dependency.getY();
        }
        float dy = dependency.getY() - child.getHeight(); // dy 195  dependency113- 339  child :48-144
//        L.e("dy=="+dy+"    dependency.getY()=="+dependency.getY()+"  child.getHeight()=="+child.getHeight());
        dy = dy < 0 ? 0 : dy;
        float y = (dy / deltaY) * deltaY;
        y = y< headHeight?headHeight:y;
        child.setTranslationY(y);

        return true;
    }
}
