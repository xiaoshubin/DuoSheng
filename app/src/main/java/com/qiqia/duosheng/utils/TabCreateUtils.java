package com.qiqia.duosheng.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.custom.SelectBigPagerTitleView;
import com.qiqia.duosheng.main.bean.Classfiy;
import com.qiqia.duosheng.main.bean.IndexClassfiy;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

//菜单指示器创建工具类
public class TabCreateUtils {
    /**
     * 类型1：用于排行榜
     * 白字：选中与未选中都为白色
     * 白指示器：指示器长度和文字长度相同
     */
    public static void setDefaultTab(Context context,MagicIndicator magicIndicator, ViewPager viewPager, List<Classfiy> allClassfiys) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return allClassfiys == null ? 0 : allClassfiys.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.WHITE);
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setText(allClassfiys.get(index).getMainName());
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
    /**
     * 类型2：用于首页顶部分类
     * 白字：选中与未选中都为白色
     * 白指示器：指示器长度和文字长度相同
     */
    public static void setDefaultTab2(Context context,MagicIndicator magicIndicator, ViewPager viewPager, List<IndexClassfiy> allClassfiys) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return allClassfiys == null ? 0 : allClassfiys.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.WHITE);
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setText(allClassfiys.get(index).getMainName());
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    /**
     * 类型3：用于分享赚钱
     * 字：选中橘色，未选中黑色，加粗
     * 指示器：指示器长度和文字长度相同，橘色
     */
    public static void setOrangeTab(Context context,MagicIndicator magicIndicator, ViewPager viewPager, String[] tabNames) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.text_black));
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.tab_orange));
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setText(tabNames[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(ContextCompat.getColor(context, R.color.tab_orange));
                indicator.setRoundRadius(3);
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
    public static void setGuideIndicator(Context context,MagicIndicator magicIndicator, ViewPager viewPager, String[] tabNames) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.white));
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.white));
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setText(tabNames[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(ContextCompat.getColor(context, R.color.tab_orange));
//                indicator.setRoundRadius(3);
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
    /**
     * 类型：不关联ViewPager
     * 字：选中橘色，未选中黑色，加粗
     * 指示器：指示器长度和文字长度相同，橘色
     */
    public interface onTitleClickListener{
        void onTitleClick(int index);
    }
    public static void setOrangeTab(Context context,MagicIndicator magicIndicator, String[] tabNames ,onTitleClickListener listener) {
        FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SelectBigPagerTitleView colorTransitionPagerTitleView = new SelectBigPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.text_black));
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.tab_orange));
                colorTransitionPagerTitleView.setText(tabNames[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> {
                    mFragmentContainerHelper.handlePageSelected(index);
                    if (listener!=null)listener.onTitleClick(index);
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(ContextCompat.getColor(context, R.color.tab_orange));
                indicator.setRoundRadius(3);
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
    }
    public static void setWhiteTab(Context context,MagicIndicator magicIndicator, String[] tabNames ,onTitleClickListener listener) {
        FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SelectBigPagerTitleView colorTransitionPagerTitleView = new SelectBigPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.white));
                colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.white));
                colorTransitionPagerTitleView.setText(tabNames[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> {
                    mFragmentContainerHelper.handlePageSelected(index);
                    if (listener!=null)listener.onTitleClick(index);
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(ContextCompat.getColor(context, R.color.white));
                indicator.setRoundRadius(3);
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
    }
}
