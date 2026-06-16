/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import androidx.compose.runtime.Composable
import org.siamdev.zappos.theme.ThemeMode
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.theme.ZapposTheme
import org.siamdev.zappos.theme.colorFromHex
import org.siamdev.zappos.navigation.NavigationRoot
import org.siamdev.zappos.navigation.Route
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

private const val DEFAULT_FONT_SIZE = 14f

@Composable
fun App(platform: Platform, splashViewModel: SplashViewModel) {
    val start = when (platform.type) {
        PlatformType.DESKTOP -> Route.Login
        PlatformType.MOBILE -> Route.Splash
        PlatformType.WEB -> Route.Login
    }

    ProvideViewModels {
        val setting = LocalSettingVM.current

        val themeMode = when (setting.activeTheme?.mode) {
            "DARK" -> ThemeMode.DARK
            "LIGHT" -> ThemeMode.LIGHT
            "SYSTEM" -> ThemeMode.SYSTEM
            else -> ThemeMode.SYSTEM
        }
        val fontScale =
            (setting.activeFont?.size?.toFloat() ?: DEFAULT_FONT_SIZE) / DEFAULT_FONT_SIZE
        val accentColor = setting.accentColorHex?.let { colorFromHex(it) } ?: YellowPrimary

        ZapposTheme(themeMode = themeMode, accentColor = accentColor, fontScale = fontScale) {
            NavigationRoot(
                platform = platform,
                startDestination = start,
                splashViewModel = splashViewModel
            )
        }
    }
}