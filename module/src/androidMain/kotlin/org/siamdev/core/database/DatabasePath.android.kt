package org.siamdev.core.database

actual fun getDatabasePath(name: String): String =
    AppContext.value.getDatabasePath(name).absolutePath