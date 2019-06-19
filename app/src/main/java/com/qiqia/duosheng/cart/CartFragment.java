package com.qiqia.duosheng.cart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.lsxiao.apollo.core.annotations.Receive;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.EventStr;
import com.qiqia.duosheng.main.MainViewPagerFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.smallcake_utils.AppUtils;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;

/**
 * 由于购物车获取商品优惠券信息非法，此项占不使用
 */
@Deprecated
public class CartFragment extends BaseBarFragment {
    @BindView(R.id.web_view)
    WebView webView;
    Unbinder unbinder;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.btn_tblogin)
    Button btnTblogin;
    @BindView(R.id.view_pager)
    NestedScrollView viewPager;
    @BindView(R.id.layout_root)
    CoordinatorLayout layoutRoot;

    public static CartFragment newInstance() {

        Bundle args = new Bundle();

        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        tvTitle.setText("购物车");
        initWebSet();


    }
    //首先验证是否通过了淘宝登录授权，如果没通过就进行登录授权，通过后显示购物车数据
    @Receive(EventStr.ALIBCTRADESDK_INIT_SUCCESS)
    public void tbLoginOrShowCart() {
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        boolean login = alibcLogin.isLogin();
        if (login) {
            btnTblogin.setVisibility(View.GONE);
            showCartList();
        } else {
            btnTblogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        tbLoginOrShowCart();
    }

    /**
     * 559025985707
     */
    public void showCartList() {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcMyCartsPage alibcMyCartsPage = new AlibcMyCartsPage();
        String api = alibcMyCartsPage.getApi();
        String OpenUrl = alibcMyCartsPage.genOpenUrl();
        String performancePageType = alibcMyCartsPage.getPerformancePageType();
        String usabilityPageType = alibcMyCartsPage.getUsabilityPageType();

//        L.e("api=="+api);
//        L.e("OpenUrl=="+OpenUrl);
//        L.e("performancePageType=="+performancePageType);
//        L.e("usabilityPageType=="+usabilityPageType);
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);
        int show = AlibcTrade.show(_mActivity, webView, new MyWebViewClient(), new MyWebChromeClient(), alibcMyCartsPage, showParams, null, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(TradeResult tradeResult) {
                L.e("成功");
            }

            @Override
            public void onFailure(int i, String s) {
                L.e("失败");
            }
        });
    }

    private void alibcLogin() {
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(_mActivity, new AlibcLoginCallback() {
            @Override
            public void onSuccess() {
                //获取淘宝用户信息
                ToastUtil.showShort("授权成功！");
                L.e("获取淘宝用户信息: " + AlibcLogin.getInstance().getSession());

            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtil.showShort("授权失败！");
                L.e(code + "淘宝登录失败: " + msg);
            }
        });

    }

    private void initWebSet() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.setWebChromeClient(new MyWebChromeClient());
    }
    //https://h5.m.taobao.com/mlapp/cart.html?umpChannel=1-26043462&u_channel=1-26043462&ybhpss=dHRpZD0yMDE0XzBfMjYwNDM0NjIlNDBiYWljaHVhbl9hbmRyb2lkXzMuMS4zLjU%3D%0A&ttid=2014_0_26043462@baichuan_android_3.1.3.5&isv_code=1.0
    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            L.e("onPageStarted==" + url);
        }



        @Override
        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            L.e("onPageFinished==" + url);
            view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                    + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null) return false;
            L.e("url==" + url);

            try {
                if (url.startsWith("weixin://") //微信
                        || url.startsWith("alipays://") //支付宝
                        || url.startsWith("mailto://") //邮件
                        || url.startsWith("tel://")//电话
                        || url.startsWith("dianping://")//大众点评
                    //其他自定义的scheme
                ) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                if (url.startsWith("tbopen://")) {
                    return false;
                }
            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }
            //处理http和https开头的url
            view.loadUrl(url);
            return true;
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            getGoodsList(html);
//            refreshHtmlContent(html);
        }
    }
    private void refreshHtmlContent(final String html){
        L.iAll("网页内容=="+html);


//        webView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //解析html字符串为对象
//                Document document = Jsoup.parse(html);
//                //通过类名获取到一组Elements，获取一组中第一个element并设置其html
////                Elements elements = document.getElementsByClass("loadDesc");
////                elements.get(0).html("<p>加载完成</p>");
////
////                //通过ID获取到element并设置其src属性
////                Element element = document.getElementById("imageView");
////                element.attr("src","file:///android_asset/dragon.jpg");
////
////                //通过类名获取到一组Elements，获取一组中第一个element并设置其文本
////                elements = document.select("p.hint");
////                elements.get(0).text("您好，我是龙猫军团！");
//
//                //获取进行处理之后的字符串并重新加载
//                String body = document.toString();
//                webView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
//            }
//        },5000);
    }
    //抽取元素
    private void getGoodsList(String html){
        //解析html字符串为对象
        Document document = Jsoup.parse(html);
        Elements script = document.select("script");
        String s = script.get(4).outerHtml();
        //<script src="//acs.m.taobao.com/h5/com.taobao.wireless.chanel.realtimerecommond/2.0/?jsv=2.3.26&amp;appKey=12574478&amp;t=1557735999213&amp;sign=dc0df843829f5ef82c3596d1916b0367&amp;v=2.0&amp;api=com.taobao.wireless.chanel.realTimeRecommond&amp;isSec=0&amp;requiredParams=albumId&amp;type=jsonp&amp;dataType=jsonp&amp;callback=mtopjsonp2&amp;data=%7B%22albumId%22%3A%22PAY_SUCCESS%22%2C%22param%22%3A%22%7B%5C%22appid%5C%22%3A8975%2C%5C%22isSupportContent%5C%22%3A0%7D%22%7D" async></script>
        String str = s.substring(s.indexOf("//"), s.lastIndexOf("\""));

//        L.e("商品列表Html=="+str);
    }

    class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            ToastUtil.showShort(message);
            return true;
        }
    }




    @OnClick({R.id.iv_back,R.id.btn_tblogin})
    public void doClicks(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MainViewPagerFragment.rgBottomTab.check(R.id.rb1);
                break;
            case R.id.btn_tblogin:
                alibcLogin();
                break;
        }
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
