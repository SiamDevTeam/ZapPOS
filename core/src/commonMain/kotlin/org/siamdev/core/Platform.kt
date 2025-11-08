package org.siamdev.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform