package com.qiqia.duosheng.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qiqia.duosheng.R;
import com.qiqia.duosheng.base.BaseBarFragment;
import com.qiqia.duosheng.base.BaseResponse;
import com.qiqia.duosheng.bean.User;
import com.qiqia.duosheng.utils.DataLocalUtils;
import com.qiqia.duosheng.utils.OnSuccessAndFailListener;

import butterknife.BindView;
import cn.com.smallcake_utils.L;
import cn.com.smallcake_utils.ToastUtil;

public class WebViewFragment extends BaseBarFragment {
    private static final String TAG = "WebViewFragment";
    @BindView(R.id.web_view)
    WebView webView;
    User user;
    public static WebViewFragment newInstance(String title, String url) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("url", url);
        WebViewFragment fragment = new WebViewFragment();
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
        user = DataLocalUtils.getUser();
        tvTitle.setText(title);
        initWebSet();


        if (url.startsWith("http")){
            webView.loadUrl(url);
        }else {
            webView.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
        }


//        webView.loadUrl("file:///android_asset/javascript.html");
//        webView.addJavascriptInterface(new AndroidJs(), "test");//AndroidtoJS类对象映射到js的test对象

    }
//百川JSBridge <script src="//g.alicdn.com/mtb/lib-BCJSBridge/0.3.5/bc-jsbridge.js"></script>
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
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
//            if (checkUrlCode(url)){
//                return false;
//            }
            wv.loadUrl(url);
            return true;
        }


    }

    /**
     * 检测出淘宝的code
     * @param url
     * http://adms.ds.jxmj.com/v4.php?ctr=DataSettings.code&code=KQtd9uKIr8PmO31LJdPl9o9B31994968&state=
     * http://adms.ds.jxmj.com/v4.php?ctr=DataSettings.code
     */
    private boolean checkUrlCode(String url) {
        String codeResponseUrl = "http://adms.ds.jxmj.com/v4.php?ctr=DataSettings.code&code=";
        String codeComplete = "=https://oauth.m.taobao.com/authorize.do";
        if (url.startsWith(codeResponseUrl)||url.startsWith(codeComplete)){
            if (url.startsWith(codeResponseUrl)){
                String code = url.substring(url.indexOf(codeResponseUrl)+codeResponseUrl.length(), url.indexOf("&state"));
                L.e(TAG,"code=="+code);
                bindDs(code);
            }
            return true;
        }
        return false;
    }

    private void bindDs(String code){
        dataProvider.user.bindDs(user.getUid(), user.getToken(),code)
                .subscribe(new OnSuccessAndFailListener<BaseResponse>(dialog) {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showLong("授权成功！");
                        popActivity();
                    }

                    @Override
                    protected void onErr(String msg) {
                        super.onErr(msg);
                        popActivity();
                    }
                });
    }


}
