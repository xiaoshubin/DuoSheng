package com.qiqia.duosheng.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class MainFragment extends BaseFragment {

    @BindView(R.id.rg_bottom_tab)
    RadioGroup rgBottomTab;
    Unbinder unbinder;
    private SupportFragment[] mFragments = new SupportFragment[5];

    @Override
    public int setLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {

        initFragment();
    }

    private void initFragment() {
        SupportFragment firstFragment = findChildFragment(HomeFragment.class);
        if (firstFragment == null) {
            mFragments[0] = HomeFragment.newInstance();
            mFragments[1] = HomeFragment.newInstance();
            mFragments[2] = HomeFragment.newInstance();
            mFragments[3] = HomeFragment.newInstance();
            mFragments[4] = HomeFragment.newInstance();
            loadMultipleRootFragment(R.id.container, 0, mFragments[0], mFragments[1], mFragments[2], mFragments[3],mFragments[4]);
        } else {
            mFragments[0] = firstFragment;
            mFragments[1] = findChildFragment(HomeFragment.class);
            mFragments[2] = findChildFragment(HomeFragment.class);
            mFragments[3] = findChildFragment(HomeFragment.class);
            mFragments[4] = findChildFragment(HomeFragment.class);
        }
        rgBottomTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb1: showHideFragment(mFragments[0]);break;
                    case R.id.rb2: showHideFragment(mFragments[1]);break;
                    case R.id.rb3: showHideFragment(mFragments[2]);break;
                    case R.id.rb4: showHideFragment(mFragments[3]);break;
                    case R.id.rb5: showHideFragment(mFragments[4]);break;


                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
