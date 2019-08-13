package com.qiqia.duosheng.dialog;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.mine.NoticeSetFragment;

public class PushSetPop extends CenterPopupView {
    public PushSetPop(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_push_set;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btn_open_push).setOnClickListener(v -> {
            goWhiteBarActivity(getContext(), NoticeSetFragment.class.getSimpleName());
            dismiss();
        });
    }
    protected void goWhiteBarActivity(Context _mActivity,String targetFragment){
        Intent intent = new Intent(_mActivity, WhiteBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT,targetFragment);
        _mActivity.startActivity(intent);
    }
}
