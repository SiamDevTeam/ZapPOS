package org.siamdev.zappos

import org.siamdev.core.getPlatform


class PlatformInfo {
    private val platform = getPlatform()

    val name get() = platform.name

    val message get() = "Hello, $name!"
}
