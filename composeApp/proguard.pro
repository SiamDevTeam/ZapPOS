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
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn okhttp3.internal.platform.**
-dontwarn okhttp3.internal.platform.android.**


# ===== kotlinx-io =====
-dontwarn kotlinx.io.**
-dontwarn kotlinx.io.bytestring.**


# ===== JNA =====
-keep class com.sun.jna.** { *; }
-dontwarn com.sun.jna.**


# ===== UniFFI / LMDB =====
-keep class rust.nostr.sdk.** { *; }
-keep class lmdb.** { *; }


# ===== Logging =====
-dontwarn org.slf4j.**
