package com.qiqia.duosheng.utils;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;

import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;

public class DemoTradeCallback implements AlibcTradeCallback {

    @Override
    public void onFailure(int errCode, String errMsg) {
        ToastUtil.showLong("电商SDK出错,错误码="+errCode+" / 错误消息="+errMsg);
        L.e("电商SDK出错,错误码="+errCode+" / 错误消息="+errMsg);
    }
    @Override
    public void onTradeSuccess(TradeResult tradeResult) {
        L.e("电商SDK成功"+tradeResult.toString());
        if(tradeResult.resultType.equals(tradeResult.resultType.TYPECART)){
            ToastUtil.showLong("加购成功");
        }else if (tradeResult.resultType.equals(tradeResult.resultType.TYPEPAY)){
            ToastUtil.showLong("支付成功,成功订单号为"+tradeResult.payResult.paySuccessOrders);
            L.e("支付成功,成功订单号为"+tradeResult.payResult.paySuccessOrders);
        }
    }
}
