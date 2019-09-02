package com.qiqia.duosheng;

import android.webkit.JavascriptInterface;

import cn.com.smallcake_utils.ToastUtil;

public class AndroidJs{
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        ToastUtil.showShort(msg);
    }
}
