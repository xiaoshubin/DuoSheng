package com.qiqia.duosheng.classfiy.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.classfiy.bean.ClassfiySection;
import com.qiqia.duosheng.main.bean.Classfiy;

import java.util.List;

public class ClassfiyAdapter extends BaseSectionQuickAdapter<ClassfiySection, BaseViewHolder> {

    public ClassfiyAdapter(List<ClassfiySection> data) {
        super(R.layout.item_classfiy_content, R.layout.item_classfiy_head, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ClassfiySection item) {
            helper.setText(R.id.tv_head_title,item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassfiySection item) {
        Classfiy.DataBean.InfoBean info = item.t;
        helper.setText(R.id.tv_name,info.getSonName());
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        Glide.with(mContext).load(info.getImgUrl()).into(ivIcon);
    }
}
