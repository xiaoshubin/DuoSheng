package com.qiqia.duosheng.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.core.CenterPopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.TransparentBarActivity;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.bean.IndexAd;
import com.qiqia.duosheng.main.WebViewFragment;
import com.qiqia.duosheng.search.GoodsInfoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.smallcake_utils.SpannableStringUtils;

public class GoodsAdvertPop extends CenterPopupView {
    IndexAd item;
    @BindView(R.id.iv_goods_pic)
    ImageView ivGoodsPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    Activity activity;
    Unbinder bind;
    public GoodsAdvertPop(@NonNull Activity context, IndexAd data) {
        super(context);
        this.activity = context;
        this.item = data;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_goods_advert;
    }
    /**
     * type 1:产品，跳商品详情
     *      2.图片，链接跳转（有链接）
     *
     */
    @Override
    protected void onCreate() {
        super.onCreate();
        bind = ButterKnife.bind(this);
        Glide.with(this.getContext()).load(item.getImg()).into(ivGoodsPic);
        tvTitle.setText(item.getTitle());
        int type = item.getType();
        String url = item.getUrl();
        //价格是否显示，底部按钮文字变换
       if (type==1){
           SpannableStringBuilder endPrice = SpannableStringUtils.getBuilder("￥").setProportion(0.5f)
                   .append(item.getEndPrice())
                   .append(" 券后价").setProportion(0.6f).create();
           tvPrice.setText(endPrice);
       }else {
           tvPrice.setVisibility(GONE);
           btnBuy.setText(TextUtils.isEmpty(url)?"知道了！":"去看看");
       }
       //点击事件变换
        btnBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                switch (type){
                    case 1: goGoodsInfoFragment(item.getId()+"");break;
                    case 2:  if (!TextUtils.isEmpty(url)) goWebFragment(item.getTitle(),url);break;
                }
            }
        });

    }

    /**
     * 跳转到商品详情页
     * @param id
     */
    public void goGoodsInfoFragment(String id){
        Intent intent = new Intent(activity, WhiteBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, GoodsInfoFragment.class.getSimpleName());
        intent.putExtra("id",id);
        activity.startActivity(intent);
    }

    /**
     * 网页展示
     * @param title
     * @param url
     */
    private void goWebFragment(String title,String url){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        Intent intent = new Intent(activity, TransparentBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, WebViewFragment.class.getSimpleName());
        intent.putExtra("bundle",bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        bind.unbind();
    }

}
