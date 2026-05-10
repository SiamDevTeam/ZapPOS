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

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        val withModuleMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(project(":module"))
            }
        }

        val webMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(withModuleMain)
            dependencies {
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core.splashscreen)
                implementation(libs.ktor.client.android)
            }
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.nav3)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.coil.network.ktor3)
            implementation(libs.coil.compose)

            implementation("io.github.vinceglb:filekit-core:0.14.1")
            implementation("io.github.vinceglb:filekit-dialogs:0.14.1")
            implementation("io.github.vinceglb:filekit-dialogs-compose:0.14.1")
            implementation("io.github.vinceglb:filekit-coil:0.14.1")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain {
            dependsOn(withModuleMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client.java)
                implementation(libs.coil.network.okhttp)
            }
        }

        jsMain {
            dependsOn(withModuleMain)
            dependsOn(webMain)
            dependencies {
                implementation(libs.kotlinx.browser)
            }
        }

        wasmJsMain {
            dependsOn(webMain)
            dependencies {
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }

        iosArm64Main { dependsOn(withModuleMain) }
        iosSimulatorArm64Main { dependsOn(withModuleMain) }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
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
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
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

            linux {
                iconFile.set(project.file("src/jvmMain/resources/ico_sizes/linux/ic_product_256.png"))
            }
            macOS {
                iconFile.set(project.file("src/jvmMain/resources/ico_sizes/macos/ic_zappos512x512.png"))
            }
            windows {
                iconFile.set(project.file("src/jvmMain/resources/ico_sizes/windows/ic_zappos_256.ico"))
            }

        }
    }
}