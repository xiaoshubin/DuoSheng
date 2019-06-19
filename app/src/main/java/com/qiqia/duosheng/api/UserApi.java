package com.qiqia.duosheng.api;

import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.VipUpgradeResponse;
import com.qiqia.duosheng.mine.bean.MyCollection;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    /**
     * Uid	是	int	用户id
     *
     * @return
     */

    @GET("v4.php?ctr=App_User.UpgradeInfo")
    Observable<BaseResponse<VipUpgradeResponse>> upgradeInfo(@Query("Uid") String uid);

    @GET("v4.php?ctr=App_User.Upgrade")
    Observable<BaseResponse> upgrade(@Query("Uid") String uid, @Query("Token") String token, @Query("Phone") String phone);

    @GET("v4.php?ctr=App_User.Phone")
    Observable<BaseResponse> phone(@Query("uid") String uid, @Query("phone") String phone, @Query("smsCode") String smsCode, @Query("token") String token);

    /**
     * uid int是用户id
     * type int是修改的类型，默认为0。 0：用户名 1：性别 2：头像 3：客服微信
     * value string是修改的值
     * token string是用户token
     *
     * @return
     */
    @GET("v4.php?ctr=App_User.Info")
    Observable<BaseResponse> info(@Query("uid") String uid, @Query("type") Integer type, @Query("value") String value, @Query("token") String token);

    /**
     * uid int是用户id
     * token string是用户token
     * phone string是手机号
     * account string是用户支付宝账号
     * name string是用户支付宝姓名
     *
     * @return
     */
    @GET("v4.php?ctr=App_User.Ali")
    Observable<BaseResponse> ali(@Query("uid") String uid, @Query("token") String token, @Query("phone") String phone, @Query("smsCode") String smsCode, @Query("account") String account, @Query("name") String name);

    @POST("v4.php?ctr=App_User.UpdateAvatar")
    Observable<BaseResponse<String>> updateAvatar(@Body RequestBody requestBody);
    @POST("v4.php?ctr=App_User.UploadImg")
    Observable<BaseResponse<String>> uploadImg(@Body RequestBody requestBody);

    @GET("v4.php?ctr=App_User.FeedBack")
    Observable<BaseResponse> feedBack(@Query("uid") String uid, @Query("token") String token, @Query("content") String content, @Query("imgs") String imgs);

    @GET("v4.php?ctr=App_User.GetWx")
    Observable<BaseResponse<String>> getWx(@Query("uid") String uid);

    /**
     * uid int是用户id
     * token string是用户token
     * id string是商品id
     * type int否类型 默认0  0：好单库 1：淘宝全网
     * couponMoney string 否type为1时传  优惠券面额
     * commision string 否type为1时传  预估收入
     */
    @GET("v4.php?ctr=App_User.Collect")
    Observable<BaseResponse> collect(@Query("uid") String uid, @Query("token") String token, @Query("id") String id, @Query("type") Integer type,
                                             @Query("couponMoney") String couponMoney, @Query("commision") String commision,@Query("couponLink") String couponLink);
    @GET("v4.php?ctr=App_User.CollectList")
    Observable<BaseResponse<MyCollection>> collectList(@Query("uid") String uid, @Query("token") String token, @Query("page") Integer page);
    @GET("v4.php?ctr=App_User.DelCol")
    Observable<BaseResponse> delCol(@Query("uid") String uid, @Query("token") String token, @Query("id") String id);
    @GET("v4.php?ctr=App_User.IsCollect")
    Observable<BaseResponse> isCollect(@Query("uid") String uid, @Query("token") String token, @Query("id") String id);

    @GET("v4.php?ctr=App_User.BindDs")
    Observable<BaseResponse> bindDs(@Query("uid") String uid, @Query("token") String token, @Query("code") String code);
}
