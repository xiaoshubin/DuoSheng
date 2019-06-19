package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.custom.TimeCount;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.EditTextUtils;
import cn.com.smallcake_utils.ToastUtil;

public class BindAlipayFragment extends BaseBarFragment {
    User user;
    TimeCount timeCount;
    @BindView(R.id.et_ali_name)
    EditText etAliName;
    @BindView(R.id.et_ali_account)
    EditText etAliAccount;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @Override
    public int setLayout() {
        return R.layout.fragment_bind_alipay;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("绑定支付宝");
        user = DataLocalUtils.getUser();
        tvPhone.setText(user.getPhone());

    }

    @OnClick({R.id.tv_get_code, R.id.btn_confirm})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                sendSmsCode();
                break;
            case R.id.btn_confirm:
                bindAlipay();
                break;
        }
    }

    private void bindAlipay() {
        if (EditTextUtils.isEmpty(etAliName,etAliAccount,etCode))return;
        String name = etAliName.getText().toString();
        String account = etAliAccount.getText().toString();
        String code = etCode.getText().toString();
        dataProvider.user.ali(user.getUid(), user.getToken(), user.getPhone(),code,account,name)
                .subscribe(new OnSuccessAndFailListener<BaseResponse>() {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showLong(baseResponse.getMsg());
                    }

                });
    }

    /**
     * 发送验证码
     */
    private void sendSmsCode() {
        dataProvider.login.sendSms(user.getPhone()).subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
            @Override
            protected void onSuccess(BaseResponse baseResponse) {
                ToastUtil.showLong(baseResponse.getMsg());
                timeCount = new TimeCount(60 * 1000, 1000, tvGetCode);
                timeCount.start();
            }

        });
    }
}
