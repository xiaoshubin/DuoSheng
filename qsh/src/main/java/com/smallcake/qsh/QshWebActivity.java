package com.smallcake.qsh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smallcake.qsh.callback.OnConfigListener;
import com.smallcake.qsh.utils.ConfigJs;

public class QshWebActivity extends AppCompatActivity {
    private static final String TAG = "QshWebActivity";
    ProgressBar progressBar;
    WebView     webView;
    ConfigJs    configJs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qsh_web);
        LinearLayout layoutRoot = findViewById(R.id.layout_root);
        progressBar = findViewById(R.id.progress_bar);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);
        layoutRoot.addView(webView);

        String url = getIntent().getStringExtra("url");
        initSet(url);
        webView.loadUrl(url);

    }


    private void initSet(String url) {
        WebSettings webSettings = webView.getSettings();
        //基础配置相关
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速,避免错乱闪烁
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDomStorageEnabled(true);//避免有的网页dom加载不出来，较大存储空间，使用简单
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //https和http混用
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        }
        //缩放相关配置
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件，避免崩溃

        //安全相关设置
        webSettings.setSavePassword(false);//不保存密码
        webSettings.setSaveFormData(false);//不保存表单数据
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
//        webSettings.setJavaScriptEnabled(!url.startsWith("file://"));//如果是file类型链接，禁用javascript
        //移除下面三个不安全函数
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        //只要设置了WebViewClient，系统就不会再将url交给第三方的浏览器去处理了
        webView.setWebViewClient(new MyWebViewClient());
        //拦截js弹窗，进度，标题等
        webView.setWebChromeClient(new MyWebChromeClient());
        //下载监听
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //测试：启用js,js调用Android
        webSettings.setJavaScriptEnabled(true);
        configJs = new ConfigJs();
        configJs.init(this);
        webView.addJavascriptInterface(configJs, "qsh");
        onEvent(configJs);

    }

    private void onEvent(final ConfigJs qshJs) {
        qshJs.setListener(new OnConfigListener() {
            @Override
            public void ready() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "ready: ");
                    }
                });
            }
            @Override
            public void error(String msg) {
                Log.e(TAG, "error: " + msg);
            }
        });
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e(TAG, "onPageStarted==" + url);
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "onPageFinished==" + url);
        }

        //5.0以下重定向页面拦截
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "5.0以下重定向页面拦截==" + url);
            loadConfig();
            if (!url.startsWith("http")) {
                goPhoneWeb(QshWebActivity.this, url);//如果安装了对应应用，自动打开第三方应用app
                return true;
            }
            view.loadUrl(url);
            return true;
        }

        //5.0以上重定向页面拦截
        @TargetApi(21)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.e(TAG, "5.0以上重定向页面拦截==" + request.getUrl().toString());
                Uri uri = request.getUrl();
                String scheme = request.getUrl().getScheme();
                view.getSettings().setJavaScriptEnabled(!"file".equals(scheme));//如果是file协议，就禁用Javascript
                assert scheme != null;
                if (!scheme.equals("http") && !scheme.equals("https")) {
                    goPhoneWeb(QshWebActivity.this, uri.toString());//如果安装了对应应用，自动打开第三方应用app;
                    return true;
                }
                view.loadUrl(uri.toString());
            }
            return true;
        }

        //重写此方法才能处理浏览器中的按键事件
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();//接受所有网站的证书
        }
    }

    /**
     * 清除开关并重新加载配置数据
     */
    private void loadConfig() {
        ConfigJs.cleanMetheds();

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast.makeText(QshWebActivity.this, message, Toast.LENGTH_LONG).show();
            return super.onJsAlert(view, url, message, result);

        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            tvTitle.setText(title);

        }

        //网页加载进度
        @Override
        public void onProgressChanged(WebView view, int position) {
            progressBar.setProgress(position);
            progressBar.setVisibility(position == 100 ? View.GONE : View.VISIBLE);
            super.onProgressChanged(view, position);
        }

        //定位
        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        //多窗口的问题
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.goBack();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return false;
    }


    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers(); //小心这个！！！暂停整个 WebView 所有布局、解析、JS。
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }

    //webView的资源释放，对象清除
    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    //跳转第三方应用，如果安装了的话
    private void goPhoneWeb(Context context, String url) {
        try {
            Uri content_url = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "没有安装此第三方应用！");
        }

    }
}
