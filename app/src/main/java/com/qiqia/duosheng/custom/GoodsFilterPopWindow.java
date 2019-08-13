package com.qiqia.duosheng.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lxj.xpopup.impl.PartShadowPopupView;
import com.qiqia.duosheng.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.smallcake_utils.L;

public class GoodsFilterPopWindow extends PartShadowPopupView {
    @BindView(R.id.et_low_price)
    EditText etLowPrice;
    @BindView(R.id.et_high_price)
    EditText etHighPrice;
    @BindView(R.id.rb_tmall)
    RadioButton rbTmall;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.rg_store)
    RadioGroup rgStore;

    private OnFilterListener listener;
    interface OnFilterListener{
        void onFilterConfirm(int lowPrice,int highPrice);
    }

    public void setListener(OnFilterListener listener) {
        this.listener = listener;
    }

    public GoodsFilterPopWindow(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_goods_filter;
    }

    @Override
    protected void onCreate() {
        ButterKnife.bind(this);
        super.onCreate();
        rbAll.setChecked(true);
    }

    @OnClick({R.id.btn_cancle,R.id.btn_confirm})
    public void doClicks(View view){
        switch (view.getId()){
            case R.id.btn_cancle:
                this.dismiss();
                break;
            case R.id.btn_confirm:
                onConfirm();
                break;
        }
    }

    private void onConfirm() {
        L.e("onConfirm");
        String low = etLowPrice.getText().toString();
        String high = etHighPrice.getText().toString();
        int lowPrice=0;
        int highPrice=0;
        if (!TextUtils.isEmpty(low)) lowPrice = Integer.parseInt(low);
        if (!TextUtils.isEmpty(high)) highPrice = Integer.parseInt(high);
        if (highPrice>lowPrice){
            if (listener!=null)listener.onFilterConfirm(lowPrice,highPrice);
        }else {
            etLowPrice.setText(high);
            etHighPrice.setText(low);
            if (listener!=null)listener.onFilterConfirm(highPrice,lowPrice);
        }
        this.dismiss();

    }

}
