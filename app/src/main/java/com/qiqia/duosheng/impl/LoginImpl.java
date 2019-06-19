package com.qiqia.duosheng.impl;

import com.qiqia.duosheng.api.LoginApi;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.bean.User;

import javax.inject.Inject;

import cn.com.smallcake_utils.MobileUtils;
import io.reactivex.Observable;

import static com.smallcake.okhttp.retrofit2.RetrofitComposeUtils.bindIoUI;


public class LoginImpl {
    @Inject
    LoginApi loginApi;
    @Inject
    public LoginImpl() {}


    public Observable<BaseResponse<User>> register(String inviteCode, Integer wxId, String phone, String smsCode) {
        return bindIoUI(loginApi.register(inviteCode,  wxId,  phone,  smsCode,  Contants.ANDROID, MobileUtils.getPseudoUnique()));
    }

    //验证码发送类型 1 短信 2 语音
    public Observable<BaseResponse> sendSms(String phone) {
        return bindIoUI(loginApi.sendSms(phone,1));
    }
    //验证码校验
    public Observable<BaseResponse> checkSms(String phone,String smsCode) {
        return bindIoUI(loginApi.checkSms(phone,smsCode));
    }

    public Observable<BaseResponse<User>> login(String code, String phone, String smsCode) {
        return bindIoUI(loginApi.login( code, Contants.ANDROID, MobileUtils.getPseudoUnique(),  phone,  smsCode));
    }
}
