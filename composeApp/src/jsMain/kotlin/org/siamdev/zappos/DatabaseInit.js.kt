package org.siamdev.zappos

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.siamdev.module.db.appDatabase

actual fun initDatabase() {
    MainScope().launch {
        database = appDatabase()
        checkDatabase(database)
    }
}