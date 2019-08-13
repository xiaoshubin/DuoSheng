package com.qiqia.duosheng.classfiy;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.classfiy.adapter.ClassfiyMenuTabAdapter;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.search.SearchHistoryFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.smallcake_utils.SPUtils;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;
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
    ViewPager2 viewPager;

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
                    //注意：先关联TabLayout和ViewPager,后设置适配器，避免TabLayout样式丢失
//                    tabLayout.setupWithViewPager(initViewPager(classfiys));
                    setupWithViewPager( initViewPager(classfiys),tabLayout);
                    tabLayout.setTabAdapter(new ClassfiyMenuTabAdapter(classfiys));
                });

    }

    public void setupWithViewPager(@Nullable ViewPager2 viewPager, @Nullable VerticalTabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                if (viewPager != null && viewPager.getAdapter().getItemCount() >= position) {
                    viewPager.setCurrentItem(position);
                }
            }
            @Override
            public void onTabReselected(TabView tab, int position) {
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.setTabSelected(position,true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
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

    /**
     * 初始化ViewPager片段数据
     * Classfiy为分类数据对象，每个人不一样，自己定义
     */
    private ViewPager2 initViewPager(List<Classfiy> classfiys) {
        viewPager.setAdapter(new FragmentStateAdapter(_mActivity) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return ClassfiyFragment.newInstance(classfiys.get(position));
            }

            @Override
            public int getItemCount() {
                 return classfiys.size();
            }

//            @Override
//            public CharSequence getPageTitle(int position) {
//                return classfiys.get(position).getMainName();
//            }

        });

//        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int i) {
//                return ClassfiyFragment.newInstance(classfiys.get(i));
//            }
//            @Override
//            public int getCount() {
//                return classfiys.size();
//            }
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return classfiys.get(position).getMainName();
//            }
//        });
        return viewPager;
    }


}
