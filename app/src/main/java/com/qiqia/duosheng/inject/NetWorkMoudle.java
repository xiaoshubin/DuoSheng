package com.qiqia.duosheng.inject;

import android.content.Context;

import com.qiqia.duosheng.api.LoginApi;
import com.qiqia.duosheng.api.ShopApi;
import com.qiqia.duosheng.api.TbLoginApi;
import com.qiqia.duosheng.api.UserApi;
import com.qiqia.duosheng.dialog.LoadImgDialog;
import com.smallcake.okhttp.retrofit2.RetrofitHttp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetWorkMoudle {
    Context context;
    public NetWorkMoudle(Context context) {
        this.context = context;
    }
    @Singleton
    @Provides
    public Context providerContext(){
        return context;
    }
    @Singleton
    @Provides
    public LoadImgDialog providerLoadDialog(Context context){
        return new LoadImgDialog(context);
    }
    //登录部分
    @Singleton
    @Provides
    public LoginApi providerLoginApi(){
        return RetrofitHttp.create(LoginApi.class);
    }
    @Singleton
    @Provides
    public ShopApi providerShopApi(){
        return RetrofitHttp.create(ShopApi.class);
    }
    @Singleton
    @Provides
    public TbLoginApi providerTbLoginApi(){
        return RetrofitHttp.getRetrofit("https://oauth.m.taobao.com").create(TbLoginApi.class);
    }
    @Singleton
    @Provides
    public UserApi providerUserApi(){
        return RetrofitHttp.create(UserApi.class);
    }


}
