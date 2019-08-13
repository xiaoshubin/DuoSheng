package com.qiqia.duosheng.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;

import com.lxj.xpopup.core.AttachPopupView;
import com.qiqia.duosheng.R;
//分享赚提示弹出框
public class ShareTakePop extends AttachPopupView {
    public ShareTakePop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_share_take;
    }
    //如果要自定义弹窗的背景，不要给布局设置背景图片，重写这个方法返回一个Drawable即可
    @Override
    protected Drawable getPopupBackground() {
        return getResources().getDrawable(R.drawable.transparent_bg);
    }
}
