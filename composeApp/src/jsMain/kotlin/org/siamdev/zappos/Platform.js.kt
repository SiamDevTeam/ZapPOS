package org.siamdev.zappos

import kotlinx.browser.window

actual fun getPlatform(): Platform = object : Platform {
    override val type = PlatformType.WEB
    override val name = "Web"
    override val info = window.navigator.userAgent
}