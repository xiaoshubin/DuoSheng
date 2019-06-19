package com.qiqia.duosheng.custom;

import android.os.CountDownTimer;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {
    TextView tvGetCode;
    public TimeCount(long millisInFuture, long countDownInterval, TextView tvGetCode) {
        super(millisInFuture, countDownInterval);
        this.tvGetCode = tvGetCode;
        try {
            tvGetCode.setClickable(false);
            tvGetCode.setText("已发送" + millisInFuture / 1000 + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        try {
            tvGetCode.setText("已发送" + millisUntilFinished / 1000 + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        try {
            tvGetCode.setClickable(true);
            tvGetCode.setText(" 重新发送 ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
