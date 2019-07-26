package com.qiqia.duosheng.main.adapter;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemMainGoodsBinding;
import com.qiqia.duosheng.main.bean.MainTypeGoods;
import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.util.List;

import cn.com.smallcake_utils.ScreenUtils;
//今日人气榜单，今日必买推荐，9.9包邮
public class MainGoodsAdapter extends BaseQuickAdapter<MainTypeGoods, BaseViewHolder> {
    private OnItemGoodsClickListener onItemGoodsClickListener;
    public interface OnItemGoodsClickListener {
        void onItemGoodsClick(GoodsInfo goods);
    }

    public void setOnItemGoodsClickListener(OnItemGoodsClickListener onItemGoodsClickListener) {
        this.onItemGoodsClickListener = onItemGoodsClickListener;
    }

    public MainGoodsAdapter() {
        super(R.layout.item_main_type_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainTypeGoods item) {
        helper.setText(R.id.tv_head_title,item.getHeadTitle())
                .setImageResource(R.id.iv_head_icon,item.getHeadIconRes())
                .addOnClickListener(R.id.tv_more);
        if (item.getHeadTitle().equals("今日必买推荐"))helper.getView(R.id.tv_more).setVisibility(View.GONE);
        addGoods(helper,item.getGoodsList());
    }

    private void addGoods(BaseViewHolder helper, List<GoodsInfo> goodsList) {
        if (goodsList==null)return;
        LinearLayout view = helper.getView(R.id.layout_goods);
        view.removeAllViews();
        for (int i = 0; i < goodsList.size(); i++) {
            GoodsInfo goods = goodsList.get(i);
            //控件获取
            View layoutGoods = LayoutInflater.from(mContext).inflate(R.layout.item_main_goods, null);
            layoutGoods.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth()/10*3, LinearLayout.LayoutParams.MATCH_PARENT));
            //点击事件
            layoutGoods.setOnClickListener(v -> {
                if (onItemGoodsClickListener!=null)onItemGoodsClickListener.onItemGoodsClick(goods);
            });
            //数据绑定
            ItemMainGoodsBinding bind = DataBindingUtil.bind(layoutGoods);
            bind.setItem(goods);
            view.addView(layoutGoods);
        }


    }
}
