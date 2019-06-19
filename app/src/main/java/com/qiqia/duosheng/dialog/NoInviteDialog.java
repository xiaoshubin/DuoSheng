package com.qiqia.duosheng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.qiqia.duosheng.R;

/**
 * 没有邀请码
 */
public class NoInviteDialog extends Dialog implements View.OnClickListener {
    public NoInviteDialog(Context context) {
        super(context, R.style.Theme_Ios_Dialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_invite_dialog_layout);
        findViewById(R.id.btn_ok).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
       cancel();
    }
}
