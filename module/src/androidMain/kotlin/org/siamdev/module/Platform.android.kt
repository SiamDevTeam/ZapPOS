/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module

import android.content.Context
import android.os.Build
import java.io.File

private lateinit var appContext: Context

fun initAndroidPlatform(context: Context) {
    appContext = context.applicationContext
}

class AndroidPlatform : Platform {

    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val dataDir: String
        get() {
            val lmdbDir = File(appContext.filesDir, "lmdb")
            if (!lmdbDir.exists()) lmdbDir.mkdirs()
            return lmdbDir.absolutePath
        }

}

actual fun getPlatform(): Platform = AndroidPlatform()
