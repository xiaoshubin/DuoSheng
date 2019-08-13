package com.qiqia.duosheng.dialog;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.qiqia.duosheng.R;

import cn.com.smallcake_utils.ClipboardUtils;

public class MyServiceWxDialog extends CenterPopupView {
    String wxkfAccount;
    public MyServiceWxDialog(@NonNull Context context,String wxkf) {
        super(context);
        wxkfAccount = wxkf;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_my_service_wx;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvWxkfAccount = findViewById(R.id.tv_wxkf_account);
        tvWxkfAccount.setText(wxkfAccount);
        findViewById(R.id.iv_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 关闭弹窗
            }
        });
        findViewById(R.id.btn_copy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copy(wxkfAccount);
                dismiss(); // 关闭弹窗
            }
        });
    }
}
