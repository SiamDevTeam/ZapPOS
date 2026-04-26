package org.siamdev.zappos

import android.app.Application
import org.siamdev.core.database.AppContext
import org.siamdev.core.database.DatabaseInitializer

class ZapPOSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.value = this
        DatabaseInitializer.initialize()
    }
}
