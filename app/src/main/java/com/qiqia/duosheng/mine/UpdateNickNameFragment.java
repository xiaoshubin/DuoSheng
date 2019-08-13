package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.EditTextUtils;
import cn.com.smallcake_utils.ToastUtil;

public class UpdateNickNameFragment extends BaseBarFragment {
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.iv_clean)
    ImageView ivClean;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    User user;
    @Override
    public int setLayout() {
        return R.layout.fragment_update_nickname;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("昵称");
         user = DataLocalUtils.getUser();
    }
    @OnClick({R.id.btn_confirm, R.id.iv_clean})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                updateNickName();
                break;
            case R.id.iv_clean:
                etNick.setText("");
                break;
        }
    }
    private void updateNickName() {
        if (EditTextUtils.isEmpty(etNick)) return;
        String nick = etNick.getText().toString();
        dataProvider.user.info(user.getUid(),0,nick,user.getToken())
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        user.setNickname(nick);
                        DataLocalUtils.saveUser(user);
                        ToastUtil.showLong("修改成功！");
                        pop();
                    }
                });

    }
}
