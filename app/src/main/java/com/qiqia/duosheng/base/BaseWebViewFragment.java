package com.qiqia.duosheng.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.qiqia.duosheng.AndroidJs;
import com.qiqia.duosheng.R;

import butterknife.BindView;
import cn.com.smallcake_utils.L;

/**
 * WebView相关漏洞：
 * <p>
 * 1.WebView 中 addJavascriptInterface（） 接口
 * 解决：Google 在Android 4.2 版本中规定对被调用的函数以 @JavascriptInterface进行注解从而避免漏洞攻击
 * <p>
 * 2.在Android 3.0以下，Android系统会默认通过searchBoxJavaBridge_的Js接口给 （可忽略）
 * <p>
 * 3.密码明文存储漏洞，密码会被明文保到 /data/data/com.package.name/databases/webview.db 中，这样就有被盗取密码的危险
 * 解决：默认开启保存密码提示，设置关闭WebSettings.setSavePassword(false),webSettings.setSaveFormData(false);
 * <p>
 * 4.域控制不严格漏洞：即 A 应用可以通过 B 应用导出的 Activity 让 B 应用加载一个恶意的 file 协议的 url，从而可以获取 B 应用的内部私有文件，从而带来数据泄露威胁
 * 如果在AndroidManifest.xml中配置了此Activity的exported属性为true,android:exported="true"说明当前Activity是否可以被另一个Application的组件启动
 * 具体：当其他应用启动此 Activity 时， intent 中的 data 直接被当作 url 来加载（假定传进来的 url 为 file:///data/local/tmp/attack.html ），
 * 其他 APP 通过使用显式 ComponentName 或者其他类似方式就可以很轻松的启动该 WebViewActivity 并加载恶意url。
 * 解决：设置android:exported="false",不要把传递的data当做url加载
 * <p>
 * 5.WebView中getSettings类的方法setAllowFileAccess（）
 * webView.getSettings().setAllowFileAccess(true);设置是否允许 WebView 使用 File 协议，默认设置为true，即允许在 File 域下执行任意 JavaScript 代码
 * 使用 file 域加载的 js代码能够使用进行【同源策略跨域访问】，从而导致隐私信息泄露
 * ● 同源策略跨域访问：对私有目录文件进行访问
 * ● 针对 IM 类产品，泄露的是聊天信息、联系人等等
 * ● 针对浏览器类软件，泄露的是cookie 信息泄露。
 * <p>
 * 解决：不允许使用 file 协议，但同时也限制了 WebView 的功能，使其不能加载本地的 html 文件，
 * <p>
 * 场景一：对于不需要使用 file 协议的应用，禁用 file 协议
 * setAllowFileAccess(false);
 * 场景二：对于需要使用 file 协议的应用，禁止 file 协议加载 JavaScript
 * setAllowFileAccess(true);
 * setJavaScriptEnabled(!url.startsWith("file://"));// 禁止 file 协议的地址使用JavaScript
 * <p>
 * 6.WebView中getSettings类的方法setAllowFileAccessFromFileURLs（），设置是否允许通过 file url 加载的 Js代码读取其他的本地文件
 * 在Android 4.1前默认允许，在Android 4.1后默认禁止
 * 解决：setAllowFileAccessFromFileURLs(false);
 * <p>
 * 7.WebView中getSettings类的方法setAllowUniversalAccessFromFileURLs（）设置是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
 * 在Android 4.1前默认允许，在Android 4.1后默认禁止
 * 解决：setAllowUniversalAccessFromFileURLs(false);
 * <p>
 * 8. setJavaScriptEnabled（）设置是否允许 WebView 使用 JavaScript（默认是不允许）
 * 即使把setAllowFileAccessFromFileURLs（）和setAllowUniversalAccessFromFileURLs（）都设置为 false，
 * 只要设置了setJavaScriptEnabled(true);通过 file URL 加载的 javascript 仍然有方法访问其他的本地文件：符号链接跨源攻击
 * 这一攻击能奏效的原因是：通过 javascript 的延时执行和将当前文件替换成指向其它文件的软链接就可以读取到被符号链接所指的文件
 * 解决：同问题5解决方法
 * <p>
 * 文章参考：[全面 & 详细的Webview使用攻略](https://www.jianshu.com/p/3c94ae673e2a)
 * [WebView 使用漏洞](https://www.jianshu.com/p/3a345d27cd42)
 * [WebView远程代码执行漏洞](https://www.jb51.net/article/139432.htm)
 * [WebView全面解析](https://www.jianshu.com/p/3e0136c9e748)
 *
 *
 */
public class BaseWebViewFragment extends BaseBarFragment  {
    private static final String TAG = "BaseWebViewFragment";
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    WebView webView;

    @Override
    public int setLayout() {
        return R.layout.fragment_base_webview;
    }


    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {

//        String url = "file:///android_asset/test.html";
        String url = "https://xiaoshubin.github.io/";
//        String url = "https://h5.m.taobao.com/?sprefer=sypc00";
//        String url = "file:///android_asset/sonic-demo-index.html";
        //创建一个WebView,并添加到布局中
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(_mActivity.getApplicationContext());
        webView.setLayoutParams(params);
        layoutRoot.addView(webView);

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
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
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
        webView.setDownloadListener((url1, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri = Uri.parse(url1);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        //测试：启用js,js调用Android
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidJs(),"android");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            L.e(TAG, "onPageStarted==" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        //5.0以下重定向页面拦截
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.e(TAG, "5.0以下重定向页面拦截==" + url);
            if (!url.startsWith("http")) {
                goPhoneWeb(_mActivity, url);//如果安装了对应应用，自动打开第三方应用app
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
                L.e(TAG, "5.0以上重定向页面拦截==" + request.getUrl().toString());
                Uri uri = request.getUrl();
                String scheme = request.getUrl().getScheme();
                view.getSettings().setJavaScriptEnabled(!"file".equals(scheme));//如果是file协议，就禁用Javascript
                assert scheme != null;
                if (!scheme.equals("http") && !scheme.equals("https")) {
                    goPhoneWeb(_mActivity, uri.toString());//如果安装了对应应用，自动打开第三方应用app;
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

    private class MyWebChromeClient extends WebChromeClient {
        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            tvTitle.setText(title);

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

    @Override//webView的内部返回
    public boolean onBackPressedSupport() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onBackPressedSupport();
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
            L.e("没有安装此第三方应用！");
        }

    }

}
