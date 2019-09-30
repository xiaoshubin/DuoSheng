package com.qiqia.duosheng.custom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.share.bean.ShareList;
import com.qiqia.duosheng.utils.Code2Utils;
import com.qiqia.duosheng.utils.DataLocalUtils;

import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ShareUtils;
import cn.com.smallcake_utils.SpannableStringUtils;

/**
 * 分享首页分享赚钱列表数据
 */
public class ShareBottomShareListPopup extends BottomPopupView {
    ShareList.DataBean item;
    Activity activity;
    public ShareBottomShareListPopup(@NonNull Activity context, ShareList.DataBean info) {
        super(context);
        this.activity = context;
        this.item = info;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_share_img_layout;
    }

    // 最大高度为Window的0.85
    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.95f);
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        View view = findViewById(R.id.layout_img);
        //设置对应商品信息
        ImageView ivIcon = view.findViewById(R.id.iv_icon);
        ImageView ivGoodsPic = view.findViewById(R.id.iv_goods_pic);
        ImageView ivCode2 = view.findViewById(R.id.iv_code2);

        TextView tvTitle = view.findViewById(R.id.tv_goods_title);
        TextView tvPrice = view.findViewById(R.id.tv_price);
        TextView tvOldPreceSales = view.findViewById(R.id.tv_old_price_sales);
        CouponBg couponPrice = view.findViewById(R.id.coupon_price);
        TextView tvRecommendDesc = view.findViewById(R.id.tv_recommend_desc);

        ivIcon.setVisibility(GONE);
        String superCode="888888";
        try {
             superCode = DataLocalUtils.getUser().getSuperCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap qrCode = Code2Utils.createQRCode(superCode, 200);
        ivCode2.setImageBitmap(qrCode);


        tvTitle.setText(item.getItemTitle());
        tvPrice.setText(item.getItemEndPrice());
        SpannableStringBuilder oldPriceSales = SpannableStringUtils.getBuilder("￥" + item.getItemPrice() + "原价").setStrikethrough()
                .append("    点击量" + item.getClick()).create();
        tvOldPreceSales.setText(oldPriceSales);
        couponPrice.setCouponText(item.getCouponMoney()+"元");
        tvRecommendDesc.setText( Html.fromHtml(Html.fromHtml(item.getCopyContent()).toString()));
        String pic = item.getItemPic();
        L.e("要分享的图片=="+pic);
        if (pic.startsWith("http")&&(pic.endsWith(".jpg"))||(pic.endsWith(".png"))){
            Glide.with(this.getContext()).load(pic).into(ivGoodsPic);
        }else {
            ivGoodsPic.setVisibility(GONE);
        }


        findViewById(R.id.btn_share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareBottomShareListPopup.this.dismiss();
                //指定视图的宽高
                layoutView(activity,view);
                //转换为bitmap用于分享
                Bitmap bitmap = loadBitmapFromView(view);
                ShareUtils.shareImg(activity,"分享",activity.getString(R.string.app_name),"多省,用的多,省的多",bitmap);
            }
        });

    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
    private void layoutView(Activity activity, View v) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        // 指定整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int measuredHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }
}
