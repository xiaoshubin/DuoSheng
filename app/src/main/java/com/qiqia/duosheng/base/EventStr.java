package com.qiqia.duosheng.base;

public interface EventStr {
    String WX_LOGIN_CODE="wx_login_code";//获取到微信登录后的code的通知
    String LOGIN_SUCCESS="login_success";//登录成功
    String ALIBCTRADESDK_INIT_SUCCESS="AlibcTradeSDK_init_success";//阿里百川初始化成功

    String UPDATE_NICKNAME="update_nickname";//修改昵称
    String UPDATE_PHONE="update_phone";//修改手机号
    String UPDATE_WXKF ="update_wxkf" ;//修改微信客服
}
