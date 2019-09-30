# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
# immer
-keep class com.gyf.immersionbar.* {*;}
-dontwarn com.gyf.immersionbar.**

# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }
 #baseRecyclerViewAdapterHelper
 -keep class com.chad.library.adapter.** {
 *;
 }
 -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
 -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
 -keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
      <init>(...);
 }
 #LitePal
 -keep class org.litepal.** {
     *;
 }
 -keep class * extends org.litepal.crud.DataSupport {
     *;
 }
 -keep class * extends org.litepal.crud.LitePalSupport {
     *;
 }
 #微信开放平台
 -keep class com.tencent.mm.opensdk.** {
     *;
 }
 -keep class com.tencent.wxop.** {
     *;
 }
 -keep class com.tencent.mm.sdk.** {
     *;
 }
 #Apollo
 -dontwarn com.esotericsoftware.kryo.**
 -dontwarn org.objenesis.instantiator.**
 -dontwarn org.codehaus.**
 -dontwarn java.nio.**
 -dontwarn java.lang.invoke.**
 -keep class com.lsxiao.apollo.generate.** { *; }
 # XPopup
 -dontwarn com.lxj.xpopup.widget.**
 -keep class com.lxj.xpopup.widget.**{*;}
 #极光推送
 -dontoptimize
 -dontpreverify
 -dontwarn cn.jpush.**
 -keep class cn.jpush.** { *; }
 -keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
 -dontwarn cn.jiguang.**
 -keep class cn.jiguang.** { *; }
 #==================gson && protobuf==========================
 -dontwarn com.google.**
 -keep class com.google.gson.** {*;}
 -keep class com.google.protobuf.** {*;}
 # 阿里百川
 -keepattributes Signature
 -keep class sun.misc.Unsafe { *; }
 -keep class com.taobao.** {*;}
 -keep class com.alibaba.** {*;}
 -keep class com.alipay.** {*;}
 -dontwarn com.taobao.**
 -dontwarn com.alibaba.**
 -dontwarn com.alipay.**
 -keep class com.ut.** {*;}
 -dontwarn com.ut.**
 -keep class com.ta.** {*;}
 -dontwarn com.ta.**
 -keep class org.json.** {*;}
 -keep class com.ali.auth.**  {*;}
 # zxing
 -dontshrink
 -dontobfuscate
 -keepattributes *Annotation*
 -keep public class com.google.vending.licensing.ILicensingService
 -keep public class com.android.vending.licensing.ILicensingService
 -keepclasseswithmembernames class * {
     native <methods>;
 }
 -keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
 }
 -keepclassmembers class **.R$* {
     public static <fields>;
 }
 -dontwarn android.support.**
 # jsoup
 -keeppackagenames org.jsoup.nodes
 # WebView
 -keepattributes *Annotation*
 -keepattributes *JavascriptInterface*
 -keep public class org.mq.study.webview.DemoJavaScriptInterface{
          public <methods>;
       }
 -keep public class org.mq.study.webview.webview.DemoJavaScriptInterface$InnerClass{
          public <methods>;
       }


