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

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimary,
    onPrimaryContainer = LightOnPrimary,

    background = LightBackground,
    surface = LightBackground
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimary,
    onPrimaryContainer = DarkOnPrimary,

    background = DarkBackground,
    surface = DarkBackground
)


/*val colorsTheme = if (isDark) {
    darkColorScheme(
        primary = theme.primary.toColor(),
        onPrimary = theme.onPrimary.toColor(),
        primaryContainer = theme.primaryContainer.toColor(),
        onPrimaryContainer = theme.onPrimaryContainer.toColor(),
        background = theme.background.toColor(),
        surface = theme.surface.toColor()
    )
} else {
    lightColorScheme(
        primary = theme.primary.toColor(),
        onPrimary = theme.onPrimary.toColor(),
        primaryContainer = theme.primaryContainer.toColor(),
        onPrimaryContainer = theme.onPrimaryContainer.toColor(),
        background = theme.background.toColor(),
        surface = theme.surface.toColor()
    )
}*/

@Composable
fun ZapposTheme(
    themeMode: ThemeMode,
    fontScale: Float = 1f,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colors = if (isDark) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = appTypography(fontScale),
        content = content
    )
}
