package com.qiqia.duosheng.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.dialog.PredictIncomeDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.SpannableStringUtils;

public class MyIncomeFragment extends BaseBarFragment {

    @BindView(R.id.tv_buy_num)
    TextView tvBuyNum;
    @BindView(R.id.tv_buy_income)
    TextView tvBuyIncome;
    @BindView(R.id.tv_team_num)
    TextView tvTeamNum;
    @BindView(R.id.tv_team_income)
    TextView tvTeamIncome;
    @BindView(R.id.layout_bar)
    LinearLayout layoutBar;

    @Override
    public int setLayout() {
        return R.layout.fragment_my_income;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("我的收益");
        tvTitle.setTextColor(Color.WHITE);
        ivRight.setVisibility(View.VISIBLE);
        ivBack.setImageResource(R.mipmap.icon_backwhite);
        layoutBar.setBackgroundColor(Color.TRANSPARENT);
        initData();
    }

    private void initData() {
        tvBuyNum.setText(builderDesc("付款笔数", "12"));
        tvBuyIncome.setText(builderDesc("预估佣金(元)", "180.54"));
        tvTeamNum.setText(builderDesc("付款笔数", "4"));
        tvTeamIncome.setText(builderDesc("预估佣金(元)", "2132.27"));
    }

    private SpannableStringBuilder builderDesc(String desc, String num) {
        return SpannableStringUtils.getBuilder(desc + "\n")
                .append(num).setForegroundColor(ContextCompat.getColor(_mActivity, R.color.text_black)).create();
    }

    @OnClick({R.id.btn_cash, R.id.tv_lastmonth_predict, R.id.tv_lastmonth_income, R.id.tv_thismonth_predict, R.id.iv_right})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.btn_cash:
                goWhiteBarActivity(CashFragment.class.getSimpleName());
                break;
            case R.id.tv_lastmonth_predict://上月预估
                showExplain(0);
                break;
            case R.id.tv_lastmonth_income://上月预估
                showExplain(1);
                break;
            case R.id.tv_thismonth_predict://上月预估
                showExplain(2);
                break;
            case R.id.iv_right://上月预估
                goWebViewFragment("收益说明", "");
                break;
        }
    }

    private void showExplain(int type) {
        String title = "预估收入";
        String msg = "本月内创建的所有订单预估收益\n将在次月25日结算";
        switch (type) {
            case 0:
                title = "预估收入";
                msg = "本月内创建的所有订单预估收益\n将在次月25日结算";
                break;
            case 1:
                title = "结算收入";
                msg = "上个月内确认收货的订单收益，每月25\n日结算后，将转入到余额中";
                break;
            case 2:
                title = "预估收入";
                msg = "上月创建的所有订单预估收益\n将在本月25日结算";
                break;
        }
        new XPopup.Builder(_mActivity)
                .asCustom(new PredictIncomeDialog(_mActivity, title, msg)).show();
    }
}
