package com.qiqia.duosheng.custom;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

/**
 * 顶部搜索栏自定义联动行为器
 *
 */
public class TransSearchBehavior extends CoordinatorLayout.Behavior<Toolbar> {
    /**标题栏的高度*/
    private int mToolbarHeight = 0;
    public TransSearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 类型判定
     * 如果是搜索的LinearLayout
     * @param parent 父级容器
     * @param child 子级视图
     * @param dependency 相对联动的控件
     * @return
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull Toolbar child, @NonNull View dependency) {
        return dependency instanceof LinearLayout;
    }

    /**
     * 当视图改变时候的变换事件
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull Toolbar child, @NonNull View dependency) {
        // 初始化高度
        if (mToolbarHeight == 0) {
            mToolbarHeight = child.getBottom() * 2;//为了更慢的
        }
        //
        //计算toolbar从开始移动到最后的百分比
        float percent = dependency.getY() / mToolbarHeight;
        //百分大于1，直接赋值为1
        if (percent >= 1) {
            percent = 1f;
        }
        // 计算alpha通道值
        float alpha = percent * 255;

        //设置背景颜色
        child.setBackgroundColor(Color.argb((int) alpha, 63, 81, 181));
        return true;
    }
}
