package com.qiqia.duosheng.base;

public interface SPStr {
    String CLASSFIYS="classfiys";//首页所有分类：首页顶部弹出POP,搜券页面，排行榜顶部Tab,排行榜子页面
    String INDEX_CLASSFIY_LIST="IndexClassfiyList";//首页顶部Tab和子页面使用
    String USER="user";//登录后的用户对象信息，序列化后保存

    String GUIDE_HOME_FRAGMENT="guide_home_fragment";//HomeFragment:第一次进入主页，蒙版
    String GUIDE_SEARCH_FRAGMENT="guide_search_fragment";//HomeFragment:第一次进入搜索结果列表页，蒙版
    String GUIDE_GOODSINFO_FRAGMENT="guide_goodsinfo_fragment";//HomeFragment:第一次进入商品详情页，蒙版
    String GUIDE_MINE_FRAGMENT="guide_mine_fragment";//HomeFragment:第一次进入我的，蒙版
    String GUIDE_VIEWPAGER_FRAGMENT="guide_viewpager_fragment";//GuideViewPagerFragment:第一次进入多页引导
}
