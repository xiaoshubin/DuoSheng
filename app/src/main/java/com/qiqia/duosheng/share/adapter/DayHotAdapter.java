package com.qiqia.duosheng.share.adapter;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.custom.BigPicPopImageLoader;
import com.qiqia.duosheng.databinding.ItemDayHotBinding;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;
import com.qiqia.duosheng.utils.ImageBindingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.ScreenUtils;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.custom.AutoNewLineLayout;

public class DayHotAdapter extends BaseQuickAdapter<GoodsInfo, DataBindBaseViewHolder> {

    ViewGroup.MarginLayoutParams layoutParams;
    public DayHotAdapter() {
        super(R.layout.item_day_hot);
        int imgWidth = (ScreenUtils.getScreenWidth()- DpPxUtils.dp2px(24))/3;
         layoutParams = new ViewGroup.MarginLayoutParams(imgWidth, imgWidth);
         layoutParams.setMargins(DpPxUtils.dp2px(4),DpPxUtils.dp2px(4),0,0);
    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, GoodsInfo item) {
        ItemDayHotBinding binding = (ItemDayHotBinding) helper.getBinding();
        binding.setItem(item);

        String guideArticle = item.getGuideArticle();
        SpannableStringBuilder goodsInfoDesc = SpannableStringUtils.getBuilder(TextUtils.isEmpty(guideArticle)?item.getItemDesc():guideArticle)
                .append("   查看详情").setForegroundColor(Color.parseColor("#D89C2A")).create();
        helper.setText(R.id.tv_goods_info_desc, goodsInfoDesc)
                .addOnClickListener(R.id.tv_goods_info_desc)
                .addOnClickListener(R.id.tv_share)
                .addOnClickListener(R.id.tv_copy_comment);
        //图片合集
        String itemPic = item.getItemPic();
        List<String> itemPicList = item.getItemPicList();
        if (itemPicList==null||itemPicList.size()==0){
            setShowPic(binding.layoutPics,Arrays.asList(itemPic));
        }else {
            setShowPic(binding.layoutPics,itemPicList);
        }


    }


    private void setShowPic(AutoNewLineLayout layout,List<String> itemPicList){
        //如果没有图片集合，图片控件全部隐藏
        if (itemPicList==null||itemPicList.size()==0){
            for (int i = 0; i < layout.getChildCount(); i++) layout.getChildAt(i).setVisibility(View.GONE);
            return;
        }
        //显示需要展示图片的控件
        int showNum = itemPicList.size();
        for (int i = 0; i < showNum; i++) {
            ImageView imageView = (ImageView) layout.getChildAt(i);
            imageView.setVisibility(View.VISIBLE);
            imageView.setLayoutParams(layoutParams);
            Glide.with(mContext).load(itemPicList.get(i)).apply(ImageBindingAdapter.getImgOptions()).into(imageView);
            //点击单张图片进行大图浏览
            int finalI = i;
            imageView.setOnClickListener(v -> showPics(layout, itemPicList, imageView, finalI));
        }
        //隐藏其他未展示控件
        if (showNum<layout.getChildCount()){
            for (int i = showNum; i < layout.getChildCount(); i++) layout.getChildAt(i).setVisibility(View.GONE);
        }
    }
    //显示多图浏览弹出pop
    private void showPics(AutoNewLineLayout layout, List<String> itemPicList, ImageView imageView, int position) {
        List<Object> list = new ArrayList<>();
        for (String url : itemPicList) list.add(url);
        new XPopup.Builder(mContext).asImageViewer(imageView, position, list,
                (popupView, index) -> popupView.updateSrcView((ImageView) layout.getChildAt(index)),
                new BigPicPopImageLoader())
                .show();
    }
}
