package cn.com.smallcake_utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * MyApplication --  cn.com.smallcake_utils
 * Created by Small Cake on  2017/12/18 14:09.
 */

public class WebUtils {
    /**
     * 跳转到手机默认浏览器相关网页
     * 如果是打开第三方应用，未安装会报错，需要捕获
     * @param context
     * @param url
     */
    public static void goWeb(Context context, String url){
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            L.e("没有安装此第三方应用！");
        }
    }
}
