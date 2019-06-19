package com.qiqia.duosheng.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import cn.com.smallcake_utils.L;

/**
 * 获取RecyclerView的垂直滚动距离
 */
public class OffsetGridLayoutManager extends GridLayoutManager {
    //竖着
    private static final int MANAGER_LINEAR_VERTICAL = 0;
    //横着一行
    private static final int MANAGER_LINEAR_HORIZONTAL = 1;
    //Grid 竖着
    private static final int MANAGER_LINEAR_GRIDVIEW_VERTICAL = 2;
    //Grid 横着
    private static final int MANAGER_LINEAR_GRIDVIEW_HORIZONTAL = 3;
    //竖着不同
    private static final int MANAGER_LINEAR_VERTICAL_ = 4;
    //横着不同
    private static final int MANAGER_LINEAR_HORIZONTAL_ = 5;
    //形态变量
    private int intType = 0;
    //item的宽/高
    private int itemW;
    private int itemH;
    private int iResult;
    //存放item宽或高
    private Map<Integer, Integer> mMapList = new HashMap<>();
    private int iposition;


    public OffsetGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }



    private Map<Integer, Integer> heightMap = new HashMap<>();

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        int count = getChildCount();
        for (int i = 0; i < count ; i++) {
            View view = getChildAt(i);
            heightMap.put(i, view.getHeight());
        }
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        try {
            int firstVisiablePosition = findFirstVisibleItemPosition();
            View firstVisiableView = findViewByPosition(firstVisiablePosition);
            int offsetY = -(int) (firstVisiableView.getY());
            for (int i = 0; i < firstVisiablePosition; i++) {
                offsetY += heightMap.get(i) == null ? 0 : heightMap.get(i);
            }
            return offsetY;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getScollYDistance() {
        if (intType == MANAGER_LINEAR_VERTICAL) {
            //找到即将移出屏幕Item的position,position是移出屏幕item的数量
            int position = this.findFirstVisibleItemPosition();
            //根据position找到这个Item
            View firstVisiableChildView = this.findViewByPosition(position);
            //获取Item的高
            int itemHeight = firstVisiableChildView.getHeight();
            //算出该Item还未移出屏幕的高度
            int itemTop = firstVisiableChildView.getTop();
            //position移出屏幕的数量*高度得出移动的距离
            int iposition = position * itemHeight;
            //减去该Item还未移出屏幕的部分可得出滑动的距离
            iResult = iposition - itemTop;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_HORIZONTAL) {
            //找到即将移出屏幕Item的position,position是移出屏幕item的数量
            int position = this.findFirstVisibleItemPosition();
            //根据position找到这个Item
            View firstVisiableChildView = this.findViewByPosition(position);
            //获取Item的宽
            int itemWidth = firstVisiableChildView.getWidth();
            //算出该Item还未移出屏幕的高度
            int itemRight = firstVisiableChildView.getRight();
            //position移出屏幕的数量*高度得出移动的距离
            int iposition = position * itemWidth;
            //因为横着的RecyclerV第一个取到的Item position为零所以计算时需要加一个宽
            iResult = iposition - itemRight + itemWidth;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_GRIDVIEW_VERTICAL) {
            //得出spanCount几列或几排
            int itemSpanCount = this.getSpanCount();
            //得出的position是一排或一列总和
            int position = this.findFirstVisibleItemPosition();
            //需要算出才是即将移出屏幕Item的position
            int itemPosition = position / itemSpanCount ;
            //因为是相同的Item所以取那个都一样
            View firstVisiableChildView = this.findViewByPosition(position);
            int itemHeight = firstVisiableChildView.getHeight();
            int itemTop = firstVisiableChildView.getTop();
            int iposition = itemPosition * itemHeight;
            iResult = iposition - itemTop;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_GRIDVIEW_HORIZONTAL) {
            //得出spanCount
            int itemSpanCount = this.getSpanCount();
            int position = this.findFirstVisibleItemPosition();
            //算出item position
            int itemPosition = position / itemSpanCount + 1;
            View firstVisiableChildView = this.findViewByPosition(position);
            int itemWidth = firstVisiableChildView.getWidth();
            int itemRight = firstVisiableChildView.getRight();
            int iposition = itemPosition * itemWidth;
            iResult = iposition - itemRight;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_VERTICAL_ | intType == MANAGER_LINEAR_HORIZONTAL_) {
            iResult = unlikeVertical();
        }
        return iResult;
    }
    /**
     * 不同Item VERTICAL
     */
    public int unlikeVertical() {
        int itemWH = 0;
        int itemTR = 0;
        int distance = 0;
        int position = this.findFirstVisibleItemPosition();
        View firstVisiableChildView = this.findViewByPosition(position);
        //判断是横着还是竖着，得出宽或高
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            itemWH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_HORIZONTAL_) {
            itemWH = firstVisiableChildView.getWidth();
        }
        //一层判断mMapList是否为空，若不为空则根据键判断保证不会重复存入
        if (mMapList.size() == 0) {
            mMapList.put(position, itemWH);
        } else {
            if (!mMapList.containsKey(position)) {
                mMapList.put(position, itemWH);
                L.d("poi", mMapList + "");
            }
        }
        //判断是横着还是竖着，得出未滑出屏幕的距离
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            itemTR = firstVisiableChildView.getTop();
        } else if (intType == MANAGER_LINEAR_HORIZONTAL_) {
            itemTR = firstVisiableChildView.getRight();
        }
        //position为动态获取，目前屏幕Item位置
        for (int i = 0; i < position; i++) {
            //iposition移出屏幕的距离
            iposition = iposition + mMapList.get(i);
        }
        //根据类型拿iposition减未移出Item部分距离，最后得出滑动距离
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            distance = iposition - itemTR;
        } else if (intType == MANAGER_LINEAR_HORIZONTAL_) {
            distance = iposition - itemTR + itemWH;
        }
        //item宽高
        itemW = firstVisiableChildView.getWidth();
        itemH = firstVisiableChildView.getHeight();
        //归零
        iposition = 0;
        return distance;
    }
}
