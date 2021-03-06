package cn.com.smallcake_utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * MyApplication --  cn.com.smallcake_utils
 * Created by Small Cake on  2017/9/7 17:20.
 * show notification
 * just a simple example
 * maybe you can write more each other notice
 * 新增8.0的兼容性
 * 跳转到主页
 * Intent intent = new Intent(getApplicationContext(), MainActivity.class);
 * PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,  intent, PendingIntent.FLAG_CANCEL_CURRENT);
 * notification.setContentIntent(contentIntent)
 */

public class NotificationUtils {
    /**
     *  简易通知显示
     *  必要三元素：smallIcon,contentTitle,contentText
     */
    public static void showNotice(Context context, CharSequence msg){
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("渠道ID", "优惠券商品", NotificationManager.IMPORTANCE_LOW);
                manager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(context, "渠道ID");
            }else {
                builder = new NotificationCompat.Builder(context);
            }
            Notification notification = builder
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(msg)
                    .build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            manager.notify(0,notification);
    }

    /**
     * use for progress notification
     * @param context
     * @param smallIcon
     * @param title
     * @param msg
     * @param progress
     */
    public static void showNoticeProgress(Context context, int smallIcon, CharSequence title, CharSequence msg,int progress){
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("测试", title, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "测试");

        }else {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentText(msg)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setProgress(100,progress,false)
                .setOngoing(true).build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(0,notification);

    }
}
