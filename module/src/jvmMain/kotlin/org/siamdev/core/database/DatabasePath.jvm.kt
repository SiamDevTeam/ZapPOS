package org.siamdev.core.database

import java.io.File

actual fun getDatabasePath(name: String): String {
    val dir = File(System.getProperty("user.home"), ".zappos/databases")
    dir.mkdirs()
    return File(dir, name).absolutePath
}