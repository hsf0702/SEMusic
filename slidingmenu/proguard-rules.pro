# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lihoujun/Downloads/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose


-keepattributes Signature
-keepattributes InnerClass

-keep class android.support.v4.** { *; }

# rest api
-keep class * extends com.michael.corelib.internet.core.RequestBase { *; }
-keep class * extends com.michael.corelib.internet.core.ResponseBase { *; }

#SingleInstanceBase
-keep public class * extends com.michael.corelib.coreutils.SingleInstanceBase { *; }

-keep class **$Properties

# eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}

# umeng
-keep class com.umeng.** { *; }
-dontwarn com.umeng.**

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

# weixin sdk
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

# weibo
-keep class com.sina.** { *; }

# qq
-keep class com.tencent.** { *; }



