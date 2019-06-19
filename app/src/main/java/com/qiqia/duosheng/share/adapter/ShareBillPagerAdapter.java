package com.qiqia.duosheng.share.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;

import java.util.LinkedList;
import java.util.List;

import cn.com.smallcake_utils.L;


public class ShareBillPagerAdapter extends PagerAdapter
        implements ViewPager.OnPageChangeListener{
    private Context mContext; // 上下文
    private LayoutInflater mInflater; // 用于解XML
    private LinkedList<View> mViews; // <span style="font-family: Arial, Helvetica, sans-serif;">用于</span><span style="font-family: Arial, Helvetica, sans-serif;">显示的View</span>
//    private List<Integer> mList; // 数据源<span style="font-family: Arial, Helvetica, sans-serif;">Drawable</span>
    private ViewPager mViewPager; //页面


    public ShareBillPagerAdapter(Context context, ViewPager viewPager, List<Integer> list){
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        this.mList = list;
        mViewPager = viewPager;
        if (list != null) {
            //无论是否多于1个，都要初始化第一个（index:0）
            mViews = new LinkedList<View>();
            ConstraintLayout view = (ConstraintLayout) (mInflater.inflate(R.layout.fragment_invite_friend_item, null));
            Integer drawable = list.get(list.size() - 1);
//            view.setImageDrawable(drawable);
            mViews.add(view);
            //注意，如果不只1个，mViews比mList多两个（头尾各多一个）
            //假设：mList为mList[0~N-1], mViews为mViews[0~N+1]
            // mViews[0]放mList[N-1], mViews[i]放mList[i-1], mViews[N+1]放mList[0]
            // mViews[1~N]用于循环；首位之前的mViews[0]和末尾之后的mViews[N+1]用于跳转
            // 首位之前的mViews[0]，跳转到末尾（N）；末位之后的mViews[N+1]，跳转到首位（1）
            if( list.size() > 1) { //多于1个要循环
                for (Integer d : list) { //中间的N个（index:1~N）
                    ConstraintLayout v = (ConstraintLayout) mInflater.inflate(
                            R.layout.fragment_invite_friend_item, null);
//                    v.setImageDrawable(d);
                    mViews.add(v);
                }
                //最后一个（index:N+1）
                view = (ConstraintLayout) mInflater.inflate(R.layout.fragment_invite_friend_item, null);
                drawable = list.get(0);
//                view.setImageDrawable(drawable);
                mViews.add(view);
            }
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        ((ViewPager) container).removeView(mViews.get(position));
//    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        View view = mViews.get(position);
//        container.addView(view);
        return (ConstraintLayout) mInflater.inflate(R.layout.fragment_invite_friend_item, null);
    }


    // 实现ViewPager.OnPageChangeListener接口
    @Override
    public void onPageSelected(int position) {
        L.i("onPageSelected:" + position);
//        if ( mViews.size() > 1) { //多于1，才会循环跳转
//            if ( position < 1) { //首位之前，跳转到末尾（N）
//                position = mList.size(); //注意这里是mList，而不是mViews
//                mViewPager.setCurrentItem(position, false);
//            } else if ( position > mList.size()) { //末位之后，跳转到首位（1）
//                mViewPager.setCurrentItem(1, false); //false:不显示跳转过程的动画
//                position = 1;
//            }
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // 什么都不干
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 什么都不干
    }
}


