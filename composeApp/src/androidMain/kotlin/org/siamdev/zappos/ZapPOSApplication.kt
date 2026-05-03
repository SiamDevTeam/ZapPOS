package org.siamdev.zappos

import android.app.Application
import kotlinx.coroutines.runBlocking
import org.siamdev.module.db.AppContext
import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.appDatabase

class ZapPOSApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        AppContext.value = this
        database = runBlocking { appDatabase() }
        database.registerDependencies()
    }
}
