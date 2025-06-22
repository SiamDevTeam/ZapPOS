package org.siamdev.zappos

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform