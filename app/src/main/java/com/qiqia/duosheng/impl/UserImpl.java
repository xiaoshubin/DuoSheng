package com.qiqia.duosheng.impl;

import com.qiqia.duosheng.api.UserApi;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.VipUpgradeResponse;
import com.qiqia.duosheng.mine.bean.MyCollection;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.RequestBody;

import static com.smallcake.okhttp.retrofit2.RetrofitComposeUtils.bindIoUI;

public class UserImpl implements UserApi {
    @Inject
    UserApi userApi;

    @Inject
    public UserImpl() {
    }

    @Override
    public Observable<BaseResponse<VipUpgradeResponse>> upgradeInfo(String uid) {
        return bindIoUI(userApi.upgradeInfo(uid));
    }

    @Override
    public Observable<BaseResponse> upgrade(String uid, String token, String phone) {
        return bindIoUI(userApi.upgrade(uid, token, phone));
    }

    @Override
    public Observable<BaseResponse> phone(String uid, String phone, String smsCode, String token) {
        return bindIoUI(userApi.phone(uid, phone, smsCode, token));
    }

    @Override
    public Observable<BaseResponse> info(String uid, Integer type, String value, String token) {
        return bindIoUI(userApi.info(uid, type, value, token));
    }

    @Override
    public Observable<BaseResponse> ali(String uid, String token, String phone, String smsCode, String account, String name) {
        return bindIoUI(userApi.ali(uid, token, phone, smsCode, account, name));
    }

    @Override
    public Observable<BaseResponse<String>> updateAvatar(RequestBody requestBody) {
        return bindIoUI(userApi.updateAvatar(requestBody));
    }

    @Override
    public Observable<BaseResponse<String>> uploadImg(RequestBody requestBody) {
        return bindIoUI(userApi.uploadImg(requestBody));
    }

    @Override
    public Observable<BaseResponse> feedBack(String uid, String token, String content, String imgs) {
        return bindIoUI(userApi.feedBack( uid,  token,  content,  imgs));
    }

    @Override
    public Observable<BaseResponse<String>> getWx(String uid) {
        return bindIoUI(userApi.getWx(uid));
    }

    @Override
    public Observable<BaseResponse> collect(String uid, String token, String id, Integer type, String couponMoney, String commision,String couponLink) {
        return bindIoUI(userApi.collect(uid, token, id, type, couponMoney, commision,couponLink));
    }

    @Override
    public Observable<BaseResponse<MyCollection>> collectList(String uid, String token, Integer page) {
        return bindIoUI(userApi.collectList(uid, token, page));
    }

    @Override
    public Observable<BaseResponse> delCol(String uid, String token, String id) {
        return bindIoUI(userApi.delCol(uid, token, id));
    }

    @Override
    public Observable<BaseResponse> isCollect(String uid, String token, String id) {
        return bindIoUI(userApi.isCollect(uid, token, id));
    }

    @Override
    public Observable<BaseResponse> bindDs(String uid, String token, String code) {
        return bindIoUI(userApi.bindDs(uid, token, code));
    }


}
