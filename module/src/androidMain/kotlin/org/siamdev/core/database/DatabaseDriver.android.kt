package org.siamdev.core.database

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual fun createDatabaseDriver(): SQLiteDriver = BundledSQLiteDriver()