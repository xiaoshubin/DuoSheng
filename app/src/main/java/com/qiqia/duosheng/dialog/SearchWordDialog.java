package com.qiqia.duosheng.dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.search.SearchFragment;

/**
 * 智能搜索
 */
public class SearchWordDialog extends CenterPopupView {
    String msg;
    TextView tvMsg;
    private Activity activity;
    public SearchWordDialog(@NonNull Activity context, String msg) {
        super(context);
        this.activity = context;
        this.msg = msg;
    }
    public SearchWordDialog setMsg(String msg) {
        this.msg = msg;
        if (tvMsg!=null)tvMsg.setText(msg);
        return this;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.search_word_dialog_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvMsg = findViewById(R.id.tv_search_msg);
        if (!TextUtils.isEmpty(msg))tvMsg.setText(msg);
        findViewById(R.id.btn_search).setOnClickListener(v -> {
            Intent intent = new Intent(activity, WhiteBarActivity.class);
            intent.putExtra(Contants.LOAD_FRAGMENT, SearchFragment.class.getSimpleName());
            intent.putExtra("keyWord", msg);
            activity.startActivity(intent);
            dismiss();
        });
        findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());
    }
}
