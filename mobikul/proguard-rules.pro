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

#-------------------------------------------------------------------------------------------------------------------------------------------

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-retrofit2.pro

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-keep class okhttp3.** {*;}


#-------------------------------------------------------------------------------------------------------------------------------------------

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-dontwarn okio.**

#-------------------------------------------------------------------------------------------------------------------------------------------

-keepattributes InnerClasses
-keepattributes EnclosingMethod

#-------------------------------------------------------------------------------------------------------------------------------------------

# remove logs
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#}

#-------------------------------------------------------------------------------------------------------------------------------------------

# JSOUP
-keeppackagenames org.jsoup.nodes

#-------------------------------------------------------------------------------------------------------------------------------------------

# For Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#-------------------------------------------------------------------------------------------------------------------------------------------

# For ARCore
-dontwarn com.google.ar.sceneform.animation.AnimationEngine
-dontwarn com.google.ar.sceneform.animation.AnimationLibraryLoader

#-------------------------------------------------------------------------------------------------------------------------------------------

# For Social Login Module
-keepclassmembernames class com.webkul.mobikul.activities.LoginAndSignUpActivity { *; }
-keepclassmembers class com.webkul.mobikul.activities.LoginAndSignUpActivity { *; }
-keep class com.webkul.mobikul.activities.LoginAndSignUpActivity { *; }
-keepclassmembernames class com.webkul.mobikul.mobikulsociallogin.MobikulSocialLoginHelper { *; }
-keepclassmembers class com.webkul.mobikul.mobikulsociallogin.MobikulSocialLoginHelper { *; }
-keep class com.webkul.mobikul.mobikulsociallogin.MobikulSocialLoginHelper { *; }

#-------------------------------------------------------------------------------------------------------------------------------------------

-dontwarn com.google.maps.**
-dontwarn com.itextpdf.text.pdf.**
-dontwarn org.joda.time.**
-dontwarn org.slf4j.**

# Jackson
-keep @com.fasterxml.jackson.annotation.JsonIgnoreProperties class * { *; }
-keep class com.fasterxml.** { *; }
-keep class org.codehaus.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepclassmembers public final enum com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility {
    public static final com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility *;
}
-keep class kotlin.Metadata { *; }

# General
-keepattributes SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,Signature,Exceptions,InnerClasses
