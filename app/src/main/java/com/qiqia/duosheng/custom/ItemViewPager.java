package com.qiqia.duosheng.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ItemViewPager extends ViewPager {
    public ItemViewPager(@NonNull Context context) {
        super(context);
    }

    public ItemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //下面这句话的作用 告诉父view，我的单击事件我自行处理，不要阻碍我。
//        getParent().requestDisallowInterceptTouchEvent(true);
//        return super.dispatchTouchEvent(ev);
//    }

}
