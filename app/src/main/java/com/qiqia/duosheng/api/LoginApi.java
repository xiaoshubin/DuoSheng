package com.qiqia.duosheng.api;

import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApi {


    /**
     * Code	是	str	微信code
     * Type	是	int	设备类型 0网页 1安卓 2ios 3wp
     * Dev	是	str	设备特征吗-用于推送
     * Phone	是	str	电话号码
     * SmsCode	是	str	短信验证码
     * @return
     */
    @FormUrlEncoded
    @POST("v4.php?ctr=App_Login.Login")
    Observable<BaseResponse<User>> login(@Field("Code")String code, @Field("Type")Integer type, @Field("Dev")String dev, @Field("Phone")String phone, @Field("SmsCode")String smsCode);

    /**
     * Phone	是	int	手机号码
     * Type	否	int	验证码发送类型 1 短信 2 语音
     * @param phone
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("v4.php?ctr=App_Login.SendSms")
    Observable<BaseResponse> sendSms(@Field("Phone")String phone,@Field("Type")Integer type);

    /**
     *Phone	是	str	手机号码
     * SmsCode	是	str	短信验证码
     * @param phone
     * @param smsCode
     * @return
     */
    @FormUrlEncoded
    @POST("v4.php?ctr=App_Login.CheckSms")
    Observable<BaseResponse> checkSms(@Field("Phone")String phone,@Field("SmsCode")String smsCode);

    /**
     * Code	是	str	邀请码
     * WxId	否	int	微信id
     * Phone	是	str	电话号码
     * SmsCode	是	str	短信验证码
     * Type	是	int	设备类型 0网页 1安卓 2ios 3wp
     * Dev	是	str	设备特征吗-用于推送
     * @param phone
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("v4.php?ctr=App_Login.Register")
    Observable<BaseResponse<User>> register(@Field("Code")String code,@Field("WxId")Integer wxId,@Field("Phone")String phone,@Field("SmsCode")String smsCode,@Field("Type")Integer type,@Field("Dev")String dev);


}
