package com.qiqia.duosheng.jpush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.qiqia.duosheng.MainActivity;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.bean.IndexAd;
import com.qiqia.duosheng.utils.GsonUtils;

import java.io.File;

import cn.com.smallcake_utils.AppUtils;
import cn.com.smallcake_utils.BitmapUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.TimeUtils;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * 这里我们只处理我们关心的自定义消息：ACTION_MESSAGE_RECEIVED
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
            processCustomMessage(context, bundle);
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        //1.这里我们拿到了极光推送过来的自定义消息
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        L.e("title==" + title + "  message==" + message);
        //2.我们把这条消息拿到后，直接通知显示
        try {
            loadJson(context, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadJson(Context context, String msg) throws Exception{
        IndexAd indexAd = GsonUtils.jsonToObj(msg, IndexAd.class);
        String url = indexAd.getImg();//图片
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                File file = Glide.with(context).download(url).submit().get();
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                emitter.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        showNotice(context, bitmap, indexAd);
                    }
                });
    }

    private void showNotice(Context context, Bitmap bitmap, IndexAd indexAd) {
        String title = indexAd.getTitle();
        // 使用remoteViews去加载自定义布局
        RemoteViews remoteViews = new RemoteViews(AppUtils.getAppPackageName(), R.layout.notification_custom);
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_price, "￥"+indexAd.getEndPrice()+"元");
        remoteViews.setTextViewText(R.id.tv_time, TimeUtils.tsToMs(indexAd.getTime()));
        remoteViews.setImageViewBitmap(R.id.iv_pic, BitmapUtils.setRoundedCorner(bitmap,9));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("渠道ID", "优惠券商品", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "渠道ID");
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(title)
                .setContentIntent(contentIntent)
                .setContent(remoteViews)
                .setAutoCancel(true)
                .build();
        manager.notify(0, notification);
    }
}
