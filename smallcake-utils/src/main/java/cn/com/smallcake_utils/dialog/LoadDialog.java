package cn.com.smallcake_utils.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import cn.com.smallcake_utils.R;


public class LoadDialog extends AlertDialog {
	String text;
	public LoadDialog(Context context, String text) {
		super(context, R.style.Theme_Loading_Dialog);
		this.text = text;
	}
	public LoadDialog(Context context) {
		super(context,R.style.Theme_Loading_Dialog);
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
