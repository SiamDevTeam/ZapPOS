package org.zappos.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform