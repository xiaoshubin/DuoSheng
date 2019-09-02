package com.qiqia.duosheng.impl;

import com.qiqia.duosheng.api.TbLoginApi;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.TbTokenResponse;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static com.smallcake.okhttp.retrofit2.RetrofitComposeUtils.bindIoUI;

public class TbLoginImpl implements TbLoginApi {
    @Inject
    TbLoginApi tbLoginApi;
    @Inject
    public TbLoginImpl() {
    }
    @Override
    public Observable<TbTokenResponse> token(String client_id, String client_secret, String grant_type, String code, String redirect_uri, String view) {
        return bindIoUI(tbLoginApi.token( client_id,  client_secret,  grant_type,  code,  redirect_uri,  view));
    }

    @Override
    public Observable<ResponseBody> oauthUrl(String response_type, String client_id, String redirect_uri) {
        return bindIoUI(tbLoginApi.oauthUrl( response_type,  client_id,  redirect_uri));
    }

    @Override
    public Observable<BaseResponse<String>> getSqUrl(String uid) {
        return bindIoUI(tbLoginApi.getSqUrl(uid));
    }

}
