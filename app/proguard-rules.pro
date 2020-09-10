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

-optimizationpasses 5
# 是否混淆第三方jar
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
# 混淆时是否做预校验
-dontpreverify
# 混淆时是否记录日志
-verbose
-allowaccessmodification
-repackageclasses
-dontusemixedcaseclassnames
-dontoptimize
-dontshrink
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings

# AndroidX
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

# 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

# 保留R下面的资源
-keep class **.R$* {*;}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


#手动启用support keep注解
#http://tools.android.com/tech-docs/support-annotations
-dontskipnonpubliclibraryclassmembers
-printconfiguration
-keep,allowobfuscation @interface androidx.annotation.Keep

-keep @androidx.annotation.Keep class *
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# 不混淆 WebView 的 JS 接口
-keepattributes *JavascriptInterface*
# 不混淆 WebView 的类的所有的内部类
#-keepclassmembers  class com.veidy.activity.WebViewActivity$*{
#    *;
#}
# 不混淆 WebChromeClient 中的 openFileChooser 方法
-keepclassmembers class * extends android.webkit.WebChromeClient{
   public void openFileChooser(...);
}
# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class com.song.sakura.ui.web.WebViewActivity$* {
   public *;
}
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
    public void onEventMainThread(**);
}
# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# AOP
-adaptclassstrings
-keepattributes InnerClasses, EnclosingMethod, Signature, *Annotation*

-keepnames @org.aspectj.lang.annotation.Aspect class * {
    ajc* <methods>;
}

# rx
-dontwarn rx.**
-keep class rx.**{*;}
-keep public class * extends rx.**
-dontwarn io.reactivex
-keep class io.reactivex.**{*;}
-keep public class * extends io.reactivex.**

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# Immersionbar
-keep class com.gyf.immersionbar.** {*;}
-dontwarn com.gyf.immersionbar.**

# Simplifyspan
-keep class cn.iwgang.simplifyspan.** {*;}
-dontwarn cn.iwgang.simplifyspan.**

# https://github.com/getActivity/XXPermissions
-keep class com.hjq.permissions.** {*;}

# https://github.com/getActivity/ToastUtils
-keep class com.hjq.toast.** {*;}

# slidinguppanel
-keep class com.sothree.slidinguppanel.** {*;}
-dontwarn com.sothree.slidinguppanel.**

# smartrefresh
-keep class com.scwang.smart.refresh.** {*;}
-dontwarn com.scwang.smartrefresh.**

# EventBus3
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
#-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

# OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {*;}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.viewholder.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.viewholder.BaseViewHolder {
     <init>(...);
}

# 不混淆bean类
-keep class com.song.sakura.entity.** {*;}
-dontwarn com.song.sakura.entity.**

# 不混淆util类
-keep class com.song.sakura.util.** {*;}
-dontwarn com.song.sakura.util.**

# 不混淆widget
-keep class com.song.sakura.widget.** {*;}
-dontwarn com.song.sakura.widget.**







