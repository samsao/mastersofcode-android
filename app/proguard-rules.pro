# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/opt/android-sdk/tools/proguard/proguard-android.txt
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

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class sun.misc.Unsafe { *; }
-keepclassmembers class * {
  public <init>(android.content.Context);
}

# For Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# For retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-dontwarn com.squareup.**
-dontwarn rx.**
-dontwarn retrofit.**
-dontwarn okio.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# For RxJava
-dontwarn rx.internal.util.**

# For Jackson
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class org.codehaus.jackson.** { *; }
-dontwarn javax.xml.**
-dontwarn javax.xml.stream.events.**
-dontwarn com.fasterxml.jackson.databind.**
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *;
}
# If the model classes are obfuscated, reflection does not work
-keep public class com.samsao.dutchauction.data.api.entity.** {
  public void set*(***);
  public *** get*();
  public boolean is*();
}


# Don't mess with classes with native methods
-keepclasseswithmembers class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}

# Parcel library
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
  public <init>(***);
}
-keep class org.parceler.Parceler$$Parcels

# Realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-dontwarn javax.**
-dontwarn io.realm.**

# saripaar validator
-keep class com.mobsandgeeks.saripaar.** {*;}
-keep @com.mobsandgeeks.saripaar.annotation.ValidateUsing class * {*;}

# stetho
-dontwarn com.facebook.stetho.**
-keep class com.facebook.stetho.** {*;}

# ShareActionProvider
-keep class android.support.v7.widget.ShareActionProvider { *; }
