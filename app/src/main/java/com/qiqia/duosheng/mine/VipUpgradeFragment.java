package com.qiqia.duosheng.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanks.htextview.rainbow.RainbowTextView;
import com.lsxiao.apollo.core.Apollo;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.bean.VipUpgradeResponse;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.ToastUtil;

public class VipUpgradeFragment extends BaseBarFragment {
    @BindView(R.id.ele_bar)
    View eleBar;
    @BindView(R.id.iv_icon1)
    ImageView ivIcon1;
    @BindView(R.id.tv_equity1)
    TextView tvEquity1;
    @BindView(R.id.iv_icon2)
    ImageView ivIcon2;
    @BindView(R.id.iv_icon3)
    ImageView ivIcon3;
    @BindView(R.id.iv_icon4)
    ImageView ivIcon4;
    @BindView(R.id.layout_top)
    LinearLayout layoutTop;
    Unbinder unbinder;
    @BindView(R.id.tv_equity2)
    TextView tvEquity2;
    @BindView(R.id.tv_equity3)
    TextView tvEquity3;
    @BindView(R.id.tv_equity4)
    TextView tvEquity4;
    @BindView(R.id.tv_up1)
    TextView tvUp1;
    @BindView(R.id.tv_up2)
    TextView tvUp2;
    @BindView(R.id.tv_up3)
    TextView tvUp3;
    @BindView(R.id.tv_UlName)
    RainbowTextView tvUlName;
    @BindView(R.id.tv_top_upgrade)
    TextView tvTopUpgrade;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_bottom_upgrade)
    TextView tvBottomUpgrade;
    String uid;
    String token;
    String phone;
    User user;
    int level;//当前用户会员等级
    String ulName;//下级会员名称
    @BindView(R.id.layout_card)
    RelativeLayout layoutCard;

    public static VipUpgradeFragment newInstance() {
        Bundle args = new Bundle();
        VipUpgradeFragment fragment = new VipUpgradeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_vip_upgrade;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("VIP会员");
        user = DataLocalUtils.getUser();
        if (user != null) {
            L.e(user.toString());
            uid = user.getUid();
            token = user.getToken();
            phone = user.getPhone();
            getUpgradeData();

        }

    }

    private void getUpgradeData() {
        dataProvider.user.upgradeInfo(uid)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<VipUpgradeResponse>>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse<VipUpgradeResponse> vipUpgradeResponseBaseResponse) {
                        VipUpgradeResponse data = vipUpgradeResponseBaseResponse.getData();
                        initEquityMsg(data);
                    }


                });
    }

    private void initEquityMsg(VipUpgradeResponse data) {

        boolean isUpgrade = data.getIsUpgrade() == 1;//是否可以升级
        level = data.getLevel();
        //如果是顶级，无法再升级
        if (level == 3) {
            ToastUtil.showLong("你已经升级到顶级会员！");
            if (getPreFragment() == null) _mActivity.finish();
            pop();
        }

        VipUpgradeResponse.ProfitDistributionConditionBean profitDistributionCondition = data.getProfitDistributionCondition();//会员权益
        VipUpgradeResponse.UpgradeConditionBean upgradeCondition = data.getUpgradeCondition();//升级要求
        VipUpgradeResponse.UpgradeInfoBean upgradeInfo = data.getUpgradeInfo();//当前用户升级信息
        String levelName = data.getLevelName();

        //下一级别VIP名称
        ulName = upgradeCondition.getUlName();
        tvTitle.setText("升级" + ulName);
        tvUlName.animateText(upgradeCondition.getUlName());
        if (level==2)layoutCard.setBackgroundResource(R.mipmap.icon_vip_card2);
        //按钮状态
        if (!isUpgrade) {
//            tvTopUpgrade.setEnabled(false);
//            tvBottomUpgrade.setEnabled(false);
//            tvState.setText("当前状态：未达标");
//            tvTopUpgrade.setBackgroundResource(R.drawable.round_gray_bg);
//            tvBottomUpgrade.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }
        //会员权益
        SpannableStringBuilder str1 = SpannableStringUtils.getBuilder("自购物奖励\n佣金提升至")
                .append(profitDistributionCondition.getPdDivideInto() + "%\n").setForegroundColor(Color.RED)
                .append("您是普通会员，自购物佣金为10元，升级为VIP会员后，佣金将提升为16元")
                .setForegroundColor(Color.GRAY).setProportion(0.8f)
                .create();
        SpannableStringBuilder str2 = SpannableStringUtils.getBuilder("下级普通会员\n佣金差额")
                .append(profitDistributionCondition.getPdSubordinate() + "%").setForegroundColor(Color.RED)
                .append("奖励\n")
                .append("您下级普通会员自购物佣金为100元平台将奖励您60元").setForegroundColor(Color.GRAY).setProportion(0.8f)
                .create();
        SpannableStringBuilder str3 = SpannableStringUtils.getBuilder("非直属会员\n佣金差额")
                .append(profitDistributionCondition.getPdEr() + "%").setForegroundColor(Color.RED)
                .append("奖励\n")
                .append("您的下级普通会员推荐的非您的直属会员，他购物佣金100元时平台将奖励您41.1元").setForegroundColor(Color.GRAY).setProportion(0.8f)
                .create();
        SpannableStringBuilder str4 = SpannableStringUtils.getBuilder("直属同级VIP团队会员\n佣金")
                .append(profitDistributionCondition.getPdLevel() + "%").setForegroundColor(Color.RED)
                .append("额外奖励\n")
                .append("您的直属VIP会员团队下的所有会员购物佣金100元时平台将奖励您11.8元").setForegroundColor(Color.GRAY).setProportion(0.8f)
                .create();
        tvEquity1.setText(str1);
        tvEquity2.setText(str2);
        tvEquity3.setText(str3);
        tvEquity4.setText(str4);
        //升级要求
        SpannableStringBuilder upStr1 = SpannableStringUtils.getBuilder("直属下级会员≥" + upgradeCondition.getUlCondition1() + "人\n")
                .append("当前：" + upgradeInfo.getSubCount() + "人").setForegroundColor(Color.GRAY).setProportion(0.8f)
                .create();
        SpannableStringBuilder upStr2 = SpannableStringUtils.getBuilder("非直属会员(下下级会员)≥" + upgradeCondition.getUlCondition2() + "人\n")
                .append("当前：" + upgradeInfo.getSubSubCount() + "人").setForegroundColor(Color.GRAY).setProportion(0.8f)
                .create();
        SpannableStringBuilder upStr3 = SpannableStringUtils.getBuilder("累计收益≥" + upgradeCondition.getUlCondition3() + "元\n")
                .append("当前：" + upgradeInfo.getTotal() + "元").setForegroundColor(Color.GRAY).setProportion(0.8f)
                .create();
        tvUp1.setText(upStr1);
        tvUp2.setText(upStr2);
        tvUp3.setText(upStr3);


    }

    @OnClick({R.id.tv_top_upgrade, R.id.tv_bottom_upgrade})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_top_upgrade:
            case R.id.tv_bottom_upgrade:
                upgrade();
                break;
        }
    }

    private void upgrade() {
        dataProvider.user.upgrade(uid, token, phone)
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showLong(baseResponse.getMsg());
                        _mActivity.finish();
                        //升级成功，修改User中的level和level_name
                        user.setLevel(level+1);
                        user.setPdid(ulName);
                        DataLocalUtils.saveUser(user);
                        Apollo.emit(EventStr.LOGIN_SUCCESS, user);
                    }


                });
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
