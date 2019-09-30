package com.qiqia.duosheng.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.qiqia.duosheng.R;

/**
 * 开启辅助服务
 */
public class OpenAccessibilityServicePop extends CenterPopupView {
    private OnOpenListener listener;
    public interface OnOpenListener {
        void onOkClick();
    }

    public void setListener(OnOpenListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_open_accessibility_service;
    }
    public OpenAccessibilityServicePop(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 关闭弹窗
            }
        });
        findViewById(R.id.tv_open).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener!=null)listener.onOkClick();
            }
        });

    }
}
