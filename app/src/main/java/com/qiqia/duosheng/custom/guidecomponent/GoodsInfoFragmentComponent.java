package com.qiqia.duosheng.custom.guidecomponent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blog.www.guideview.Component;
import com.qiqia.duosheng.R;

import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ScreenUtils;

public class GoodsInfoFragmentComponent implements Component {
    private int targetViewHeight;
    public GoodsInfoFragmentComponent(int dotLineRectHeight) {
         targetViewHeight = dotLineRectHeight;
    }


    OnComponentClickListener listener;

    public void setListener(OnComponentClickListener listener) {
        this.listener = listener;
    }
    @Override public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_goodsinfo_fragment, null);
        ll.setMinimumWidth(ScreenUtils.getScreenWidth()- DpPxUtils.dp2px(8));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, targetViewHeight);
        View viewDottedRect = ll.findViewById(R.id.view_dotted_rect);
        View viewDottedRectBuy = ll.findViewById(R.id.view_dotted_rect_buy);
        viewDottedRect.setLayoutParams(param);
        L.e("targetView.getMeasuredHeight()=="+targetViewHeight);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (listener!=null)listener.onClick();
            }
        });
        return ll;
    }



    @Override public int getAnchor() {
        return Component.ANCHOR_TOP;
    }

    @Override public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return DpPxUtils.px2dp(targetViewHeight)-DpPxUtils.px2dp(8)+60;
    }
}
