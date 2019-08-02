package com.qiqia.duosheng;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ali.auth.third.ui.context.CallbackContext;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.qiqia.duosheng.base.BaseActivity;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.base.SPStr;
import com.qiqia.duosheng.bean.IndexAd;
import com.qiqia.duosheng.bean.SearchKey;
import com.qiqia.duosheng.dialog.GoodsAdvertPop;
import com.qiqia.duosheng.dialog.PushSetPop;
import com.qiqia.duosheng.dialog.SearchWordDialog;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.com.smallcake_utils.ActivityCollector;
import cn.com.smallcake_utils.ClipboardUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SPUtils;
import cn.com.smallcake_utils.TimeUtils;


public class MainActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        ImmersionBar.with(this).statusBarColor(R.color.white).autoStatusBarDarkModeEnable(true, 0.2f).supportActionBar(true).init();
        if (findFragment(MainViewPagerFragment.class) == null) {
            loadRootFragment(R.id.content, new MainViewPagerFragment());
        }
//        if (findFragment(CoordinatorLayoutTestFragment.class) == null) {
//            loadRootFragment(R.id.content, new CoordinatorLayoutTestFragment());
//        }
        getPermission(); //权限管理
        initSearchDialog();   //智能搜索
        getIndexAd(); //APP首页广告弹窗
        showPushSetPop(); //推送设置弹窗



    }

    /**
     * 大于一天就显示一次推送设置弹窗
     */
    private void showPushSetPop() {
        DateTime nowTime = DateTime.now();
        DateTime lastTime =  SPUtils.readObject(SPStr.SHOW_PUSH_SET_TIME);
        if (lastTime==null||Days.daysBetween(lastTime, nowTime).getDays()>0){
            new XPopup.Builder(this).asCustom(new PushSetPop(this)).show();
            SPUtils.saveObject(SPStr.SHOW_PUSH_SET_TIME,DateTime.now());
        }
    }


    /**
     * time 为此广告修改时间
     * frequency 0：每天展示一次 1：只展示一次
     */
    private void getIndexAd() {
        dataProvider.shop.indexAd()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<IndexAd>>() {
                    @Override
                    protected void onSuccess(BaseResponse<IndexAd> indexAdBaseResponse) {
                        IndexAd data = indexAdBaseResponse.getData();
                        int adId = data.getAdId();
                        LitePal.where("adId=" + adId).findAsync(IndexAd.class).listen(new FindMultiCallback() {
                            @Override
                            public <T> void onFinish(List<T> t) {
                                if (t == null || t.size() == 0) {
                                    showGoodsPop(data);
                                    data.setShowTime(TimeUtils.getTime());
                                    data.save();
                                } else {
                                    List<IndexAd> indexAdList = (List<IndexAd>) t;
                                    IndexAd indexAd = indexAdList.get(0);
                                    showAdOrNot(indexAd, data);

                                }

                            }
                        });
                    }
                });
    }

    /**
     * 已经保存了数据后，是否需要展示
     *
     * @param indexAd 本地数据库查询出来的广告对象
     * @param data    网络请求的广告对象
     */
    private void showAdOrNot(IndexAd indexAd, IndexAd data) {
        int time = data.getTime();//网络对象的最新一次修改时间
        int frequency = data.getFrequency();//0 ：不需要每天展示，1，需要每天展示一次
        int localTime = indexAd.getTime();//本地存储的修改时间
        if (localTime != time) {//如果修改过，立即展示，并保存时间
            showGoodsPop(data);
            indexAd.setTime(time);
            indexAd.save();
        } else if (localTime == time && frequency == 0) {//如果没有修改过，且需要每天展示，对比展示时间，如果次展示时间和当前时间差距一天，就展示
            int currentTime = TimeUtils.getTime();//当前时间
            int showTime = indexAd.getShowTime();
            Date currentDate = new Date(currentTime);
            Date showDate = new Date(showTime);
            //如果当前时间天数在指定时间之后，展示广告
            if (isShow(showDate, currentDate)) {
                showGoodsPop(data);
                indexAd.setShowTime(currentTime);
                indexAd.save();
            }

        }
    }

    //时间要大于1天以上才展示
    public static boolean isShow(Date oldTime, Date currentTime) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(oldTime);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(currentTime);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1 > 0;

    }

    /**
     * 展示商品
     *
     * @param data
     */
    private void showGoodsPop(IndexAd data) {
        new XPopup.Builder(this)
                .asCustom(new GoodsAdvertPop(this, data)).show();

    }

    BasePopupView smartSearchPop;
    SearchWordDialog searchWordDialog;

    private void initSearchDialog() {
        String paste = ClipboardUtils.paste();
        paste = TextUtils.isEmpty(paste) ? "牙刷" : paste;
        searchWordDialog = new SearchWordDialog(this, paste);
        smartSearchPop = new XPopup
                .Builder(this)
                .asCustom(searchWordDialog);
    }

    /**
     * 阿里百川回调
     * 为了是能够正常接收登陆组建的结果回调，需要开发者在使用登陆功能的Activity中重写onActivityResult方法，
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);
        L.e("requestCode==" + requestCode + "  resultCode" + resultCode + "  data" + data.getDataString());
    }

    /**
     * 首页申请权限
     * READ_PHONE_STATE：手机标示
     * READ_EXTERNAL_STORAGE，WRITE_EXTERNAL_STORAGE 读写存储权限
     */
    private void getPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(data -> L.e("以获得权限" + data.toString()))
                .onDenied(data -> L.e("未获得权限" + data.toString())).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSearchStr();
    }

    /**
     * 智能搜索：
     * 1.如果应用没有退出到后台(回到前台)，不再执行，避免应用跳其他页面回到主页弹出智能搜索-->return
     * 2.只要通过了前一项，就把后台参数置为false,避免进入其他页回退到主页后重复弹出智能搜索
     * 3.如果是第一次新用户引导，不执行，避免引导蒙版和智能搜索重复-->return
     * 4.如果没有粘贴内容，不执行-->return
     * 5.如果是最近一段时间搜索过的词条，不进行弹出，避免相同的复制内容，因为切换了应用而频繁弹出-->return
     * 6.如果是淘宝链接过来的，截取其中的标题关键字部分数据来进行搜寻，避免内容过长
     * 7.如果内容大于88，不认为是属于正常搜索词-->return
     * 8.刷新搜索弹出窗内容，并弹出弹出框
     */
    private void getSearchStr() {
        if (!MyApplication.isRunInBackground) return;

        MyApplication.isRunInBackground = false;

        int guideHomeFragment = (int) SPUtils.get(SPStr.GUIDE_HOME_FRAGMENT, 0);
        if (guideHomeFragment == 0) return;

        String paste = ClipboardUtils.paste();
        if (TextUtils.isEmpty(paste)) return;

        SearchKey lastSearchKey = LitePal.order("time desc").findFirst(SearchKey.class);
        if (lastSearchKey != null && paste.equals(lastSearchKey.getKey())) return;

        if (paste.contains("淘♂寳♀")) paste = paste.substring(paste.indexOf("【") + 1, paste.indexOf("】"));

        if (paste.length()>88)return;

        searchWordDialog.setMsg(paste);
        smartSearchPop.show();

    }

    //再按一次退出程序
    private long firstTime = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                //如果两次按键时间间隔大于2秒，则不退出
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                    //两次按键小于2秒时，退出应用
                } else {
                    ActivityCollector.finishAll();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}
