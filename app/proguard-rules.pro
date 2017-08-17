# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yonikal/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-dontskipnonpubliclibraryclasses
-dontusemixedcaseclassnames
-verbose
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends com.actionbarsherlock.app.SherlockListFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.view.View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public void set*(...);
}
-keepclasseswithmembernames class *{
    public <init>(android.content.Context,android.util.AttributeSet);
}
-keepclasseswithmembernames class *{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable$Creator *;
}
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-dontwarn android.support.**
-dontwarn com.google.ads.**


-dontshrink
-dontoptimize
-dontwarn  org.apache.**
-dontwarn com.**
-dontwarn butterknife.internal.**
-dontwarn javax.**
-dontwarn retrofit.**
-dontwarn okio.**
-keep class com.** {*;}
-keep class org.** {*;}
-keep interface org.** {*;}
-keep interface com.** {*;}
-keep class javax.** {*;}
-keep class butterknife.*{*;}
-keep class retrofit.**{*;}
-keep class okio.**
-keep class **$$ViewInjector {*;}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keep class com.google.gson.*{*;}
-keep class com.google.inject.*{*;}
-keep class org.apach.http.*{*;}
-keep class org.james.mime4j.*{*;}
-keep class javax.inject*{*;}
-keepattributes Signature
-keep class sun.misc.Unsafe {*;}