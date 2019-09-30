package com.smallcake.qsh.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.smallcake.qsh.callback.OnConfigListener;
import com.smallcake.qsh.http.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;


public class ConfigJs {
    private static final String                   TAG           = "AndroidJs";
    private static       HashMap<String, Boolean> methedsSwitch = new HashMap();
    Activity activity;

    public void init(Activity content) {
        activity = content;
    }

    /**
     * url:当前页面的url地址
     * debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
     * appId: '', // 必填，公众号的唯一标识
     * timestamp: , // 必填，生成签名的时间戳
     * nonceStr: '', // 必填，生成签名的随机串
     * signature: '',// 必填，签名
     * jsApiList: [] // 必填，需要使用的JS接口列表
     * <p>
     * 签名算法(signature)
     * 对此四个参数进行加密
     * jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg
     * noncestr=Wm3WZYTPz0wzccnW
     * timestamp=1414587457
     * url=http://mp.weixin.qq.com?params=value
     * 签名生成规则如下：参与签名的字段包括有效的jsapi_ticket,noncestr（随机字符串）, , timestamp（时间戳）, url
     * 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1
     * 这里需要注意的是所有参数名均为小写字符。对string1作MD5加密，字段名和字段值都采用原始值，不进行URL 转义
     */
    @JavascriptInterface
    public void config(String url, boolean debug, String appId, int timestamp, String nonceStr, String signature, String[] jsApiList) {
        Log.e(TAG, "url==" + url + "  debug==" + debug + " appid==" + appId + "  timestamp==" + timestamp + "  nonceStr==" + nonceStr + " signature==" + signature + "  jsApiList==" + jsApiList);
        configCheck(url, debug, appId, timestamp, nonceStr, signature, jsApiList);
    }

    //清除方法
    public static void cleanMetheds() {
        methedsSwitch.clear();
    }

    //通过config接口注入权限验证配置
    private void configCheck(String url, boolean debug, String appId, int timestamp, String nonceStr, String signature, String[] jsApiList) {
        String stringFromNet = HttpHelper.getStringFromNet("http://www.zaoxingwu.cn/v4.php?ctr=App_Profit.WebConfigAutograph" +
                "&url=" + url + "&debug=" + debug + "&appId=" + appId + "&timestamp=" + timestamp + "&nonceStr=" + nonceStr + "&signature=" + signature + "&jsApiList=" + Arrays.toString(jsApiList));
        try {
            Log.e(TAG, stringFromNet);
            JSONObject jsonObject = new JSONObject(stringFromNet);
            int status = jsonObject.getInt("status");
            if (status == 1) {
                Log.e(TAG, "签名匹配成功！");
                if (listener != null) listener.ready();
            } else {
                Log.e(TAG, "签名匹配失败！");
                String msg = jsonObject.getString("msg");
                if (listener != null) listener.error(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "stringFromNet==" + stringFromNet);
    }

    private OnConfigListener listener;

    public void setListener(OnConfigListener listener) {
        this.listener = listener;
    }

    @JavascriptInterface
    public void shareText(String msg) {
        if (!methedsSwitch.get("shareText")) return;
        if (activity == null) throw new NullPointerException("ConfigJs should init first");
        if (TextUtils.isEmpty(msg)) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        activity.startActivity(intent);

    }
}
