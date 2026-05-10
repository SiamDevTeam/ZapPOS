/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

private val DarkColors = darkColorScheme(
    primary              = DarkPrimary,
    onPrimary            = DarkOnPrimary,
    primaryContainer     = DarkPrimaryContainer,
    onPrimaryContainer   = DarkOnPrimaryContainer,

    background           = DarkBackground,
    onBackground         = DarkOnBackground,
    surface              = DarkSurface,
    onSurface            = DarkOnSurface,
    surfaceVariant       = DarkSurfaceVariant,
    onSurfaceVariant     = DarkOnSurfaceVariant,

    outline              = DarkOutline,
    outlineVariant       = DarkOutlineVariant,
)

private val LightColors = lightColorScheme(
    primary              = LightPrimary,
    onPrimary            = LightOnPrimary,
    primaryContainer     = LightPrimaryContainer,
    onPrimaryContainer   = LightOnPrimaryContainer,

    background           = LightBackground,
    onBackground         = LightOnBackground,
    surface              = LightSurface,
    onSurface            = LightOnSurface,
    surfaceVariant       = LightSurfaceVariant,
    onSurfaceVariant     = LightOnSurfaceVariant,

    outline              = LightOutline,
    outlineVariant       = LightOutlineVariant,
)

@Composable
fun ZapposTheme(
    themeMode: ThemeMode,
    accentColor: Color = YellowPrimary,
    fontScale: Float = 1f,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT  -> false
        ThemeMode.DARK   -> true
    }

    val onAccent = if (accentColor.luminance() > 0.35f) Color(0xFF1A1A1A) else Color.White

    // Blend accent with the target surface to produce tonal containers
    val darkContainer = Color(
        red   = accentColor.red   * 0.28f,
        green = accentColor.green * 0.28f,
        blue  = accentColor.blue  * 0.28f,
        alpha = 1f
    )
    val lightContainer = Color(
        red   = accentColor.red   * 0.18f + 0.82f,
        green = accentColor.green * 0.18f + 0.82f,
        blue  = accentColor.blue  * 0.18f + 0.82f,
        alpha = 1f
    )
    val onLightContainer = Color(
        red   = (accentColor.red   * 0.5f).coerceIn(0f, 1f),
        green = (accentColor.green * 0.5f).coerceIn(0f, 1f),
        blue  = (accentColor.blue  * 0.5f).coerceIn(0f, 1f),
        alpha = 1f
    )

    val colorScheme = if (isDark) {
        DarkColors.copy(
            primary            = accentColor,
            onPrimary          = onAccent,
            primaryContainer   = darkContainer,
            onPrimaryContainer = accentColor
        )
    } else {
        LightColors.copy(
            primary            = accentColor,
            onPrimary          = onAccent,
            primaryContainer   = lightContainer,
            onPrimaryContainer = onLightContainer
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = appTypography(fontScale),
        content     = content
    )
}