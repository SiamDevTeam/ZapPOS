
# Android-only classes (Desktop JVM ไม่มี)
-dontwarn android.**
-dontwarn androidx.**
-dontwarn dalvik.**


# OkHttp optional TLS / crypto
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**


# OkHttp / Ktor internal platform adapters
-dontwarn okhttp3.internal.platform.**
-dontwarn okhttp3.internal.platform.android.**


# kotlinx-io (descriptor missing warning)
-dontwarn kotlinx.io.**
-dontwarn kotlinx.io.bytestring.**


# JNA
-keep class com.sun.jna.** { *; }
-dontwarn com.sun.jna.**


# SLF4J
-dontwarn org.slf4j.**
