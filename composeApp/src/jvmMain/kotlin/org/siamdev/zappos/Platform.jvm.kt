package org.siamdev.zappos

actual fun getPlatform(): Platform = object : Platform {
    override val type = PlatformType.DESKTOP
    override val name = "Desktop"
    override val info = System.getProperty("os.name") ?: "Unknown"
}