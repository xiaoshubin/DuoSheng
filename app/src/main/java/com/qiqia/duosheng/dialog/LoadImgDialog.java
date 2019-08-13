package com.qiqia.duosheng.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.qiqia.duosheng.R;


public class LoadImgDialog extends AlertDialog {
	String text;
	public LoadImgDialog(Context context, String text) {
		super(context, R.style.DialogStyle);
		this.text = text;
	}
	public LoadImgDialog(Context context) {
		super(context,R.style.DialogStyle);
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_dialog);
		setCanceledOnTouchOutside(false);
		((TextView)findViewById(R.id.tv_load_dialog)).setText(TextUtils.isEmpty(text)?"数据加载中...":text);
	}
	
}
