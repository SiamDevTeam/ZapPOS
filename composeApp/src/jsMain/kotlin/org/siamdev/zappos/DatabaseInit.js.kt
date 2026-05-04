/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.siamdev.module.db.appDatabase

actual fun initDatabase() {
    val handler = CoroutineExceptionHandler { _, e ->
        console.error("[ZapPOS] Database init failed:", e.message)
    }
    MainScope().launch(handler) {
        database = appDatabase()
        database.registerDependencies()
        checkDatabase(database)
    }
}