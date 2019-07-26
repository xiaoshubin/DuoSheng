package com.qiqia.duosheng.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.login.LoginFragment;
import com.qiqia.duosheng.login.PhoneLoginFragment;
import com.qiqia.duosheng.mine.CashFragment;
import com.qiqia.duosheng.mine.InviteFriendFragment;
import com.qiqia.duosheng.mine.MyCollectionFragment;
import com.qiqia.duosheng.mine.NoticeSetFragment;
import com.qiqia.duosheng.mine.SetFragment;
import com.qiqia.duosheng.mine.VipUpgradeFragment;
import com.qiqia.duosheng.search.GoodsInfoFragment;
import com.qiqia.duosheng.search.SearchFragment;
import com.qiqia.duosheng.search.SearchHistoryFragment;
import com.qiqia.duosheng.search.bean.GoodsInfo;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 白底黑字电量栏Activity
 * 不传LOAD_FRAGMENT默认为搜索历史页面
 */
public class WhiteBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_bar);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarColor(R.color.white).autoStatusBarDarkModeEnable(true,0.2f).init();
        loadFragmentByType();

    }

    private void loadFragmentByType() {
        Intent intent = getIntent();
        String intExtra = intent.getStringExtra(Contants.LOAD_FRAGMENT);
        if (TextUtils.isEmpty(intExtra))intExtra= SearchHistoryFragment.class.getSimpleName();
        switch (intExtra) {
            case "LoginFragment":
                loadFragment(LoginFragment.newInstance());
                break;
            case "SearchHistoryFragment":
                loadFragment(SearchHistoryFragment.newInstance());
                break;
            case "SearchFragment":
                loadFragment(SearchFragment.newInstance(intent.getStringExtra("keyWord")));
                break;
            case "GoodsInfoFragment":
                String id = intent.getStringExtra("id");
                GoodsInfo goodsInfo = (GoodsInfo) intent.getSerializableExtra("goodsInfo");
                loadFragment(TextUtils.isEmpty(id)?GoodsInfoFragment.newInstance(goodsInfo):GoodsInfoFragment.newInstance(id));
                break;
            case "VipUpgradeFragment":
                loadFragment(VipUpgradeFragment.newInstance());
                break;
            case "PhoneLoginFragment":
                loadFragment(PhoneLoginFragment.newInstance(intent.getStringExtra("code"),intent.getIntExtra("wxid",0)));
                break;
            case "SetFragment":
                loadFragment(SetFragment.newInstance());
                break;
            case "InviteFriendFragment":
                loadFragment(new InviteFriendFragment());
                break;
            case "MyCollectionFragment":
                loadFragment(new MyCollectionFragment());
                break;
            case "CashFragment":
                loadFragment(new CashFragment());
                break;
            case "NoticeSetFragment":
                loadFragment(new NoticeSetFragment());
                break;
        }

    }

    private void loadFragment(SupportFragment fragment) {
        loadRootFragment(R.id.content,fragment);
    }
}
