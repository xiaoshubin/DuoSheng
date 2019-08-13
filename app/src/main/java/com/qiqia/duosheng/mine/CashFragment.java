package com.qiqia.duosheng.mine;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.SpannableStringUtils;

public class CashFragment extends BaseBarFragment {
    @BindView(R.id.tv_ali_account)
    TextView tvAliAccount;
    @BindView(R.id.tv_ali_name)
    TextView tvAliName;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_withdraw_money)
    TextView tvWithdrawMoney;
    @BindView(R.id.tv_withdraw_all)
    TextView tvWithdrawAll;

    @Override
    public int setLayout() {
        return R.layout.fragment_cash;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("提现 ");
        initData();
    }

    private void initData() {
        //手续费
        SpannableStringBuilder fee = SpannableStringUtils.getBuilder("手续费")
                .append("￥").setForegroundColor(ContextCompat.getColor(_mActivity, R.color.orangered)).setProportion(0.5f)
                .append("1").setForegroundColor(ContextCompat.getColor(_mActivity, R.color.orangered)).create();
        tvFee.setText(fee);
    }

    @OnClick({R.id.tv_update_alipay,R.id.btn_confirm,R.id.tv_withdraw_list})
    public void doClicks(View view){
        switch (view.getId()){
            case R.id.tv_update_alipay:
                start(new BindAlipayFragment());
                break;
            case R.id.btn_confirm:
                withdraw();
                break;
            case R.id.tv_withdraw_list:
                start(new CashListFragment());
                break;
        }
    }

    private void withdraw() {
        start(new CashSuccessFragment());
    }
}
