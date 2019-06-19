package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsxiao.apollo.core.Apollo;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.custom.TimeCount;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.EditTextUtils;
import cn.com.smallcake_utils.ToastUtil;

public class UpdatePhoneFragment extends BaseBarFragment {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_clean)
    ImageView ivClean;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.tv_err)
    TextView tvErr;
    TimeCount timeCount;
    User user;

    @Override
    public int setLayout() {
        return R.layout.fragment_update_phone;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("修改手机号");
        user = DataLocalUtils.getUser();
        if (user == null) return;
        etPhone.setText(user.getPhone());
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        ImmersionBar.with(this).statusBarColor(R.color.white).autoStatusBarDarkModeEnable(true, 0.2f).init();
    }

    @OnClick({R.id.btn_confirm, R.id.tv_get_code, R.id.iv_clean})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                sendSmsCode();
                break;
            case R.id.btn_confirm:
                updatePhone();
                break;
            case R.id.iv_clean:
                etPhone.setText("");
                break;
        }
    }

    private void updatePhone() {
        if (EditTextUtils.isEmpty(etPhone, etCode)) return;
        String phone = etPhone.getText().toString();
        String smsCode = etCode.getText().toString();
        dataProvider.user.phone(user.getUid(), phone, smsCode, user.getToken())
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showLong("修改成功！");
                        user.setPhone(phone);
                        DataLocalUtils.saveUser(user);
                        Apollo.emit(EventStr.UPDATE_PHONE, phone);
                        pop();
                    }

                    @Override
                    protected void onErr(String msg) {
                        tvErr.setText(msg);
                    }
                });
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
                ToastUtil.showLong(baseResponse.getMsg());
                timeCount = new TimeCount(60 * 1000, 1000, tvGetCode);
                timeCount.start();
            }


        });
    }
}
