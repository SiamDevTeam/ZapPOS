import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(libs.jna)
            implementation("org.rust-nostr:nostr-sdk:0.42.1")
            //implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        val commonMain by getting {
            dependencies {
                // Compose Multiplatform
                implementation(compose.preview)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(libs.material.icons.core)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.navigation.compose)

                // Network
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                // AndroidX Lifecycle
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                // FlowMVI core
                implementation(libs.flowmvi.core)

                // FlowMVI Compose
                implementation(libs.flowmvi.compose)

                // FlowMVI Saved State
                implementation(libs.flowmvi.savedstate)

                // FlowMVI debugger client (optional, dev/debug only)
                implementation(libs.flowmvi.debugger.client)

                // FlowMVI Essenty integration (optional, if using Decompose)
                implementation(libs.flowmvi.essenty)
                implementation(libs.flowmvi.essenty.compose)

                // ktlint for runtime check (optional, dev tools)
                runtimeOnly(libs.ktlint.core)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
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
//        ndk.abiFilters += setOf("arm64-v8a", "armeabi-v7a")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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

