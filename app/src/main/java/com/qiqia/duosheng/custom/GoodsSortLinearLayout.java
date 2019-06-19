package com.qiqia.duosheng.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.R;

import java.util.ArrayList;
import java.util.List;

public class GoodsSortLinearLayout extends LinearLayout implements View.OnClickListener {
    /**
     * 筛选排序
     */
    private Integer[] unSelectIcons = {R.mipmap.icon_com, R.mipmap.icon_sell, R.mipmap.icon_price, R.mipmap.icon_select};
    private Integer[] selectIcons = {R.mipmap.icon_com_decline, R.mipmap.icon_sell_cur, R.mipmap.icon_price_cur, R.mipmap.icon_select_cur};

    private OnSortClickListener listener;

    private List<LinearLayout> layoutSelectTabs = new ArrayList<>();
    LinearLayout layoutCom;
    LinearLayout layoutSell;
    LinearLayout layoutPrice ;
    LinearLayout layoutSelect;

    BasePopupView goodsFilterPop;
    BasePopupView comPopupView;

    public void setListener(OnSortClickListener listener) {
        this.listener = listener;
    }
    public interface OnSortClickListener{
        void onItemClick(int order);
        void onFilterClick(int lowPrice, int highPrice);

    }

    private void initView() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.goods_sort_layout,this);
        layoutCom = findViewById(R.id.layout_com);
        layoutSell = findViewById(R.id.layout_sell);
        layoutPrice = findViewById(R.id.layout_price);
        layoutSelect = findViewById(R.id.layout_select);
        layoutSelectTabs.add(layoutCom);
        layoutSelectTabs.add(layoutSell);
        layoutSelectTabs.add(layoutPrice);
        layoutSelectTabs.add(layoutSelect);

        layoutCom.setOnClickListener(this);
        layoutSell.setOnClickListener(this);
        layoutPrice.setOnClickListener(this);
        layoutSelect.setOnClickListener(this);

        initFilter();
        initCom();
    }


    @Override
    public void onClick(View v) {
        if (listener==null)return;
        cleanStyle();
        switch (v.getId()){
            case R.id.layout_com:
                if (comPopupView!=null)comPopupView.show();
                break;
            case R.id.layout_sell:
                selectSell();
                break;
            case R.id.layout_price:
                selectPrice();
                break;
            case R.id.layout_select:
                if (goodsFilterPop!=null)goodsFilterPop.show();
                break;
        }
    }

    /**
     * 综合选择项
     */
    private void initCom() {
        TextView tvName = (TextView) layoutCom.getChildAt(0);
        tvName.setTextColor(ContextCompat.getColor(getContext(),R.color.text_black));
        ImageView ivIcon = (ImageView) layoutCom.getChildAt(1);
        ivIcon.setImageResource(selectIcons[0]);
        ComprehensivePopWindow comprehensivePopWindow = new ComprehensivePopWindow(this.getContext());
        comprehensivePopWindow.setListener(new ComprehensivePopWindow.OnComListener() {
            @Override
            public void onItemChecked(int position) {
                TextView tvName = (TextView) layoutCom.getChildAt(0);
                tvName.setTextColor(ContextCompat.getColor(getContext(),R.color.text_black));
                ImageView ivIcon = (ImageView) layoutCom.getChildAt(1);
                ivIcon.setImageResource(selectIcons[0]);
                if (listener!=null){
                    switch (position){
                        case 0://综合，默认
                            listener.onItemClick(14);
                            break;
                        case 1://佣金比例从高到低
                            listener.onItemClick(5);
                            break;
                        case 2://佣金比例从低到高
                            listener.onItemClick(8);
                            break;
                    }

                }
            }
        });
         comPopupView = new XPopup.Builder(getContext())
                .atView(this)
                .asCustom(comprehensivePopWindow);
    }

    /**
     * Order  4.月销量（高到低）
     *          7.月销量（低到高）
     *
     *          9.全天销量（高到低）
     *          10全天销量（低到高）
     *         11.近2小时销量（高到低）
     *         12.近2小时销量（低到高）
      */

    private void selectSell() {
        TextView tvName = (TextView) layoutSell.getChildAt(0);
        tvName.setTextColor(ContextCompat.getColor(getContext(),R.color.text_black));

        Integer tagSell = Integer.parseInt(layoutSell.getTag().toString());
        ImageView ivIcon = (ImageView) layoutSell.getChildAt(1);
        ivIcon.setImageResource(tagSell==4?selectIcons[1]:selectIcons[2]);
        listener.onItemClick(tagSell);
        layoutSell.setTag(tagSell==4?7:4);
    }

    /**
     * Order
     *1.券后价(低到高)，2.券后价（高到低）
     */
    private void selectPrice() {
        TextView tvName = (TextView) layoutPrice.getChildAt(0);
        tvName.setTextColor(ContextCompat.getColor(getContext(),R.color.text_black));
        Integer tag = Integer.parseInt(layoutPrice.getTag().toString());
        ImageView ivIcon = (ImageView) layoutPrice.getChildAt(1);
        ivIcon.setImageResource(tag==1?selectIcons[1]:selectIcons[2]);
        listener.onItemClick(tag);
        layoutPrice.setTag(tag==1?2:1);
    }

    /**
     * 价格区间查询
     */
    private void initFilter() {

        GoodsFilterPopWindow goodsFilterPopWindow = new GoodsFilterPopWindow(getContext());
        goodsFilterPopWindow.setListener(new GoodsFilterPopWindow.OnFilterListener() {
            @Override
            public void onFilterConfirm(int lowPrice, int highPrice) {
                TextView tvName = (TextView) layoutSelect.getChildAt(0);
                tvName.setTextColor(ContextCompat.getColor(getContext(),R.color.text_black));
                ImageView ivIcon = (ImageView) layoutSelect.getChildAt(1);
                ivIcon.setImageResource(selectIcons[3]);
                if (listener!=null)listener.onFilterClick(lowPrice,highPrice);
            }
        });
         goodsFilterPop = new XPopup.Builder(getContext())
                .atView(this)
                .asCustom(goodsFilterPopWindow);


    }



    public GoodsSortLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }
    public GoodsSortLinearLayout(Context context) {
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
