package com.qiqia.duosheng.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.lsxiao.apollo.core.Apollo;
import com.lsxiao.apollo.core.contract.ApolloBinder;
import com.qiqia.duosheng.activities.TransparentBarActivity;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.dialog.LoadImgDialog;
import com.qiqia.duosheng.inject.DaggerCommonComponent;
import com.qiqia.duosheng.inject.DataProvider;
import com.qiqia.duosheng.inject.NetWorkMoudle;
import com.qiqia.duosheng.main.WebViewFragment;
import com.qiqia.duosheng.search.GoodsInfoFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.com.smallcake_utils.ActivityCollector;
import me.yokeyword.fragmentation.SupportActivity;

public class BaseActivity extends SupportActivity {
    @Inject
    protected DataProvider dataProvider;
    private ApolloBinder apolloBinder;//事件通知
    @Inject
    protected LoadImgDialog dialog;//加载圈
    @Override
    public void onCreate( @Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ButterKnife.bind(this);
        apolloBinder = Apollo.bind(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCommonComponent.builder().netWorkMoudle(new NetWorkMoudle(this)).build().inject(this);
        ActivityCollector.addActivity(this);
    }

    /**
     * 事件通知解绑
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(apolloBinder != null) apolloBinder.unbind();
        ActivityCollector.removeActivity(this);
    }
    protected void goActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
    protected void goActivity(Class<?> activity,String key,String value){
        Intent intent = new Intent(this, activity);
        intent.putExtra(key,value);
        startActivity(intent);
    }
    /**
     * 跳转到商品详情页
     * @param id
     */
    public void goGoodsInfoFragment(String id){
        Intent intent = new Intent(this, WhiteBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, GoodsInfoFragment.class.getSimpleName());
        intent.putExtra("id",id);
        startActivity(intent);
    }

    protected void goWebViewFragment(String title, String url){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        Intent intent = new Intent(this, TransparentBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, WebViewFragment.class.getSimpleName());
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }
}
