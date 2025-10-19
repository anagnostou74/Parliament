# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Dev\SDK/tools/proguard/proguard-android.txt
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
# --- Firebase model keeping (Java) ---
-keepclassmembers class gr.mobap.mps.MpsData {
    public <init>();
    public *;
}
-keep class gr.mobap.mps.MpsData { *; }

-keepclassmembers class gr.mobap.calendar.CalendarData {
    public <init>();
    public *;
}
-keep class gr.mobap.calendar.CalendarData { *; }

# Αν έχεις και άλλα models για Firebase:
-keep class gr.mobap.**.**Data { *; }
-keepclassmembers class gr.mobap.**.**Data { public <init>(); public *; }

# Διατήρησε annotations που χρησιμοποιεί το Firebase
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
# Αν χρησιμοποιείς @PropertyName/@IgnoreExtraProperties/@Keep
-keep class com.google.firebase.** { *; }
-keepclassmembers class * {
    @com.google.firebase.database.PropertyName *;
    @com.google.firebase.database.IgnoreExtraProperties *;
    @androidx.annotation.Keep *;
}

