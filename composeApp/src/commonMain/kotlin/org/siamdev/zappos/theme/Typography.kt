/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.times

fun appTypography(scale: Float = 1f): Typography {
    val base = Typography()
    fun TextStyle.scaled() = copy(fontSize = scale * fontSize, lineHeight = scale * lineHeight)
    return Typography(
        displayLarge    = base.displayLarge.scaled(),
        displayMedium   = base.displayMedium.scaled(),
        displaySmall    = base.displaySmall.scaled(),
        headlineLarge   = base.headlineLarge.scaled(),
        headlineMedium  = base.headlineMedium.scaled(),
        headlineSmall   = base.headlineSmall.scaled(),
        titleLarge      = base.titleLarge.scaled(),
        titleMedium     = base.titleMedium.scaled(),
        titleSmall      = base.titleSmall.scaled(),
        bodyLarge       = base.bodyLarge.scaled(),
        bodyMedium      = base.bodyMedium.scaled(),
        bodySmall       = base.bodySmall.scaled(),
        labelLarge      = base.labelLarge.scaled(),
        labelMedium     = base.labelMedium.scaled(),
        labelSmall      = base.labelSmall.scaled(),
    )
}