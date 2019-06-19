package com.qiqia.duosheng.mine.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.custom.CouponBg;
import com.qiqia.duosheng.search.bean.GoodsInfo;

import java.util.ArrayList;
import java.util.Collections;

import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.ToastUtil;

public class MyCollectionAdapter extends BaseQuickAdapter<GoodsInfo, BaseViewHolder> {
    private ArrayList<Integer> selectArrays = new ArrayList<>();
    private boolean isShowSelectBtn;//是否显示选中按钮,默认不显示
    public MyCollectionAdapter() {
        super(R.layout.item_my_collection);
    }

    /**
     * 单项
     * 选中或取消选中
     * @param selectPosition
     */
    public void setSelectPosition(Integer selectPosition) {
        boolean contains = selectArrays.contains(selectPosition);
        if (contains) selectArrays.remove(selectPosition);
        else selectArrays.add(selectPosition);
        Collections.sort(selectArrays);
        L.e("收藏的位置=="+mData.get(selectPosition).toString());
        notifyDataSetChanged();
    }

    /**
     * 全选
     */
    public void selectAll() {
        for (int i = 0; i < mData.size(); i++) {
            boolean contains = selectArrays.contains(i);
            if (!contains)selectArrays.add(i);
        }
        Collections.sort(selectArrays);
        L.e("selectArrays=="+selectArrays.toString());
        notifyDataSetChanged();
    }

    /**
     * 是否选中了所有的数据
     */
    public boolean isSelectAll(){
        return mData.size()== selectArrays.size();
    }

    /**
     * 删除选中项
     */
    public void delSelect() {
        for (int i = 0, j=0; i < selectArrays.size(); i++,j++) {
            Integer position = selectArrays.get(i);
            L.e("删除position="+position+" i="+i+"   "+mData.get(position-i).toString());
           remove(position-i);
        }
        selectArrays.clear();
    }
    /**
     * 取消全选
     */
    public void cancleAll() {
        selectArrays.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelectArrays() {
        return selectArrays;
    }

    public String getAllSelectIds(){
        if (selectArrays.size()==0){
            ToastUtil.showLong("未选中！");
            return "";
        }
        StringBuffer buffer =new StringBuffer();
        for (int i = 0; i < selectArrays.size(); i++) {
            GoodsInfo dataBean = mData.get(selectArrays.get(i));
            String goodsId = dataBean.getItemId();
            buffer.append(goodsId);
            if (i<selectArrays.size()-1)buffer.append(",");
        }
        return buffer.toString();
    }

    public void setShowSelectBtn(boolean showSelectBtn) {
        isShowSelectBtn = showSelectBtn;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo item) {
        helper.setText(R.id.tv_title,item.getItemTitle())
                .setText(R.id.tv_shop_title,item.getShopName())
                .setText(R.id.tv_volume,"已售"+item.getItemSale())
                .setText(R.id.tv_commission_price,"预估收益 ￥"+item.getCommision())
                .setText(R.id.tv_reserve_price,"原价￥"+item.getItemPrice())
                .setText(R.id.tv_zk_final_price,SpannableStringUtils.getBuilder("券后￥").setProportion(0.5f)
                        .append(item.getItemEndPrice()).create()).addOnClickListener(R.id.rb_select);


        CouponBg couponBg = helper.getView(R.id.coupon_price);
        couponBg.setCouponText(item.getCouponMoney()+"元");
        //加入删除线
        TextView tvReservePrice = helper.getView(R.id.tv_reserve_price);
        tvReservePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //是否显示原价
        String reserve_price = item.getItemPrice();
        String zk_final_price = item.getItemEndPrice();
        helper.getView(R.id.tv_reserve_price).setVisibility(reserve_price.equals(zk_final_price)? View.INVISIBLE:View.VISIBLE);
        //商品图片
        ImageView ivPic = helper.getView(R.id.iv_pict_url);
        Glide.with(mContext).load(item.getItemPic()).apply(new RequestOptions().transform(new RoundedCorners(9))).into(ivPic);

        //根据类型，显示不同的小图标，淘宝或天猫
        ImageView ivUserType = helper.getView(R.id.iv_user_type);
        boolean is_tmall = item.getIsTmall()==1;
        Glide.with(mContext).load(is_tmall?R.mipmap.icon_tmall:R.mipmap.icon_tb).into(ivUserType);

        //是否显示选中按钮
        RadioButton rbSelect = helper.getView(R.id.rb_select);
        if (isShowSelectBtn){
            rbSelect.setVisibility(View.VISIBLE);
            //是否选中
            boolean contains = selectArrays.contains(helper.getLayoutPosition());
            rbSelect.setChecked(contains);
        }else {
            rbSelect.setVisibility(View.GONE);
        }



    }

}
