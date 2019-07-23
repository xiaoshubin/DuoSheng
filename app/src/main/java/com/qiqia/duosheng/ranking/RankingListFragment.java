package com.qiqia.duosheng.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.utils.TabCreateUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SPUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RankingListFragment extends BaseFragment {
    public static ViewPager viewPager;
    List<Classfiy> allClassfiys;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).statusBarColor(R.color.transparent).autoDarkModeEnable(true).init();
    }

    public static RankingListFragment newInstance() {
        Bundle args = new Bundle();
        RankingListFragment fragment = new RankingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_ranking_list;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.view_pager);
        allClassfiys = new ArrayList<>();
        allClassfiys.add(new Classfiy("实时榜"));
        allClassfiys.add(new Classfiy("24时榜"));

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initAllClassify();
    }

    public void initAllClassify() {
        Observable.create(new Observable.OnSubscribe<List<Classfiy>>() {
            @Override
            public void call(Subscriber<? super List<Classfiy>> subscriber) {
                long t1 = System.currentTimeMillis();
                List<Classfiy> classfiys = SPUtils.readObject(SPStr.CLASSFIYS);
                long t2 = System.currentTimeMillis();
                L.e("查询Classfiy耗时==" + (t2 - t1));

                if (classfiys == null || classfiys.size() == 0) return;
                allClassfiys.addAll(classfiys);
                subscriber.onNext(classfiys);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Classfiy>>() {
                    @Override
                    public void call(List<Classfiy> classfiys) {
                        initTabLayout();
                    }
                });


    }

    private void initTabLayout() {
        if (allClassfiys == null || allClassfiys.size() == 0) return;
        TabCreateUtils.setDefaultTab(this.getContext(), magicIndicator, viewPager, allClassfiys);
        initViewPager();
    }


    private void initViewPager() {
        FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return allClassfiys.size();
            }

            @Override
            public Fragment getItem(int i) {
                if (i == 0) {//实时榜
                    return ItemRankingListFragment.newInstance(1, 0);
                } else if (i == 1) {//24小时榜
                    return ItemRankingListFragment.newInstance(2, 0);
                } else {//其他分类
                    return ItemRankingListFragment.newInstance(0, allClassfiys.get(i).getCid());
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return allClassfiys.get(position).getMainName();
            }
        };
        long t1 = System.currentTimeMillis();
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(allClassfiys.size() / 3);//130ms
        long t2 = System.currentTimeMillis();
        L.e("预加载耗时==" + (t2 - t1));
    }
}
