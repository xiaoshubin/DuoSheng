package com.qiqia.duosheng.custom.guidecomponent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blog.www.guideview.Component;
import com.qiqia.duosheng.R;

public class MineFragmentComponent implements Component {
    public MineFragmentComponent() {
    }
    OnComponentClickListener listener;
    public void setListener(OnComponentClickListener listener) {
        this.listener = listener;
    }
    @Override public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_mine_fragment, null);
//        ll.setMinimumWidth(ScreenUtils.getScreenWidth()- DpPxUtils.dp2px(8));
        ll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (listener!=null)listener.onClick();
            }
        });
        return ll;
    }



    @Override public int getAnchor() {
        return Component.ANCHOR_LEFT;
    }

    @Override public int getFitPosition() {
        return Component.FIT_START;
    }

    @Override public int getXOffset() {
        return 37;
    }

    @Override public int getYOffset() {
        return -3;
    }
}
