package com.qiqia.duosheng.share.adapter;

import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.custom.BigPicPopImageLoader;
import com.qiqia.duosheng.databinding.ItemCircleOfFriendsBinding;
import com.qiqia.duosheng.share.bean.ShareList;
import com.qiqia.duosheng.utils.DataBindBaseViewHolder;
import com.qiqia.duosheng.utils.ImageBindingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.smallcake_utils.DpPxUtils;
import cn.com.smallcake_utils.ScreenUtils;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.TimeUtils;
import cn.com.smallcake_utils.custom.AutoNewLineLayout;

/**
 * 朋友圈
 */
public class CircleOfFriendsAdapter extends BaseQuickAdapter<ShareList.DataBean, DataBindBaseViewHolder> {
    ViewGroup.MarginLayoutParams layoutParams;
    public CircleOfFriendsAdapter() {
        super(R.layout.item_circle_of_friends);
        int imgWidth = (ScreenUtils.getScreenWidth()- DpPxUtils.dp2px(24))/3;
        layoutParams = new ViewGroup.MarginLayoutParams(imgWidth, imgWidth);
        layoutParams.setMargins(DpPxUtils.dp2px(4),DpPxUtils.dp2px(4),0,0);
    }

    @Override
    protected void convert(DataBindBaseViewHolder helper, ShareList.DataBean item) {

        ItemCircleOfFriendsBinding binding = (ItemCircleOfFriendsBinding) helper.getBinding();
        binding.setItem(item);

        SpannableStringBuilder goodsInfoDesc = SpannableStringUtils.getBuilder(item.getItemTitle())
                .append("   查看详情").setForegroundColor(Color.parseColor("#D89C2A")).create();
        helper.setText(R.id.tv_goods_info_desc,goodsInfoDesc)
                .setText(R.id.tv_goods_desc,  Html.fromHtml(Html.fromHtml(item.getCopyContent()).toString())).addOnClickListener(R.id.tv_share);

        //图片合集
        String itemPic = item.getItemPic();
        List<String> itemPicList = item.getItemPicList();
        if (itemPicList==null||itemPicList.size()==0){
            setShowPic(binding.layoutPics, Arrays.asList(itemPic));
        }else {
            setShowPic(binding.layoutPics,itemPicList);
        }
        //时间
        int showTime = item.getShowTime();
        binding.tvTime.setText(TimeUtils.tsToCustom(showTime,"MM-dd  HH:mm"));

    }
    private void setShowPic(AutoNewLineLayout layout, List<String> itemPicList){
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
        if (showNum<layout.getChildCount()) for (int i = showNum; i < layout.getChildCount(); i++) layout.getChildAt(i).setVisibility(View.GONE);

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
