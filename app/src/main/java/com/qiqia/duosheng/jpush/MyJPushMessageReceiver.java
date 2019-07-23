package com.qiqia.duosheng.jpush;

import android.content.Context;

import java.util.Set;

import cn.com.smallcake_utils.L;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 * 1.新的消息回调方式中相关回调类。
 * 2.新的 tag 与 alias 操作回调会在开发者定义的该类的子类中触发。
 * 3.手机号码设置的回调会在开发者定义的该类的子类中触发。
 * 4.新回调方式与旧的自定义Receiver兼容：
 * 配置了此Receiver以后，默认是也会发广播给旧Receiver的
 * 对于onMessage、onNotifyMessageArrived、onNotifyMessageOpened、onMultiActionClicked
 * 如果重写了这些方法，则需要调用super才会发给旧Receiver
 *
 * 该类为回调父类，开发者需要继承该类并在 Manifest 中配置您对应实现的类，接口操作的结果会在您配置的类中的如下方法中回调。
 * */
public class MyJPushMessageReceiver extends JPushMessageReceiver {
    //tag 增删查改的操作会在此方法中回调结果
    @Override
    public void onTagOperatorResult(Context context,JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        Set<String> tags = jPushMessage.getTags();
        int sequence = jPushMessage.getSequence();
        String s = tags.toString();
        L.e("sequence=="+sequence+"   JPushTag=="+s);
    }
    @Override
    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
        super.onCheckTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
    }
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
