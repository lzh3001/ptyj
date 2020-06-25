# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

-keep class android.**{
<methods>;
<fields>;
}

-dontwarn com.iccgame.**
-keep class com.iccgame.sdk.ICC_Callback{
<methods>;
<fields>;
}

-keep class com.iccgame.sdk.ICC_Activity{
<methods>;
<fields>;
}

-keep class com.iccgame.sdk.ICC_HTML5Interface{
<methods>;
<fields>;
}

-keep class com.iccgame.sdk.JniCallback{
<methods>;
<fields>;
}

-keep class com.iccgame.sdk.JniHelper{
<methods>;
<fields>;
}

-keep class com.iccgame.sdk.ICC_SDK{
<methods>;
<fields>;
}

#忽略警告
-dontwarn com.alipay.**
-keep class com.alipay.**{
<methods>;
<fields>;
}