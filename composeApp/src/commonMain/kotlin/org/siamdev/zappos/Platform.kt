package org.siamdev.zappos

enum class PlatformType {
    MOBILE,
    DESKTOP,
    WEB
}

interface Platform {
    val type: PlatformType
    val name: String
    val info: String
}

expect fun getPlatform(): Platform