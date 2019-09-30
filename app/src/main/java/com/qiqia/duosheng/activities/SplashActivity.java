package com.qiqia.duosheng.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.MainActivity;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.SPStr;
import com.smallcake.qsh.QshWebActivity;

import cn.com.smallcake_utils.SPUtils;

public class SplashActivity extends AppCompatActivity {

    @Override//耗时188ms
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            setContentView(R.layout.fragment_startup);
        else ImmersionBar.with(this).init();

        int index = (int) SPUtils.get(SPStr.GUIDE_VIEWPAGER_FRAGMENT, 0);
//            if (true) goGuideFragment();
        if (index == 0) goGuideFragment();
        else startActivity(new Intent(this, MainActivity.class));
        this.finish();

    }

    protected void goGuideFragment() {
//        Intent intent = new Intent(this, TransparentBarActivity.class);
//        intent.putExtra(Contants.LOAD_FRAGMENT, CoordinatorLayoutTestFragment.class.getSimpleName());
//        intent.putExtra(Contants.LOAD_FRAGMENT, GuideViewPagerFragment.class.getSimpleName());
//        startActivity(intent);


        Intent intent = new Intent(this, QshWebActivity.class);
        intent.putExtra("url","file:///android_asset/test.html");
        startActivity(intent);
    }

}
