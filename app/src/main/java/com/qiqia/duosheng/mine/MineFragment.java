package com.qiqia.duosheng.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.lsxiao.apollo.core.annotations.Receive;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.dialog.MyServiceWxDialog;
import com.qiqia.duosheng.utils.BannerUtils;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.GuideUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.ClipboardUtils;
import cn.com.smallcake_utils.DataCleanUtil;
import cn.com.smallcake_utils.IdentifierUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;

public class MineFragment extends BaseFragment {
    @BindView(R.id.ele_bar)
    View eleBar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_invite_desc)
    TextView tvInviteDesc;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.tv_copy_code)
    TextView tvCopyCode;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.iv_set)
    ImageView ivSet;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_clean_cache)
    TextView tvCleanCache;
    @BindView(R.id.layout_upgrade_vip)
    RelativeLayout layoutUpgradeVip;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;
    @BindView(R.id.iv_level)
    ImageView ivLevel;
    @BindView(R.id.layout_middle)
    LinearLayout layoutMiddle;
    @BindView(R.id.tv_guide)
    TextView tvGuide;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.layout_income)
    LinearLayout layoutIncome;
    User user;
    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        user = DataLocalUtils.getUser();
        initUserData();
        setCacheSize();
        initBanner();
    }

    private void initBanner() {
        BannerUtils.addImageToBanner(_mActivity, dataProvider.shop, 3, banner);
    }

    private void setCacheSize() {
        try {
            String totalCacheSize = DataCleanUtil.getTotalCacheSize(_mActivity);
            tvCleanCache.setText("清除缓存（" + totalCacheSize + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUserData() {
        if (user == null) return;
//            L.e("user=="+user.toString());
            tvNickname.setText(user.getNickname());
            tvInviteCode.setText(user.getSuperCode());
            String headimgurl = user.getHeadimgurl();
            RequestOptions options = new RequestOptions();
            options.transform(new CircleCrop()).placeholder(R.mipmap.logo).error(R.mipmap.logo);
            Glide.with(this).load(headimgurl).apply(options).into(ivHead);
            int level = user.getLevel();
            int levelRes = IdentifierUtils.getMipmapResourceID("vip_level_" + level);
            ivLevel.setImageResource(levelRes);
            if (level > 2) layoutUpgradeVip.setVisibility(View.GONE);

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).init();
        GuideUtils.showGuide(this, ivSet);
    }

    @Receive(EventStr.LOGIN_SUCCESS)
    public void loginSuccess(User user) {
        L.e("登录成功.....");
        this.user = user;
        initUserData();
    }


    @OnClick({R.id.tv_copy_code, R.id.iv_msg,R.id.iv_set, R.id.tv_clean_cache, R.id.tv_about_us, R.id.tv_feedback, R.id.layout_upgrade_vip, R.id.layout_middle, R.id.tv_guide,
            R.id.tv_collection, R.id.tv_service,R.id.tv_income,R.id.tv_invite_friend,R.id.tv_member_fans,R.id.tv_order})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_code:
                String inviteCode = tvInviteCode.getText().toString();
                ClipboardUtils.copy(inviteCode);
                break;
            case R.id.iv_set:
                goTransparentBarActivity(SetFragment.class.getSimpleName());
                break;
            case R.id.iv_msg:
                goTransparentBarActivity(NoticeFragment.class.getSimpleName());
                break;
            case R.id.layout_upgrade_vip:
                goWhiteBarActivity(VipUpgradeFragment.class.getSimpleName());
                break;
            case R.id.tv_feedback:
                goTransparentBarActivity(FeedBackFragment.class.getSimpleName());
                break;
            case R.id.tv_about_us:
                goTransparentBarActivity(AboutUsFragment.class.getSimpleName());
                break;
            case R.id.tv_clean_cache:
                new XPopup.Builder(getContext()).asConfirm(getString(R.string.app_name), "确认要清除缓存吗？",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                DataCleanUtil.clearAllCache(getContext());
                                ToastUtil.showShort("清理成功！");
                                tvCleanCache.setText("清除缓存");
                            }
                        })
                        .show();
                break;
            case R.id.tv_income:
                goTransparentBarActivity(MyIncomeFragment.class.getSimpleName());
                break;
            case R.id.tv_service:
                getWxData();
                break;
            case R.id.tv_invite_friend:
                goWhiteBarActivity(InviteFriendFragment.class.getSimpleName());
                break;
            case R.id.tv_member_fans:
                goTransparentBarActivity(MemberFansFragment.class.getSimpleName());
                break;
            case R.id.tv_collection:
                goWhiteBarActivity(MyCollectionFragment.class.getSimpleName());
                break;
            case R.id.tv_order:
                goTransparentBarActivity(OrderFragment.class.getSimpleName());
                break;
            default:
                ToastUtil.showLong("开发中....");
                break;
        }
    }

    private void getWxData() {
        dataProvider.user.getWx(user.getUid())
                .subscribe(new OnSuccessAndFailListener<BaseResponse<String>>() {
                    @Override
                    protected void onSuccess(BaseResponse<String> baseResponse) {
                        String data = baseResponse.getData();
                        new XPopup.Builder(_mActivity).asCustom(new MyServiceWxDialog(_mActivity,data)).show();
                    }

                    @Override
                    protected void onErr(String msg) {
                       ToastUtil.showLong("上级未设置！");
                    }
                });
    }

}
