package com.qiqia.duosheng.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import javax.inject.Inject;

import cn.com.smallcake_utils.R;
import cn.com.smallcake_utils.SmallUtils;


public class QLoadDialog extends Dialog {
	String text;

	@Inject
	public QLoadDialog() {
		super(SmallUtils.getApp(), R.style.Theme_Loading_Dialog);
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smallcake_utils_loading_dialog);
		setCanceledOnTouchOutside(false);
		((TextView)findViewById(R.id.tv_load_dialog)).setText(TextUtils.isEmpty(text)?"数据加载中...":text);
	}
	
}
