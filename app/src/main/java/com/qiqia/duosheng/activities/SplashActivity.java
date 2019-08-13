package com.qiqia.duosheng.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.MainActivity;
import com.qiqia.duosheng.base.Contants;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.main.GuideViewPagerFragment;

import cn.com.smallcake_utils.SPUtils;

public class SplashActivity extends AppCompatActivity {

    @Override//耗时188ms
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
        new Handler().postDelayed(() -> {
            int index = (int) SPUtils.get(SPStr.GUIDE_VIEWPAGER_FRAGMENT, 0);
            if (true) goGuideFragment();
//            if (index==0) goGuideFragment();
            else{
                startActivity(new Intent(this, MainActivity.class));
            }
            this.finish();
        },300);

    }
    protected void goGuideFragment(){
        Intent intent = new Intent(this, TransparentBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT,GuideViewPagerFragment.class.getSimpleName());
        startActivity(intent);
    }

}
