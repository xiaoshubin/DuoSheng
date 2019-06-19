package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.custom.GoodsSortLinearLayout;
import com.qiqia.duosheng.main.bean.Classfiy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.smallcake_utils.SPUtils;

/**
 * 实用排行榜列表页
 * 1.大额券
 * 2.高佣榜
 * 3.白菜价
 * //可以占用排行榜的样式设置
 */

public class PracticalListFragment extends BaseBarFragment {
    int type = 1;
    int cid;
    int sort;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.sort_line)
    GoodsSortLinearLayout sortLine;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static PracticalListFragment newInstance(int sort) {
        Bundle args = new Bundle();
        args.putInt("sort", sort);
        PracticalListFragment fragment = new PracticalListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_practical_list;
    }

    /**
     * sort
     * 默认0，0.综合（最新），1.券后价(低到高)，2.券后价（高到低），3.券面额（高到低），4.月销量（高到低），5.佣金比例（高到低），6.券面额（低到高），
     * 7.月销量（低到高），8.佣金比例（低到高），9.全天销量（高到低），10全天销量（低到高），11.近2小时销量（高到低），12.近2小时销量（低到高），
     * 13.优惠券领取量（高到低），14.好单库指数（高到低）
     */
    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        sort = getArguments().getInt("sort");
        String title;
        switch (sort) {
            case 3:
                title = "大额券";
                break;
            case 5:
                title = "高佣榜";
                break;
            default:
                title = "白菜价";
                type = 2;
                break;
        }
        tvTitle.setText(title);
        initTabLayout();

    }

    private void initTabLayout() {
        List<Classfiy> allClassfiys = SPUtils.readObject(SPStr.CLASSFIYS);
        if (allClassfiys == null) return;
        for (int i = 0; i < allClassfiys.size(); i++) {
            Classfiy classfiy = allClassfiys.get(i);
            String mainName = classfiy.getMainName();
            tabLayout.addTab(tabLayout.newTab());
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            tabAt.setText(mainName);
        }
        initViewPager(allClassfiys);

    }

    private void onEvent(List<ItemPracticalListFragment> fragments) {
        //筛选条件排序
        sortLine.setListener(new GoodsSortLinearLayout.OnSortClickListener() {
            @Override
            public void onItemClick(int order) {
                fragments.get(pageIndex).onRefreshByFilter(order);
            }

            @Override
            public void onFilterClick( int lowPrice, int highPrice) {
                fragments.get(pageIndex).onRefreshByFilter(lowPrice,highPrice);
            }
        });
    }
    private int pageIndex;
    private void initViewPager(List<Classfiy> allClassfiys) {
        List<ItemPracticalListFragment> fragments = new ArrayList<ItemPracticalListFragment>();
        for (int i = 0; i < allClassfiys.size(); i++) {
            cid = allClassfiys.get(i).getCid();
            ItemPracticalListFragment itemRankingListFragment = ItemPracticalListFragment.newInstance(type, cid,sort);
            fragments.add(itemRankingListFragment);
        }
        FragmentManager fm = this.getChildFragmentManager();
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(fm) {
            @Override
            public int getCount() {
                return fragments.size();
            }
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return allClassfiys.get(position).getMainName();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(allClassfiys.size());
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pageIndex  = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        onEvent(fragments);

    }



}
