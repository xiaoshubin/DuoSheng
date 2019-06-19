package com.qiqia.duosheng.login;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lsxiao.apollo.core.Apollo;
import com.lsxiao.apollo.core.annotations.Receive;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;

public class LoginFragment extends BaseFragment {

    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.line_wx_login)
    LinearLayout lineWxLogin;
    @BindView(R.id.line_phone_login)
    LinearLayout linePhoneLogin;

    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;
    @Override
    public int setLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        initWXApi();
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initWXApi() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(_mActivity, Contants.WX_OPEN_APP_ID, true);
        // 将应用的appId注册到微信
        boolean b = api.registerApp(Contants.WX_OPEN_APP_ID);
        L.e("是否注册微信=="+b);
    }

    @Receive(EventStr.WX_LOGIN_CODE)
    public void onEvents(String code){
        L.e("LoginFragment==拿到微信登录的code传递给手机登录页面》》"+code);
        loginByCode(code);
    }

    private void loginByCode(String code) {
        dataProvider.login.login(code, null, null)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<User>>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse<User> baseResponse) {
                        User result = baseResponse.getData();
                        boolean isNewUser = result.getNewUser() == 1;
                        if (isNewUser) {
                            //新用户，拿到微信登录的code传递给手机登录页面
                            ToastUtil.showLong("新用户你好，绑定手机后就可以使用啦！");
                            int wxid = result.getWxid();
                            start(PhoneLoginFragment.newInstance(code,wxid));
                        } else {
//                            //老用户，保存信息，关闭页面，发送通知
                            ToastUtil.showLong(result.getNickname()+",你好！");
                            DataLocalUtils.saveUser(result);
                            Apollo.emit(EventStr.LOGIN_SUCCESS, result);
                            if (getPreFragment()==null){
                                _mActivity.finish();
                            }
                        }

                    }


                });


    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    @OnClick({R.id.line_wx_login, R.id.line_phone_login,R.id.iv_close})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.line_wx_login:
                wxSendLogin();
                break;
            case R.id.line_phone_login:
                start(new PhoneLoginFragment());
                break;
            case R.id.iv_close:
                pop();
                _mActivity.finish();
                break;
        }
    }

    /**
     * 发送微信登录请求，调起微信登录页面
     */
    private void wxSendLogin() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_qiqia";
        api.sendReq(req);
    }

    @Receive(EventStr.LOGIN_SUCCESS)
    public void loginSuccess(User user) {
        if (getPreFragment()==null){
            _mActivity.finish();
        }
        pop();
    }


}
