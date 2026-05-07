import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.sqldelight)
}

// https://sqldelight.github.io/sqldelight/latest/multiplatform_sqlite/migrations/
sqldelight {
    databases {
        register("ZapPOSBiz") {
            packageName.set("org.siamdev.module.db.biz")
            srcDirs.setFrom("src/commonMain/sqldelight/biz")
            generateAsync = true
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/biz"))
        }
        register("ZapPOSSys") {
            packageName.set("org.siamdev.module.db.sys")
            srcDirs.setFrom("src/commonMain/sqldelight/sys")
            generateAsync = true
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/sys"))
            verifyMigrations = true
        }
    }
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    val xcfName = "coreKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    androidLibrary {
        namespace = "org.siamdev.module"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    js {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.sqldelight.runtime)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.annotation.jvm)
                implementation(libs.sqldelight.android.driver)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        nativeMain {
            dependencies {
                implementation(libs.sqldelight.native.driver)
            }
        }

        jsMain {
            dependencies {
                implementation(libs.sqldelight.web.worker.driver)
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
                implementation(npm("sql.js", "1.8.0"))
            }
        }
    }
}