package com.qiqia.duosheng.classfiy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.classfiy.adapter.ClassfiyMenuTabAdapter;
import com.qiqia.duosheng.custom.VerticalViewPager;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.search.SearchHistoryFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.SPUtils;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 搜券，实际上是商品分类，并包含一个搜索
 */
public class SearchCouponFragment extends BaseFragment {


    @BindView(R.id.ele_bar)
    View eleBar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tab_layout)
    VerticalTabLayout tabLayout;
    @BindView(R.id.view_pager)
    VerticalViewPager viewPager;

    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).statusBarColor(R.color.transparent).autoDarkModeEnable(true).init();
    }

    public static SearchCouponFragment newInstance() {
        Bundle args = new Bundle();
        SearchCouponFragment fragment = new SearchCouponFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_search_coupon;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        allClassfiy();
    }

    public void allClassfiy() {

        Observable.create((Observable.OnSubscribe<List<Classfiy>>) subscriber -> {
            List<Classfiy> classfiys = SPUtils.readObject(SPStr.CLASSFIYS);
            if (classfiys == null || classfiys.size() == 0) return;
            subscriber.onNext(classfiys);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(classfiys -> {
                    ClassfiyMenuTabAdapter classfiyMenuTabAdapter = new ClassfiyMenuTabAdapter(classfiys);
                    tabLayout.setupWithViewPager(initViewPager(classfiys));
                    tabLayout.setTabAdapter(classfiyMenuTabAdapter);
                });


    }

    @OnClick({R.id.layout_search, R.id.iv_back, R.id.tv_right})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.layout_search:
                goWhiteBarActivity(SearchHistoryFragment.class.getSimpleName());
                break;
            case R.id.iv_back:
            case R.id.tv_right:
                MainViewPagerFragment.rgBottomTab.check(R.id.rb1);
                break;
        }
    }


    private ViewPager initViewPager(List<Classfiy> classfiys) {
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return ClassfiyFragment.newInstance(classfiys.get(i));
            }
            @Override
            public int getCount() {
                return classfiys.size();
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return classfiys.get(position).getMainName();
            }
        });
        return viewPager;
    }


}
