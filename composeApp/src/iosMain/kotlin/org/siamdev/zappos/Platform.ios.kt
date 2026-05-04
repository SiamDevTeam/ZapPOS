/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import platform.UIKit.UIDevice

actual fun getPlatform(): Platform = object : Platform {
    override val type = PlatformType.MOBILE
    override val name = "iOS"
    override val info = UIDevice.currentDevice.systemName() +
            " " + UIDevice.currentDevice.systemVersion
}