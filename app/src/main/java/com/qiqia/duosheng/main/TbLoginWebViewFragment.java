package com.qiqia.duosheng.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.utils.DemoTradeCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.com.smallcake_utils.AppUtils;
import cn.com.smallcake_utils.L;

public class TbLoginWebViewFragment extends BaseBarFragment {
    private static final String TAG = "TbLoginWebViewFragment";
    @BindView(R.id.layout_bar)
    LinearLayout layoutBar;
    @BindView(R.id.web_view)
    WebView webView;

    public static TbLoginWebViewFragment newInstance(String title, String url) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("url", url);
        TbLoginWebViewFragment fragment = new TbLoginWebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_webview;
    }


    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        String url = bundle.getString("url");
        tvTitle.setText(title);
        initWebSet();
        openTbLoginBySdkWeb(url);
    }
    private void openTbLoginBySdkWeb(String url){
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, AppUtils.getVersionName());
        AlibcBasePage detailPage = new AlibcPage(url);
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, true);
        AlibcTrade.show(_mActivity,webView,new MyWebViewClient(), new WebChromeClient(), detailPage, showParams, null, exParams, new DemoTradeCallback());
    }
    private void initWebSet() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
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
        }



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("taobao")){
                return false;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
