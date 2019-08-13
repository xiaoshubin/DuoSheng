package com.qiqia.duosheng.main.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.databinding.ItemMainGoodsBinding;
import com.qiqia.duosheng.databinding.ItemMainTypeGoodsBinding;
import com.qiqia.duosheng.main.bean.MainTypeGoods;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;

import java.util.List;

import cn.com.smallcake_utils.ScreenUtils;
//今日人气榜单，今日必买推荐，9.9包邮
public class MainGoodsAdapter extends BaseQuickAdapter<MainTypeGoods, DataBindBaseViewHolder> {
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
    protected void convert(DataBindBaseViewHolder helper, MainTypeGoods item) {
        ItemMainTypeGoodsBinding binding = (ItemMainTypeGoodsBinding) helper.getBinding();
        binding.setItem(item);
        helper.addOnClickListener(R.id.tv_more);
        if (item.getHeadTitle().equals("今日必买推荐"))helper.getView(R.id.tv_more).setVisibility(View.GONE);
        addGoods(item,binding.layoutGoods);
    }

    private void addGoods(MainTypeGoods item, LinearLayout view) {
        List<GoodsInfo> goodsList = item.getGoodsList();
        if (goodsList==null||goodsList.size()==0)return;

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
            if (item.getHeadTitle().equals("今日人气榜单")){
                //根据排行不同，显示不同Tag
                if (i==0){
                    bind.ivTag.setImageResource(R.mipmap.icon_rank1);
                }else if (i==1){
                    bind.ivTag.setImageResource(R.mipmap.icon_rank2);
                } else if (i==2){
                    bind.ivTag.setImageResource(R.mipmap.icon_rank3);
                }else {
                    bind.ivTag.setImageResource(0);
                }
            }else {
                bind.ivTag.setImageResource(0);
            }
            bind.setItem(goods);
            view.addView(layoutGoods);
        }


    }
}
