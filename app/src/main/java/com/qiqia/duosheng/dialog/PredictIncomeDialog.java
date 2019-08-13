package com.qiqia.duosheng.dialog;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.qiqia.duosheng.R;

public class PredictIncomeDialog extends CenterPopupView {
    String title;
    String msg;
    public PredictIncomeDialog(@NonNull Context context,String title,String msg) {
        super(context);
        this.title = title;
        this.msg = msg;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_predict_income;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvMsg = findViewById(R.id.tv_msg);
        tvTitle.setText(title);
        tvMsg.setText(msg);
        findViewById(R.id.tv_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
