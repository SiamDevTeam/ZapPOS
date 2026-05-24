/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import android.app.Application
import kotlinx.coroutines.runBlocking
import org.siamdev.module.db.AppContext
import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.appDatabase
import org.siamdev.zappos.cache.AndroidThumbnailCache
import org.siamdev.zappos.cache.thumbnailCache

class ZapPOSApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        AppContext.value = this
        database = runBlocking { appDatabase() }
        database.registerDependencies()
        thumbnailCache = AndroidThumbnailCache()
    }
}
