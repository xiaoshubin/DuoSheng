package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class CashSuccessFragment extends BaseBarFragment {


    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;


    @Override
    public int setLayout() {
        return R.layout.fragment_cash_success;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("提现 ");
        ivIcon.setScaleX(0f);
        ivIcon.setScaleY(0f);
        ivIcon.animate()
                .scaleXBy(0f).scaleYBy(0f)
                .scaleX(1f).scaleY(1f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(800).start();
    }

    @OnClick({R.id.btn_confirm})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                pop();
                break;
        }
    }

}
