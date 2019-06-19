package com.qiqia.duosheng.custom.guidecomponent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blog.www.guideview.Component;
import com.qiqia.duosheng.R;

import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.ScreenUtils;

public class HomeFragmentComponent implements Component {

    OnComponentClickListener listener;
    public void setListener(OnComponentClickListener listener) {
        this.listener = listener;
    }

    @Override public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_home_fragment, null);
        ll.setMinimumWidth(ScreenUtils.getScreenWidth()- DpPxUtils.dp2px(8));
        ll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (listener!=null)listener.onClick();
            }
        });
        return ll;
    }


    @Override public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return -65;
    }
}
