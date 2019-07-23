package com.qiqia.duosheng;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.lsxiao.apollo.core.Apollo;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.utils.OnFrontUtil;
import com.smallcake.okhttp.retrofit2.RetrofitHttp;

import org.litepal.LitePal;

import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SmallUtils;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MyApplication extends Application {
    public static boolean isRunInBackground=true;//首次从后台进入

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        SmallUtils.init(this);
        RetrofitHttp.init(this, Contants.BASE_URL);
        Apollo.init(AndroidSchedulers.mainThread(), this);

        JPushInterface.init(this);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);

//        Fragmentation.builder()
//                .stackViewMode(Fragmentation.BUBBLE)
//                .debug(BuildConfig.DEBUG)
//             .install();

        LitePal.initialize(this);
        //阿里百川：淘宝授权，跳淘宝购买，领券，
        initAliBc();
        //前台监听：对当前应用生命周期的监听，方便知道应用在前台还是后台，以此来确定是否需要智能搜索
        OnFrontUtil.listenOnFront(this, new OnFrontUtil.OnFrontCallback() {
            @Override
            public void onFront() {
                //数据上报，App回到前台
                L.e("App回到前台");
                isRunInBackground=true;
            }
        });

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

    private void initAliBc() {
        try {
            /**
             * 百川电商SDK初始化【异步】
             * @param context 建议设置Application（必填）
             * @param initResultCallback  初始化状态信息回调（可以为null）
             */
            AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
                @Override
                public void onSuccess() {
                    L.e("百川SDK初始化成功");
                    AlibcTradeSDK.setTaokeParams(new AlibcTaokeParams(Contants.PID, "", ""));//设置淘客全局参数
                    AlibcTradeSDK.setChannel("多省APP推广位", Contants.PID);
                    Apollo.emit(EventStr.ALIBCTRADESDK_INIT_SUCCESS);
                }

                @Override
                public void onFailure(int code, String msg) {
                    //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
                    L.e("百川初始化失败： " + code + " msg==" + msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //方法数量过多，合并
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
