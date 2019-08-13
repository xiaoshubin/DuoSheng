package com.qiqia.duosheng.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;


public class ObjectAnimUtils{
    public static void transColor(TextView view){
        ObjectAnimator animator = ObjectAnimator.ofInt(view,"textColor", 0xff2C2828, 0xffF6412D);
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }
    public static void transColor2(View view){
        ObjectAnimator animator = ObjectAnimator.ofInt(view, "BackgroundColor",  0xffF6412D,0xff2C2828 );
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

}
