package com.qiqia.duosheng.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.main.GuideViewPagerFragment;
import com.qiqia.duosheng.main.PracticalListFragment;
import com.qiqia.duosheng.main.StartupFragment;
import com.qiqia.duosheng.main.WebViewFragment;
import com.qiqia.duosheng.mine.AboutUsFragment;
import com.qiqia.duosheng.mine.FeedBackFragment;
import com.qiqia.duosheng.mine.MemberFansFragment;
import com.qiqia.duosheng.mine.MyIncomeFragment;
import com.qiqia.duosheng.mine.NoticeFragment;
import com.qiqia.duosheng.mine.OrderFragment;
import com.qiqia.duosheng.mine.SetFragment;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.CreateShareFragment;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 透明低白字字电量栏Activity
 * 不传LOAD_FRAGMENT默认为搜索历史页面
 */
public class TransparentBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_bar);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarColor(R.color.transparent).autoStatusBarDarkModeEnable(true,0.2f).init();
        loadFragmentByType();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadFragmentByType() {
        Intent intent = getIntent();
        String intExtra = intent.getStringExtra(Contants.LOAD_FRAGMENT);
        if (TextUtils.isEmpty(intExtra))intExtra= StartupFragment.class.getSimpleName();
        switch (intExtra) {
            case "StartupFragment":
                loadFragment(StartupFragment.newInstance());
                break;
            case "GuideViewPagerFragment":
                loadFragment(GuideViewPagerFragment.newInstance());
                break;
            case "SetFragment":
                loadFragment(SetFragment.newInstance());
                break;
            case "PracticalListFragment":
                loadFragment(PracticalListFragment.newInstance(intent.getIntExtra("sort",1)));
                break;
            case "CreateShareFragment":
                Bundle bundle = intent.getBundleExtra("bundle");
                GoodsInfo goodsInfo = (GoodsInfo) bundle.getSerializable("goodsInfo");
                loadFragment(CreateShareFragment.newInstance(goodsInfo));
                break;
            case "WebViewFragment":
                Bundle bundleWeb = intent.getBundleExtra("bundle");
                loadFragment(WebViewFragment.newInstance(bundleWeb.getString("title"),bundleWeb.getString("url")));
                break;
            case "MyIncomeFragment":
                loadFragment(new MyIncomeFragment());
                break;
            case "MemberFansFragment":
                loadFragment(new MemberFansFragment());
                break;
            case "AboutUsFragment":
                loadFragment(new AboutUsFragment());
                break;
            case "FeedBackFragment":
                loadFragment(new FeedBackFragment());
                break;
            case "OrderFragment":
                loadFragment(new OrderFragment());
                break;
            case "NoticeFragment":
                loadFragment(new NoticeFragment());
                break;
        }

    }

    private void loadFragment(SupportFragment fragment) {
        loadRootFragment(R.id.content,fragment);
    }
}
