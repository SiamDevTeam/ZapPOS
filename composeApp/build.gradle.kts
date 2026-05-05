@file:OptIn(ExperimentalWasmDsl::class)

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
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
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
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

    sourceSets {

        // Intermediate source set shared by all platforms that support the database module.
        // wasmJs is excluded because the :module does not target wasmJs.
        val withModuleMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(project(":module"))
            }
        }

        // Intermediate source set shared by JS and WasmJS (web targets).
        // Holds the main() entry point and web resources (index.html, styles.css, assets).
        val webMain by creating {
            dependsOn(commonMain.get())
        }

        val androidMain by getting {
            dependsOn(withModuleMain)
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core.splashscreen)
                implementation("io.ktor:ktor-client-android:3.3.0")
            }
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

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        val jvmMain by getting {
            dependsOn(withModuleMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation("io.ktor:ktor-client-java:3.3.0")
                implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
            }
        }

        val jsMain by getting {
            dependsOn(withModuleMain)
            dependsOn(webMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-browser:0.5.0")
            }
        }

        val wasmJsMain by getting {
            dependsOn(webMain)
        }

        // iosMain is lazily created by the default hierarchy, so "by getting" fails.
        // Apply withModuleMain to each iOS target source set directly instead.
        val iosArm64Main by getting { dependsOn(withModuleMain) }
        val iosSimulatorArm64Main by getting { dependsOn(withModuleMain) }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:3.3.0")
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
    // project.setProperty("archivesBaseName", "ZapPOS-v1.0")
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName = "ZapPOS-${variant.buildType.name}-v${variant.versionName}.apk"
            }
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
            modules("java.sql", "java.naming")
            includeAllModules = true

            targetFormats(
                TargetFormat.Dmg
                , TargetFormat.Msi
                , TargetFormat.Exe
                , TargetFormat.Deb
                , TargetFormat.Rpm
                , TargetFormat.AppImage
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