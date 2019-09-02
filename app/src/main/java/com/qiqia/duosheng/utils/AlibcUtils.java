package com.qiqia.duosheng.utils;

import android.app.Activity;

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
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.android.trade.page.AlibcShopPage;

import java.util.HashMap;
import java.util.Map;

import cn.com.smallcake_utils.AppUtils;

/**
 * 注意事项：使用高佣转链功能，一定要使用淘宝联盟后台的账号，传入联盟的APPKEY以及adzone ID；
 *
 *
 */
public class AlibcUtils {

    //未登录的情况下，开启登录，并获取登录后的回调用户信息
    public static void getTbSession(Activity activity,AlibcLoginCallback alibcLoginCallback) {
        AlibcLogin.getInstance().showLogin(activity, alibcLoginCallback);
    }

    public static void openUrlNative(Activity activity, String url) {
        openUrl(activity, url, OpenType.Native);
    }

    public static void openDetailNative(Activity activity, String goodsId) {
        openDetail(activity, goodsId, OpenType.Native);
    }




    /**
     * 以原生淘宝的方式打开【一个Url页面】，
     * @param url 例如：领取优惠券链接
     *            调用了AlibcTrade.show方法的Activity都需要调用AlibcTradeSDK.destory()
     */
    private static void openUrl(Activity activity, String url, OpenType type) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcPage(url);
        AlibcShowParams showParams = new AlibcShowParams(type, true);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }

    /**
     * 通过id打开一个【商品详情】页面
     */
    private static void openDetail(Activity activity, String goodsId, OpenType type) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcDetailPage(goodsId);
        AlibcShowParams showParams = new AlibcShowParams(type, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }
    public static void openMiniDetail(Activity activity, String goodsId) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcDetailPage(goodsId);
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Auto, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }
    /**
     * 打开电商组件, 使用默认的webview打开
     * @param activity             必填
     * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
     * @param showParams           show参数
     * @param taokeParams          淘客参数
     * @param trackParam           yhhpass参数
     *  tradeProcessCallback 交易流程的回调，必填，不允许为null；
     */
    private static void tradeShow(Activity activity, AlibcBasePage tradePage, AlibcShowParams showParams, AlibcTaokeParams taokeParams, Map trackParam) {
        //show方法返回int 0标识跳转到手淘打开了,1标识用h5打开,-1标识出错
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
    //打开我的购物车
    public static void openMyCart(Activity activity) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcMyCartsPage();
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Auto, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }

    /**
     * 打开我的订单
     * @param activity 上下文
     * @param status 0：全部；1：待付款；2：待发货；3：待收货；4：待评价
     * @param allOrder 进行订单分域 flase只展示通过当前app下单的订单,实测无效
     */
    public static void openMyOrder(Activity activity,int status,boolean allOrder) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcMyOrdersPage(status,allOrder);
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Auto, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }
    //打开店铺
    public static void openShop(Activity activity,String shopId) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcShopPage(shopId);
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Auto, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new DemoTradeCallback());
    }

}
