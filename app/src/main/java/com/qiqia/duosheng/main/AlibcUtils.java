package com.qiqia.duosheng.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.qiqia.duosheng.activities.TransparentBarActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.inject.DataProvider;
import com.qiqia.duosheng.utils.DemoTradeCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.smallcake_utils.AppUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class AlibcUtils {
    //注意事项：使用高佣转链功能，一定要使用淘宝联盟后台的账号，传入联盟的APPKEY以及adzone ID；

    //是否登录了淘宝客服端
    public static boolean isLoginTb() {
        return AlibcLogin.getInstance().isLogin();
    }
    //未登录的情况下，开启登录，并获取登录后的回调用户信息
    public static void getTbSession(Activity activity,AlibcLoginCallback alibcLoginCallback) {
        AlibcLogin.getInstance().showLogin(activity, alibcLoginCallback);
    }

    public static void openUrlNative(Activity activity, String url) {
        openUrl(activity, url, OpenType.Native);
    }

    public static void openUrlH5(Activity activity, String url) {
        openUrl(activity, url, OpenType.H5);
    }

    public static void openDetailNative(Activity activity, String goodsId) {
        openDetail(activity, goodsId, OpenType.Native);
    }

    public static void openDetailH5(Activity activity, String goodsId) {
        openDetail(activity, goodsId, OpenType.H5);
    }


    /**
     * 以原生淘宝的方式打开一个链接，
     * 例如：领取优惠券链接
     */

    private static void openUrl(Activity activity, String url, OpenType type) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcPage(url);
        AlibcShowParams showParams = new AlibcShowParams(type, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }

    /**
     * 通过id打开一个详情页面
     */
    private static void openDetail(Activity activity, String goodsId, OpenType type) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcDetailPage(goodsId);
        AlibcShowParams showParams = new AlibcShowParams(type, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }
    /**
     * 打开电商组件, 使用默认的webview打开
     *
     * @param activity             必填
     * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
     * @param showParams           show参数
     * @param taokeParams          淘客参数
     * @param trackParam           yhhpass参数
     *  tradeProcessCallback 交易流程的回调，必填，不允许为null；
     * @return 0标识跳转到手淘打开了,1标识用h5打开,-1标识出错
     */
    private static void tradeShow(Activity activity, AlibcBasePage tradePage, AlibcShowParams showParams, AlibcTaokeParams taokeParams, Map trackParam) {
        AlibcTrade.show(activity, tradePage, showParams, taokeParams, trackParam, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            }
            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
            }
        });
    }
    /**
     * 淘宝H5登录授权
     * https://oauth.taobao.com/authorize?
     * response_type=code
     * &client_id=26071071
     * &redirect_uri=http://adms.ds.jxmj.com/v4.php?ctr=DataSettings.code
     * &state=1234
     * &view=web
     */
    public static void loginH5(Context context, DataProvider dataProvider){
        String response_type ="code";
        String client_id ="26071071";
        String redirect_uri ="http://adms.ds.jxmj.com/v4.php?ctr=DataSettings.code";
        dataProvider.tbLogin.oauthUrl(response_type,client_id,redirect_uri).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    goWebViewFragment(context,"淘宝授权",responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private static void goWebViewFragment(Context context, String title, String url){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        Intent intent = new Intent(context, TransparentBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, WebViewFragment.class.getSimpleName());
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }


}
