package com.qiqia.duosheng.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lsxiao.apollo.core.Apollo;
import com.lsxiao.apollo.core.contract.ApolloBinder;
import com.qiqia.duosheng.activities.TransparentBarActivity;
import com.qiqia.duosheng.activities.WhiteBarActivity;
import com.qiqia.duosheng.dialog.LoadImgDialog;
import com.qiqia.duosheng.inject.DaggerCommonComponent;
import com.qiqia.duosheng.inject.DataProvider;
import com.qiqia.duosheng.inject.NetWorkMoudle;
import com.qiqia.duosheng.main.PracticalListFragment;
import com.qiqia.duosheng.main.WebViewFragment;
import com.qiqia.duosheng.search.GoodsInfoFragment;
import com.qiqia.duosheng.search.SearchFragment;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.share.CreateShareFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.smallcake_utils.SoftInputUtils;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

public abstract class BaseFragment extends SwipeBackFragment {

    @Inject
    protected DataProvider dataProvider;//请求接口资源提供者
    private ApolloBinder apolloBinder;//事件通知
    @Inject
    protected LoadImgDialog dialog;//加载圈

    public abstract int setLayout();

    protected abstract void onBindView(View view, ViewGroup container, Bundle savedInstanceState);
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(_mActivity).inflate(setLayout(), null);
        unbinder = ButterKnife.bind(this,view);
        onBindView(view, container, savedInstanceState);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCommonComponent.builder().netWorkMoudle(new NetWorkMoudle(this.getContext())).build().inject(this);
        apolloBinder = Apollo.bind(this);
    }
    protected void popActivity() {
        if (getPreFragment()==null){
            _mActivity.finish();
        }
        pop();
        SoftInputUtils.closeSoftInput(_mActivity);
    }
    /**
     * 事件通知解绑
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(apolloBinder != null) apolloBinder.unbind();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null)unbinder.unbind();
    }
     protected void goActivity(Class<?> activity){
         Intent intent = new Intent(_mActivity, activity);
         startActivity(intent);
     }
     protected void goTransparentBarActivity(String targetFragment){
         Intent intent = new Intent(_mActivity, TransparentBarActivity.class);
         intent.putExtra(Contants.LOAD_FRAGMENT,targetFragment);
         startActivity(intent);
     }
     protected void goTransparentBarActivity(String targetFragment,Bundle bundle){
         Intent intent = new Intent(_mActivity, TransparentBarActivity.class);
         intent.putExtra(Contants.LOAD_FRAGMENT,targetFragment);
         intent.putExtra("bundle",bundle);
         startActivity(intent);
     }protected void goWebViewFragment(String title, String url){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        Intent intent = new Intent(_mActivity, TransparentBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, WebViewFragment.class.getSimpleName());
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }


     protected void goWhiteBarActivity(String targetFragment){
         Intent intent = new Intent(_mActivity, WhiteBarActivity.class);
         intent.putExtra(Contants.LOAD_FRAGMENT,targetFragment);
         startActivity(intent);
     }

     protected void goWhiteBarActivity(String targetFragment,String key,String value){
         Intent intent = new Intent(_mActivity, WhiteBarActivity.class);
         intent.putExtra(Contants.LOAD_FRAGMENT,targetFragment);
         intent.putExtra(key,value);
         startActivity(intent);
     }

    /**
     * 跳转到商品详情页(通过ID)
     * @param id
     */
    protected void goGoodsInfoFragment(String id){
         Intent intent = new Intent(_mActivity, WhiteBarActivity.class);
         intent.putExtra(Contants.LOAD_FRAGMENT, GoodsInfoFragment.class.getSimpleName());
         intent.putExtra("id",id);
         startActivity(intent);
     }
    /**
     * 跳转到商品详情页(通过商品实体类)
     * 如果有couponInfo：就判定为【好单库商品】，可以通过id来查询商品详情
     * 否则为【全网商品】，直接使用此对象
     */
    protected  void goGoodsInfoFragment(GoodsInfo item) {
        String couponInfo = item.getCouponInfo();
        if (TextUtils.isEmpty(couponInfo)){
           start(GoodsInfoFragment.newInstance(item.getItemId()));
        }else {
           start(GoodsInfoFragment.newInstance(item));
        }
    }
    /**
     * 跳转到商品详情页(通过商品实体类，并走Activity)
     * 类型 默认0  0：好单库 1：淘宝全网
     * 如果有couponInfo：就判定为【好单库商品】，可以通过id来查询商品详情
     * 否则为【全网商品】，直接使用此对象
     */
    protected  void goGoodsInfoFragmentByActivity(GoodsInfo item) {
        String couponInfo = item.getCouponInfo();
        Intent intent = new Intent(_mActivity, WhiteBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT,GoodsInfoFragment.class.getSimpleName());
        if (TextUtils.isEmpty(couponInfo)) intent.putExtra("id",item.getItemId());
        else intent.putExtra("goodsInfo",item);
        startActivity(intent);
    }

    /**
     * 跳转到搜索结果页面
     * @param keyWord
     */
    public void goSearchFragment(String keyWord){
         Intent intent = new Intent(_mActivity, WhiteBarActivity.class);
         intent.putExtra(Contants.LOAD_FRAGMENT, SearchFragment.class.getSimpleName());
         intent.putExtra("keyWord",keyWord);
         startActivity(intent);
     }

    /**
     * 跳转到排行榜单页
     * @param sort
     */
    protected void goPracticalListFragment(int sort){
        Intent intent = new Intent(_mActivity, TransparentBarActivity.class);
        intent.putExtra(Contants.LOAD_FRAGMENT, PracticalListFragment.class.getSimpleName());
        intent.putExtra("sort", sort);
        startActivity(intent);
    }

    /**
     * 来到创建分享页面
     * @param goodsInfo
     */
    protected void goCreateShareFragment(GoodsInfo goodsInfo){
        Bundle bundle = new Bundle();
        bundle.putSerializable("goodsInfo",goodsInfo);
        goTransparentBarActivity(CreateShareFragment.class.getSimpleName(),bundle);
    }

     protected void goActivity(Class<?> activity,Bundle bundle){
         Intent intent = new Intent(_mActivity, activity);
         intent.putExtra("bundle",bundle);
         startActivity(intent);
     }


}
