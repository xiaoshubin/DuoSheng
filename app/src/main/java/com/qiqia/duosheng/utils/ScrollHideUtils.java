package com.qiqia.duosheng.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 根据RecyclerView的滚动来显示和隐藏控件
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
                direction = dy>0;
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
                LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams();
                linearParams.height = 0;
                view.setLayoutParams(linearParams);
            }
        });
    }
    private static void showAnim(View view){
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams();
        linearParams.height = barHeight;
        view.setLayoutParams(linearParams);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animator.start();
    }
}
