package com.qiqia.duosheng.main.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.main.bean.GoodsType;
/**
 * 纯文字的大类
 */
public class TextGoodsTypeAdapter extends BaseQuickAdapter<GoodsType, BaseViewHolder> {

    int selectPosition;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        this.notifyDataSetChanged();
    }

    public TextGoodsTypeAdapter() {
        super(R.layout.item_text_goods_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsType item) {

        int layoutPosition = helper.getLayoutPosition();
        if (layoutPosition==selectPosition){
            helper.setTextColor(R.id.tv_name, Color.WHITE);
            helper.setBackgroundRes(R.id.tv_name, R.drawable.round_orange_btn_bg);
        }else {
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext,R.color.text_black));
            helper.setBackgroundColor(R.id.tv_name, Color.WHITE);
        }
    }
}
