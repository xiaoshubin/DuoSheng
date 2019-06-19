package com.qiqia.duosheng.main.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.main.bean.Classfiy;

import java.util.List;

/**
 * 首页，所有分类弹出POP
 */
public class AllGoodsTypePopAdapter extends BaseQuickAdapter<Classfiy, BaseViewHolder> {
    public AllGoodsTypePopAdapter() {
        super(R.layout.item_goods_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, Classfiy item) {
        helper.setText(R.id.tv_name,item.getMainName());
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        List<Classfiy.DataBean> data = item.getData();
        if (data!=null){
            Classfiy.DataBean.InfoBean infoBean = data.get(0).getInfo().get(0);
            Glide.with(mContext).load(infoBean.getImgUrl()).into(ivIcon);
        }else {
            Glide.with(mContext).load(item.getImgRes()).into(ivIcon);
        }


    }
}
