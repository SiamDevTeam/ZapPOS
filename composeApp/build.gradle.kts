@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.serialization)
}

/*
configurations.all {
    resolutionStrategy {
        force("net.java.dev.jna:jna:5.18.0")
        force("net.java.dev.jna:jna-platform:5.18.0")
    }
}*/

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xdata-flow-based-exhaustiveness")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    /*sourceSets {

        val nativeAndJvmMain by creating {
            dependsOn(commonMain.get())
        }
        androidMain.get().dependsOn(nativeAndJvmMain)
        jvmMain.get().dependsOn(nativeAndJvmMain)
        iosMain.get().dependsOn(nativeAndJvmMain)

        nativeAndJvmMain.dependencies {
            implementation(libs.nostr.sdk.kmp)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation("io.ktor:ktor-client-android:3.3.0")
            implementation("androidx.sqlite:sqlite-ktx:2.6.2")
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.nav3)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")
            implementation("io.coil-kt.coil3:coil-compose:3.3.0")
        }

        jsMain.dependencies {
            implementation(npm("@rust-nostr/nostr-sdk", "0.44.0"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("io.ktor:ktor-client-java:3.3.0")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
            implementation("androidx.sqlite:sqlite:2.6.2")
        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:3.3.0")
            implementation("androidx.sqlite:sqlite-ktx:2.6.2")
        }

        wasmWasiMain.dependencies {
            implementation("androidx.sqlite:sqlite-ktx:2.6.2")
        }
    }*/

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)

            implementation("io.ktor:ktor-client-android:3.3.0")
            implementation("androidx.sqlite:sqlite-ktx:2.6.2")

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization)

            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.nav3)
            implementation(libs.jetbrains.lifecycle.viewmodel)


            implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")
            implementation("io.coil-kt.coil3:coil-compose:3.3.0")

            //implementation(libs.nostr.sdk.kmp)


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("io.ktor:ktor-client-java:3.3.0")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
            implementation("androidx.sqlite:sqlite:2.6.2")

        }

        jsMain.dependencies { implementation("org.jetbrains.kotlinx:kotlinx-browser:0.5.0") }

        iosMain.dependencies {
            //implementation(libs.nostr.sdk.kmp)
            implementation("io.ktor:ktor-client-darwin:3.3.0")
            implementation("androidx.sqlite:sqlite-ktx:2.6.2")
        }
    }
}

android {
    namespace = "org.siamdev.zappos"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.siamdev.zappos"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        /*jniLibs {
            pickFirsts += setOf("lib/arm64-v8a/libxxxx.so",
                "lib/armeabi-v7a/libxxxx.so",
                "lib/x86_64/libxxxx.so",
                "lib/x86/libxxxx.so")
        }*/
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.siamdev.zappos.MainKt"

        // https://github.com/JetBrains/compose-multiplatform/issues/4883
        buildTypes.release.proguard {
            version.set("7.7.0")
            configurationFiles.from("proguard.pro")
        }

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg
                , TargetFormat.Msi
                , TargetFormat.Deb
                , TargetFormat.Rpm
            )
            packageName = "ZapPOS"
            packageVersion = "1.0.0"

            val icProduct = "src/jvmMain/resources/ico_sizes/linux/ic_product_256.png"
            linux {
                iconFile.set(project.file(icProduct))
            }

            windows {
                iconFile.set(project.file(icProduct))
            }

        }
    }
}