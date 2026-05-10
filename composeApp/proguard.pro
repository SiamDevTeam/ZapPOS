# ===== Global attributes =====
-keepattributes Signature,InnerClasses,EnclosingMethod


# ===== Navigation3 =====
-keep class androidx.navigation3.** { *; }
-keep class androidx.navigationevent.** { *; }


# ===== Compose Desktop =====
-keep class androidx.compose.ui.window.** { *; }
-keepclassmembers class * {
    *** getDp(...);
}


# ===== Android (Desktop ignore) =====
-dontwarn android.**
-dontwarn androidx.**
-dontwarn dalvik.**


# ===== Coil 3 =====
-keep class coil3.** { *; }
-keep class coil3.network.okhttp.** { *; }
-keep class coil3.network.ktor3.** { *; }


# ===== Okio =====
-keep class okio.** { *; }
-keepclassmembers class okio.** { *; }


# ===== OkHttp / Ktor =====
-keep class okhttp3.** { *; }
-keep class io.ktor.** { *; }
-dontwarn okhttp3.**
-dontwarn io.ktor.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn okhttp3.internal.platform.**
-dontwarn okhttp3.internal.platform.android.**

# ===== SQLDelight / SQLite JDBC =====
-keep class app.cash.sqldelight.** { *; }
-keep class org.sqlite.** { *; }
-keep class java.sql.** { *; }
-dontwarn java.sql.**


# ===== kotlinx-io / coroutines / datetime =====
-dontwarn kotlinx.io.**
-dontwarn kotlinx.io.bytestring.**
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.datetime.** { *; }
-dontwarn kotlinx.datetime.**
