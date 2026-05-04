/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import android.os.Build

actual fun getPlatform(): Platform = object : Platform {
    override val type = PlatformType.MOBILE
    override val name = "Android"
    override val info = "Android ${Build.VERSION.SDK_INT}"
}
