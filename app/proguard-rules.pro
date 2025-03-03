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

# Keep your application class
-keep class com.zero.flowfund.** { *; }

# Keep notification service
-keep class com.zero.flowfund.service.PaymentNotificationListener { *; }

# Keep API models
-keep class com.zero.flowfund.data.** { *; }

# OkHttp rules
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Retrofit rules
-keepattributes Signature
-keepattributes Exceptions

# Keep Compose rules
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# Keep Coroutines
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep DataStore
-keepclassmembers class androidx.datastore.preferences.** { *; }

# Keep Android runtime
-keep class android.app.** { *; }
-keep class android.content.** { *; }
-keep class android.service.** { *; }

# Keep Notification related classes
-keep class android.service.notification.** { *; }
-keep class androidx.core.app.NotificationCompat { *; }

# Keep JSON related classes
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep Kotlin Metadata
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Keep Material3 components
-keep class androidx.compose.material3.** { *; }

# Keep PaymentNotification and related classes
-keep class com.zero.flowfund.data.PaymentNotification { *; }
-keep class com.zero.flowfund.data.AlertLog { *; }
-keep class com.zero.flowfund.data.LogManager { *; }
-keep class com.zero.flowfund.data.AppPreferences { *; }

# Keep API related classes
-keep class com.zero.flowfund.api.AlertsApi { *; }
-keep class org.json.JSONObject { *; }
-keep class org.json.JSONArray { *; }

# Keep OkHttp and Okio
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }

# Keep Compose Navigation
-keep class androidx.navigation.** { *; }

# Keep Android Lifecycle
-keep class androidx.lifecycle.** { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.ViewModelProvider { *; }

# Keep DataStore and Preferences
-keep class androidx.datastore.** { *; }
-keep class kotlinx.coroutines.flow.** { *; }

# Keep Material3 and Compose
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }

# Keep service and notification related
-keep class android.service.notification.NotificationListenerService { *; }
-keep class android.app.NotificationChannel { *; }
-keep class android.app.NotificationManager { *; }

# Keep Kotlin Coroutines
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-keep class kotlinx.coroutines.android.** { *; }

# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep R8 full mode
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepattributes EnclosingMethod

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}