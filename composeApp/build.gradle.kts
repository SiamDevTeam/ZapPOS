import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.serialization)
}

configurations.all {
    resolutionStrategy {
        force("net.java.dev.jna:jna:5.18.0")
        force("net.java.dev.jna:jna-platform:5.18.0")
    }
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xdata-flow-based-exhaustiveness")
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

    jvm()

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.koin.android)
            implementation("io.ktor:ktor-client-android:3.3.0")

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
            implementation(projects.core)
            implementation(libs.koin.compose)

            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.nav3)
            implementation(libs.jetbrains.lifecycle.viewmodel)


            // This lib include JNA
            implementation(libs.nostr.sdk.kmp)

            // This lib include JNA
            implementation("io.github.crowded-libs:kotlin-lmdb:0.3.6") {
                exclude(group = "net.java.dev.jna", module = "jna")
            }

            implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")
            implementation("io.coil-kt.coil3:coil-compose:3.3.0")


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("io.ktor:ktor-client-java:3.3.0")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")

        }

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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        /*jniLibs {
            pickFirsts += setOf("lib/arm64-v8a/libjnidispatch.so",
                "lib/armeabi-v7a/libjnidispatch.so",
                "lib/x86_64/libjnidispatch.so",
                "lib/x86/libjnidispatch.so")
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
            targetFormats(
                TargetFormat.Dmg
                , TargetFormat.Msi
                , TargetFormat.Deb
                , TargetFormat.Rpm
            )
            packageName = "ZapPOS"
            packageVersion = "1.0.0"

            linux {
                iconFile.set(project.file("src/jvmMain/resources/ico_sizes/linux/ic_zappos_256.png"))
            }
        }
    }
}