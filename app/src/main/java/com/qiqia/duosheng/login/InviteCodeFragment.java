package com.qiqia.duosheng.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsxiao.apollo.core.Apollo;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.dialog.NoInviteDialog;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.ToastUtil;

public class InviteCodeFragment extends BaseBarFragment {

    @BindView(R.id.et_invite_code)
    EditText etInviteCode;
    @BindView(R.id.tv_err)
    TextView tvErr;

    String phoneNumber;
    String msgCode;
    int wxId;

    @Override
    public int setLayout() {
        return R.layout.fragment_invite_code;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        phoneNumber = getArguments().getString("phoneNumber");
        msgCode = getArguments().getString("msgCode");
        wxId = getArguments().getInt("wxId");
    }

    public static InviteCodeFragment newInstance(int wxId, String phoneNumber, String msgCode) {
        Bundle args = new Bundle();
        args.putInt("wxId", wxId);
        args.putString("phoneNumber", phoneNumber);
        args.putString("msgCode", msgCode);
        InviteCodeFragment fragment = new InviteCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.tv_no_invite, R.id.btn_login})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_no_invite:
                new NoInviteDialog(_mActivity).show();
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    /**
     * {"status":0,"data":[],"msg":"参数不能为空！"}
     * {"status":0,"data":[],"msg":"验证码已过期或者不存在！"}
     */
    private void login() {
        String inviteCode = etInviteCode.getText().toString();
        if (TextUtils.isEmpty(inviteCode)) {
            new NoInviteDialog(_mActivity).show();
            return;
        }
        dataProvider.login.register(inviteCode, wxId, phoneNumber, msgCode)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<User>>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse<User> baseResponse) {
                        if (baseResponse.getStatus()==1){
                            ToastUtil.showLong("恭喜你注册成功！");
                            User result =  baseResponse.getData();
                            //保存用户信息，并进入主页
                            DataLocalUtils.saveUser(result);
                            Apollo.emit(EventStr.LOGIN_SUCCESS,result);
                            pop();
                        }else {
                            tvErr.setText(baseResponse.getMsg());
                        }
                    }

                    @Override
                    protected void onErr(String msg) {
                        tvErr.setText(msg);
                    }
                });


    }
}
