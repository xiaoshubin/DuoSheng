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
import com.qiqia.duosheng.dialog.SearchWordDialog;
import com.qiqia.duosheng.main.MainViewPagerFragment;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

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

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        ImmersionBar.with(this).statusBarColor(R.color.white).autoStatusBarDarkModeEnable(true, 0.2f).supportActionBar(true).init();
        if (findFragment(MainViewPagerFragment.class) == null) {
            loadRootFragment(R.id.content, new MainViewPagerFragment());
        }
        //权限管理
        getPermission();
        //智能搜索
        initSearchDialog();
        //APP首页广告弹窗
        getIndexAd();

        //TODO 1.阿里百川测试
//        L.e(TAG,"是否登录淘宝授权=="+ AlibcUtils.isLoginTb());
        //昵称，头像，openid，openSid
//        L.e(TAG,"淘宝信息=="+ AlibcLogin.getInstance().getSession());
//        AlibcUtils.loginH5(this,dataProvider);
    }


    /**
     * time 为此广告修改时间
     * frequency 1：只展示一次
     * 0：每天展示一次
     */
    private void getIndexAd() {
        dataProvider.shop.indexAd()
                .subscribe(new OnSuccessAndFailListener<BaseResponse<IndexAd>>() {
                    @Override
                    protected void onSuccess(BaseResponse<IndexAd> indexAdBaseResponse) {
                        IndexAd data = indexAdBaseResponse.getData();
                        int time = data.getTime();
                        int frequency = data.getFrequency();
                        int adId = data.getAdId();
                        LitePal.where("adId=" + adId).findAsync(IndexAd.class).listen(new FindMultiCallback() {
                            @Override
                            public <T> void onFinish(List<T> t) {
                                if (t==null||t.size()==0){
                                    showGoodsPop(data);
                                    data.setShowTime(TimeUtils.getTime());
                                    data.save();
                                    return;
                                }else {
                                    List<IndexAd> indexAdList = (List<IndexAd>) t;
                                    IndexAd indexAd = indexAdList.get(0);
                                    showAdOrNot(indexAd, frequency, time, data);
                                }

                            }
                        });
                    }
                });
    }

    /**
     * 已经保存了数据后，是否需要展示
     * @param indexAd 本地数据库查询出来的时间
     * @param frequency
     * @param time
     * @param data
     */
    private void showAdOrNot(IndexAd indexAd, int frequency, int time, IndexAd data) {
        int localTime = indexAd.getTime();//本地存储的修改时间
        if (localTime != time) {//如果修改过，立即展示，并保存时间
            showGoodsPop(data);
            indexAd.setTime(time);
            indexAd.save();
        }else  if (localTime == time&&frequency == 0){//如果没有修改过，且需要每天展示，对比展示时间，如果次展示时间和当前时间差距一天，就展示
            int currentTime = TimeUtils.getTime();//当前时间
            int showTime = indexAd.getShowTime();
            Date currentDate = new Date(currentTime);
            Date showDate = new Date(showTime);
            //如果当前时间天数在指定时间之后，展示广告
            if (isShow(showDate,currentDate)){
                showGoodsPop(data);
                indexAd.setShowTime(currentTime);
                indexAd.save();
            }

        }
    }

    public static boolean isShow(Date oldTime, Date currentTime) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(oldTime);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(currentTime);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1>0;

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

    BasePopupView basePopupView;
    SearchWordDialog searchWordDialog;

    private void initSearchDialog() {
        String paste = ClipboardUtils.paste();
        paste = TextUtils.isEmpty(paste) ? "牙刷" : paste;
        searchWordDialog = new SearchWordDialog(this, paste);
        basePopupView = new XPopup.Builder(this).asCustom(searchWordDialog);
    }

    /**
     * 阿里百川
     * 为了是能够正常接收登陆组建的结果回调，需要开发者在使用登陆功能的Activity中重写onActivityResult方法，
     *
     * @param requestCode
     * @param resultCode
     * @param data
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
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("以获得权限" + data.toString());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        L.e("未获得权限" + data.toString());
                    }
                }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSearchStr();
    }

    /**
     * 获取粘贴板内容,以便智能搜索
     * 1.如果应用没有退出到后台，不再执行，避免应用跳其他页面回到主页弹出智能搜索
     * 2.如果是第一次新用户引导，不执行，避免引导蒙版和智能搜索重复
     * 3.只要通过了前两项，就把后台参数置为false,避免进入其他页回退到主页后重复弹出智能搜索
     * 4.如果没有粘贴内容，不执行
     * 5.如果是最近搜索过的词条，不进行弹出,return
     * 6.如果是淘宝链接过来的，截取其中的标题关键字部分数据
     */
    private void getSearchStr() {
        if (!MyApplication.isRunInBackground) return;
        int guideHomeFragment = (int) SPUtils.get(SPStr.GUIDE_HOME_FRAGMENT, 0);
        if (guideHomeFragment == 0) return;
        MyApplication.isRunInBackground = false;
        String paste = ClipboardUtils.paste();
        if (!TextUtils.isEmpty(paste)) {
            SearchKey lastSearchKey = LitePal.order("time desc").findFirst(SearchKey.class);
            if (lastSearchKey!=null&&paste.equals(lastSearchKey.getKey()))return;
            if (paste.contains("淘♂寳♀")) paste = paste.substring(paste.indexOf("【") + 1, paste.indexOf("】"));
            searchWordDialog.setMsg(paste);
            basePopupView.show();
        }
    }


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
