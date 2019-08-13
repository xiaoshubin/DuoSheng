package com.qiqia.duosheng.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.R;

import java.util.ArrayList;
import java.util.List;

import cn.com.smallcake_utils.ToastUtil;

/**
 * 会员粉丝排序自定义
 */
public class MemberFansSortLinearLayout extends LinearLayout implements View.OnClickListener {
    /**
     * 筛选排序
     */
    private Integer[] unSelectIcons = {R.mipmap.icon_sell, R.mipmap.icon_price, R.mipmap.icon_select};
    private Integer[] selectIcons = {R.mipmap.icon_sell_cur, R.mipmap.icon_price_cur, R.mipmap.icon_select_cur};

    private OnSortClickListener listener;

    private List<LinearLayout> layoutSelectTabs = new ArrayList<>();
    LinearLayout layoutSell;
    LinearLayout layoutPrice;
    LinearLayout layoutSelect;

    BasePopupView goodsFilterPop;

    public void setListener(OnSortClickListener listener) {
        this.listener = listener;
    }



    private void initView() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.member_fans_sort_layout, this);
        layoutSell = findViewById(R.id.layout_sell);
        layoutPrice = findViewById(R.id.layout_price);
        layoutSelect = findViewById(R.id.layout_select);
        layoutSelectTabs.add(layoutSell);
        layoutSelectTabs.add(layoutPrice);
        layoutSelectTabs.add(layoutSelect);

        layoutSell.setOnClickListener(this);
        layoutPrice.setOnClickListener(this);
        layoutSelect.setOnClickListener(this);

//        initFilter();
    }


    @Override
    public void onClick(View v) {
        if (listener == null) return;

        switch (v.getId()) {
            case R.id.layout_sell:
                cleanStyle();
                selectSell();
                break;
            case R.id.layout_price:
                cleanStyle();
                selectPrice();
                break;
            case R.id.layout_select:
                selectOrder();
                break;
        }
    }
    /**
     * 价格区间查询
     */
    private void selectOrder() {
        new XPopup.Builder(getContext())
                .hasShadowBg(false)
                .hasStatusBarShadow(false)
                .atView(layoutSelect)
                .offsetY(-8)
                .isCenterHorizontal(true)
                .asAttachList(new String[]{"全部", "VIP会员"}, null,
                        (position, text) -> {
                            cleanStyle();
                            TextView tvName = (TextView) layoutSelect.getChildAt(0);
                            tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
                            ImageView ivIcon = (ImageView) layoutSelect.getChildAt(1);
                            ivIcon.setImageResource(selectIcons[2]);
                            ToastUtil.showLong("click " + text);
                            if (listener != null) listener.onItemClick(position);
                        }).show();
    }


    /**
     * Order  4.月销量（高到低）
     * 7.月销量（低到高）
     * <p>
     * 9.全天销量（高到低）
     * 10全天销量（低到高）
     * 11.近2小时销量（高到低）
     * 12.近2小时销量（低到高）
     */

    private void selectSell() {
        TextView tvName = (TextView) layoutSell.getChildAt(0);
        tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));

        Integer tagSell = Integer.parseInt(layoutSell.getTag().toString());
        ImageView ivIcon = (ImageView) layoutSell.getChildAt(1);
        ivIcon.setImageResource(tagSell == 4 ? selectIcons[0] : selectIcons[1]);
        listener.onItemClick(tagSell);
        layoutSell.setTag(tagSell == 4 ? 7 : 4);
    }

    /**
     * Order
     * 1.券后价(低到高)，2.券后价（高到低）
     */
    private void selectPrice() {
        TextView tvName = (TextView) layoutPrice.getChildAt(0);
        tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
        Integer tag = Integer.parseInt(layoutPrice.getTag().toString());
        ImageView ivIcon = (ImageView) layoutPrice.getChildAt(1);
        ivIcon.setImageResource(tag == 1 ? selectIcons[0] : selectIcons[1]);
        listener.onItemClick(tag);
        layoutPrice.setTag(tag == 1 ? 2 : 1);
    }


//    private void initFilter() {
//        goodsFilterPop = new XPopup.Builder(getContext())
//                .hasShadowBg(false)
//                .hasStatusBarShadow(false)
//                .atView(layoutSelect)
//                .isCenterHorizontal(true)
//                .asAttachList(new String[]{"全部", "VIP会员"}, null,
//                        (position, text) -> {
//                            ToastUtil.showLong("click " + text);
//                            if (listener != null) listener.onItemClick(position);
//                        });
//
//
//    }


    public MemberFansSortLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public MemberFansSortLinearLayout(Context context) {
        super(context, null);
    }


    /**
     * 回归默认状态
     */
    private void cleanStyle() {
        for (int i = 0; i < layoutSelectTabs.size(); i++) {
            LinearLayout layoutTab = layoutSelectTabs.get(i);
            Integer unSelectIcon = unSelectIcons[i];
            TextView tvName = (TextView) layoutTab.getChildAt(0);
            ImageView ivIcon = (ImageView) layoutTab.getChildAt(1);
            tvName.setTextColor(ContextCompat.getColor(this.getContext(), R.color.text_gray));
            ivIcon.setImageResource(unSelectIcon);
        }
    }

}
