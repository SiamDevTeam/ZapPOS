/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module

interface Platform {
    val name: String

    val dataDir: String
}

expect fun getPlatform(): Platform

fun getDatabasePath(): String = getPlatform().dataDir
