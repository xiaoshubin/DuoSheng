package com.qiqia.duosheng.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.qiqia.duosheng.R;

import butterknife.BindView;

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
 *
 * 文章参考：[全面 & 详细的Webview使用攻略](https://www.jianshu.com/p/3c94ae673e2a)
 *          [WebView 使用漏洞](https://www.jianshu.com/p/3a345d27cd42)
 */
public class BaseWebViewFragment extends BaseBindBarFragment {
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public int setLayout() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void onBindView(View view, ViewGroup container, Bundle savedInstanceState) {
        String url = "https://h5.m.taobao.com/?sprefer=sypc00";
        initSet(url);
    }

    private void initSet(String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setJavaScriptEnabled(!url.startsWith("file://"));

    }

    @Override//webView的返回
    public boolean onBackPressedSupport() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onBackPressedSupport();
    }
}
