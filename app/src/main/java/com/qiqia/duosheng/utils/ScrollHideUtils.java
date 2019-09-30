package com.qiqia.duosheng.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import cn.com.smallcake_utils.L;

/**
 * 根据RecyclerView的滚动来显示和隐藏控件
 * barView的父布局一定要是对应的布局参数：
 * 如父布局为LinearLayout： LinearLayout.LayoutParams
 * ConstraintLayout：ConstraintLayout.LayoutParams
 */
public class ScrollHideUtils {
    static int barHeight;
    public static void hideBarByRecyclerView(RecyclerView recyclerView, View barView){
        barHeight = barView.getLayoutParams().height;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean direction=false;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE://现在不是滚动状态
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://手指 拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滚动
                        if (direction) hideAnim(barView);else showAnim(barView);
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                L.e("dy=="+dy);
                direction = dy>barHeight;
            }
        });
    }

    private static void hideAnim(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ConstraintLayout.LayoutParams linearParams =(ConstraintLayout.LayoutParams) view.getLayoutParams();
                linearParams.height = 0;
                view.setLayoutParams(linearParams);
            }
        });
    }
    private static void showAnim(View view){
        ConstraintLayout.LayoutParams linearParams =(ConstraintLayout.LayoutParams) view.getLayoutParams();
        linearParams.height = barHeight;
        view.setLayoutParams(linearParams);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animator.start();
    }
}
