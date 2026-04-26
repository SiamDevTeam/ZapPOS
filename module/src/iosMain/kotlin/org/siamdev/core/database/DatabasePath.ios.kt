package org.siamdev.core.database

import platform.Foundation.NSHomeDirectory

actual fun getDatabasePath(name: String): String =
    "${NSHomeDirectory()}/Documents/$name"