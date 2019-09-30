package com.qiqia.duosheng.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarActivity;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.search.bean.GoodsInfo;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.SpannableStringUtils;
import cn.com.smallcake_utils.ToastUtil;

/**
 * 专为首页各类天猫超时而生
 * 淘宝，天猫超市，天猫国际，天猫生鲜，聚划算
 */
public class WebViewShopActivity extends BaseBarActivity {

    private static final String TAG = "WebViewShopActivity";

    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.layout_search_coupon)
    LinearLayout layoutSearchCoupon;
    @BindView(R.id.tv_share_get)
    TextView tvShareGet;
    @BindView(R.id.layout_share)
    LinearLayout layoutShare;
    @BindView(R.id.tv_ownbuy_recom)
    TextView tvOwnbuyRecom;
    @BindView(R.id.layout_buy)
    LinearLayout layoutBuy;
    @BindView(R.id.layout_share_and_buy)
    LinearLayout layoutShareAndBuy;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("dataProvider=="+(dataProvider==null));
        setContentView(R.layout.activity_web_view);
        ImmersionBar.with(this).statusBarColor(R.color.transparent).init();
        ButterKnife.bind(this);
        user = DataLocalUtils.getUser();
        initWebViewSet();
        //加载链接和标题
        Bundle bundle = getIntent().getBundleExtra("bundle");
        String title = bundle.getString("title");
        String url = bundle.getString("url");
        tvTitle.setText(title);
        webView.loadUrl(url);
    }

    private void initWebViewSet() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            L.e(TAG, "onPageStarted==" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            L.e(TAG, "onPageFinished==" + url);
            hookGoodsId(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
            if (url == null) return false;
            L.e(TAG, "url==" + url);
            try {
                if (openOtherApp(url)) return false;
            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }
            wv.loadUrl(url);
            return true;
        }

        private boolean openOtherApp(String url) {
            L.e(TAG, "openOtherApp==" + url);
//            startAction(url);
            return url.startsWith("weixin://") //微信
                    || url.startsWith("alipays://") //支付宝
                    || url.startsWith("mailto://") //邮件
                    || url.startsWith("tel://")//电话
                    || url.startsWith("dianping://")//大众点评
                    || url.startsWith("tbopen://")//淘宝
                    || url.startsWith("tmall://");//天猫
        }

    }

    /**
     *
     * http://www.zaoxingwu.cn/v4.php?ctr=App_Shop.GoodsInfo&uid=13&id=579751705261&Type=0
     * 1.聚划算
     * https://ju.taobao.com/m/jusp/alone/detailwap/mtp.htm?item_id=565140975682
     * 2.天猫超时
     * https://detail.m.tmall.com/item.htm?spm=a3204.11780841.todayCrazy-78.d0&id=22109068630&addressId=0&storeCode=0&skuId=45631121136
     * 3.天猫生鲜
     * https://detail.m.tmall.com/item.htm?spm=a223ee.11136065/N.4501620082.1&acm=lb-zebra-305314-3197742.1003.4.4572411&id=593261224875&scm=1003.4.lb-zebra-305314-3197742.OTHER_593261224875_4572411
     * @param url
     */
    private void hookGoodsId(String url) {
        String jhs = "https://ju.taobao.com/m/jusp/alone/detailwap/mtp.htm?item_id=";
        if (url.startsWith(jhs)){
            layoutSearchCoupon.setVisibility(View.VISIBLE);
            String item_id = url.substring(url.indexOf(jhs)+jhs.length(), url.indexOf("&"));
            L.e(TAG,"截取的聚划算商品Id = "+item_id);
            if (!TextUtils.isEmpty(item_id)){
                layoutSearchCoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getGoodsInfo(item_id);
                    }
                });
            }
        }else {
            layoutSearchCoupon.setVisibility(View.GONE);
            layoutShareAndBuy.setVisibility(View.GONE);
        }
    }

    private void getGoodsInfo(String id) {
        dataProvider.shop.goodsInfo(user==null?"0":user.getUid(),id, 0)
                .subscribe(new OnSuccessAndFailListener<BaseResponse<GoodsInfo>>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse<GoodsInfo> listBaseResponse) {
                        GoodsInfo goodsInfo = listBaseResponse.getData();
                        if (goodsInfo == null) {
                            ToastUtil.showLong("暂无商品优惠券信息！");
                            return;
                        }
                        initData(goodsInfo);
                    }
                    private void initData(GoodsInfo goodsInfo) {
                        String commission_price = goodsInfo.getCommision();//预估收益
                        layoutSearchCoupon.setVisibility(View.GONE);
                        layoutShareAndBuy.setVisibility(View.VISIBLE);
                        //分享赚，自购返
                        SpannableStringBuilder _commission_price = SpannableStringUtils.getBuilder("￥").setProportion(0.5f)
                                .append(commission_price).create();
                        tvShareGet.setText(_commission_price);
                        tvOwnbuyRecom.setText(_commission_price);
                    }
                });
    }


}
