package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lsxiao.apollo.core.Apollo;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.EditTextUtils;
import cn.com.smallcake_utils.ToastUtil;

public class UpdateWxkfFragment extends BaseBarFragment {
    @BindView(R.id.et_wxkf)
    EditText etWxkf;
    @BindView(R.id.iv_clean)
    ImageView ivClean;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    User user;
    @Override
    public int setLayout() {
        return R.layout.fragment_update_wxkf;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("微信客服更换");
        user = DataLocalUtils.getUser();
    }

    @OnClick({R.id.btn_confirm, R.id.iv_clean})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                updateWxkf();
                break;
            case R.id.iv_clean:
                etWxkf.setText("");
                break;
        }
    }

    private void updateWxkf() {
        if (EditTextUtils.isEmpty(etWxkf)) return;
        String wxkf = etWxkf.getText().toString();
        dataProvider.user.info(user.getUid(),3,wxkf,user.getToken())
                .subscribe(new OnSuccessAndFailListener<BaseResponse>() {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showLong("修改成功！");
                        user.setWx(wxkf);
                        DataLocalUtils.saveUser(user);
                        Apollo.emit(EventStr.UPDATE_WXKF,wxkf);
                        pop();
                    }


                });
    }

}
