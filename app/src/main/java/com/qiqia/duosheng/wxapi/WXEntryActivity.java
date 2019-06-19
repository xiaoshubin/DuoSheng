package com.qiqia.duosheng.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.lsxiao.apollo.core.Apollo;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.utils.NetworkUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private static String TAG = "MicroMsg.WXEntryActivity";

    private IWXAPI api;
    private MyHandler handler;

    @Override
    public void onReq(BaseReq baseReq) {
        L.e("onReq==" + baseReq.toString());
    }

    /**
     * 减少“强制分享至不同群”等滥用分享能力
     * 不管用户是否分享成功，均为成功。说明如下：
     * https://open.weixin.qq.com/cgi-bin/announce?spm=a311a.9588098.0.0&action=getannouncement&key=11534138374cE6li&version=
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        int errCode = baseResp.errCode;
        int type = baseResp.getType();
        String openId = baseResp.openId;
        String errStr = baseResp.errStr;
        String transaction = baseResp.transaction;
		L.e("WXEntryActivity==errCode=="+errCode+"  errStr=="+errStr+"  openid ="+openId+"  type=="+type+"  transaction=="+transaction);
        String result;
        switch (errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "ERR_OK";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "ERR_USER_CANCEL";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "ERR_AUTH_DENIED";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "ERR_UNSUPPORT";
                break;
            default:
                result = "default";
                break;
        }
		L.e("result=="+result);
        if (type == ConstantsAPI.COMMAND_SENDAUTH) {
            SendAuth.Resp authResp = (SendAuth.Resp) baseResp;
            final String code = authResp.code;
            L.e("code==" + code);
            if (TextUtils.isEmpty(code)){
                ToastUtil.showShort("微信登录失败，请检查网络或直接手机登录！.");
                finish();
                return;
            }else {
                //用code登录一下，如果是老用户就直接登录成功，如果是新用户则需要绑定手机好
                finish();
                ToastUtil.showShort("微信授权成功...检测是否是新用户！");
                Apollo.emit(EventStr.WX_LOGIN_CODE,code);

            }
        }
        //如果是分享到微信成功后的回调，
        if (type==ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
            if (baseResp.errCode==BaseResp.ErrCode.ERR_OK){
                ToastUtil.showLong("分享成功！");
            }else {
                ToastUtil.showLong("分享失败！");
            }
            finish();
        }

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        api = WXAPIFactory.createWXAPI(this, Contants.WX_OPEN_APP_ID, false);
        handler = new MyHandler(this);

        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<WXEntryActivity> wxEntryActivityWeakReference;

        public MyHandler(WXEntryActivity wxEntryActivity) {
            wxEntryActivityWeakReference = new WeakReference<WXEntryActivity>(wxEntryActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            int tag = msg.what;
            switch (tag) {
                case NetworkUtil.GET_TOKEN: {
                    Bundle data = msg.getData();
                    try {
                        String result = data.getString("result");

                        L.e("result==" + data.getString("result"));
                        JSONObject json = new JSONObject(data.getString("result"));
                        String openId, accessToken, refreshToken, scope;
                        openId = json.getString("openid");
                        accessToken = json.getString("access_token");
                        refreshToken = json.getString("refresh_token");
                        scope = json.getString("scope");
                        L.e("json==" + json.toString());
//						Intent intent = new Intent(wxEntryActivityWeakReference.get(), SendToWXActivity.class);
//						intent.putExtra("openId", openId);
//						intent.putExtra("accessToken", accessToken);
//						intent.putExtra("refreshToken", refreshToken);
//						intent.putExtra("scope", scope);
//						wxEntryActivityWeakReference.get().startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        L.e(TAG, e.getMessage());
                    }
                }
            }
        }
    }


}