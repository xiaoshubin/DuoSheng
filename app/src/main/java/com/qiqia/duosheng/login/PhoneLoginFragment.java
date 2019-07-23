package com.qiqia.duosheng.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lsxiao.apollo.core.Apollo;
import com.lsxiao.apollo.core.annotations.Receive;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.custom.TimeCount;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.smallcake_utils.ToastUtil;

public class PhoneLoginFragment extends BaseBarFragment {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_err)
    TextView tvErr;
    Unbinder unbinder;


    @Override
    public int setLayout() {
        return R.layout.fragment_phone_login;
    }

    String code;
    int wxid;
    boolean inputPhone, inputCode;
    TimeCount timeCount;

    public static PhoneLoginFragment newInstance(String code, int wxid) {
        Bundle args = new Bundle();
        args.putString("code", code);
        args.putInt("wxid", wxid);
        PhoneLoginFragment fragment = new PhoneLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        code = getArguments().getString("code");
        wxid = getArguments().getInt("wxid");

        onEvent();

    }

    private void onEvent() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputPhone = s.length() > 0;
                updateLoginBtn();
            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputCode = s.length() > 0;
                updateLoginBtn();
            }
        });


    }

    private void updateLoginBtn() {
        if (inputPhone && inputCode) {
            btnLogin.setClickable(true);
            btnLogin.setBackgroundResource(R.drawable.round_black_btn_bg);
        } else {
            btnLogin.setClickable(false);
            btnLogin.setBackgroundResource(R.drawable.round_gray_btn_bg);
        }
    }


    @OnClick({R.id.btn_login, R.id.tv_get_code})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //如果使用微信登录过来，为新用户，只验证验证码即可
                if (wxid != 0) {
                    checkCode();
                } else {
                    loginByPhone();//否则为：直接点击【手机快速登录】，就要调用login判断是否是新用户
                }
                break;
            case R.id.tv_get_code:
                sendSmsCode();
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendSmsCode() {
        String phoneNumber = etPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showShort("请输入手机号！");
            return;
        }
        dataProvider.login.sendSms(phoneNumber).subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
            @Override
            protected void onSuccess(BaseResponse baseResponse) {
                int status = baseResponse.getStatus();
                ToastUtil.showLong(baseResponse.getMsg());
                if (status == 1) {
                    tvErr.setText("");
                    timeCount = new TimeCount(60 * 1000, 1000,tvGetCode);
                    timeCount.start();
                } else {
                    tvErr.setText(baseResponse.getMsg());
                }
            }


            @Override
            protected void onErr(String msg) {
                super.onErr(msg);
                tvErr.setText(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timeCount != null) timeCount.cancel();
    }


    /**
     * {"status":1,"data":{"new":1},"msg":"ok"}
     * {"status":0,"data":[],"msg":"请输入正确的手机号码!"}
     * new:1 为新用户 跳手机绑定界面， 0 为旧用户 跳首页
     * <p>
     * 改为使用验证验证码
     */
    private void loginByPhone() {
        String phoneNumber = etPhone.getText().toString();
        String smsCode = etCode.getText().toString();
        dataProvider.login.login(code, phoneNumber, smsCode)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<User>>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse<User> baseResponse) {
                        tvErr.setText("");

                        User result = baseResponse.getData();
                        boolean isNewUser = result.getNewUser() == 1;
                        if (isNewUser) {
                            int wxid = result.getWxid();
                            start(InviteCodeFragment.newInstance(wxid, phoneNumber, smsCode));
                        } else {
                            ToastUtil.showLong("登录成功");
                            //保存用户信息，并进入主页
                            DataLocalUtils.saveUser(result);
                            Apollo.emit(EventStr.LOGIN_SUCCESS, result);
                        }

                    }

                    @Override
                    protected void onErr(String msg) {
                        super.onErr(msg);
                        tvErr.setText(msg);
                    }
                });


    }

    private void checkCode() {
        String phoneNumber = etPhone.getText().toString();
        String smsCode = etCode.getText().toString();
        dataProvider.login.checkSms(phoneNumber, smsCode)
                .subscribe(new OnSuccessAndFailListener<BaseResponse>() {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        start(InviteCodeFragment.newInstance(wxid, phoneNumber, smsCode));
                    }

                    @Override
                    protected void onErr(String msg) {
                        super.onErr(msg);
                        tvErr.setText(msg);
                    }
                });


    }

    @Receive(EventStr.LOGIN_SUCCESS)
    public void loginSuccess(User user) {
        pop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
