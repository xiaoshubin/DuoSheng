package com.qiqia.duosheng.api;

import com.qiqia.duosheng.bean.TbTokenResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TbLoginApi {
    /**
     client_id	必选	 	等同于AppKey，创建应用时获得。
     client_secret	必选	 	等同于AppSecret，创建应用时获得。
     grant_type	必选	authorization_code	授权类型 ，值为authorization_code
     code	必选	 	上一步获取code得到
     redirect_uri	必选	 	可填写应用注册时回调地址域名。redirect_uri指的是应用发起请求时，所传的回调地址参数，在用户授权后应用会跳转至redirect_uri。要求与应用注册时填写的回调地址域名一致或顶级域名一致 。
     state	可选	 	可自定义，如1212等；维持应用的状态，传人值与返回值保持一致。
     view	必选	 wap	Wap对应无线端的浏览器页面样式。
     */
    @FormUrlEncoded
    @POST("token")
    Observable<TbTokenResponse> token(@Field("client_id") String client_id, @Field("client_secret") String client_secret, @Field("grant_type") String grant_type, @Field("code") String code, @Field("redirect_uri") String redirect_uri, @Field("view") String view);

    /**
     * 淘宝H5登录授权
     * https://oauth.taobao.com/authorize?
     * response_type=code
     * &client_id=26071071
     * &redirect_uri=http://adms.ds.jxmj.com/v4.php?ctr=DataSettings.code
     * &state=1234
     * &view=web
     */
    @FormUrlEncoded
    @POST("authorize")
    Observable<ResponseBody> oauthUrl(@Field("response_type") String response_type, @Field("client_id") String client_id, @Field("redirect_uri") String redirect_uri);
}
