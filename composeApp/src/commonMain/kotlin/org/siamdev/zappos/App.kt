/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.siamdev.zappos.theme.ThemeMode
import org.siamdev.zappos.theme.ZapposTheme
import org.siamdev.zappos.navigation.NavigationRoot
import org.siamdev.zappos.navigation.Route
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

private const val DEFAULT_FONT_SIZE = 14f

@Composable
fun App(platform: Platform, splashViewModel: SplashViewModel) {
    val settingVM = viewModelOf { SettingViewModel() }
    val activeTheme by settingVM.activeTheme.collectAsState()
    val activeFont by settingVM.activeFont.collectAsState()

    val themeMode = when (activeTheme?.mode) {
        "DARK" -> ThemeMode.DARK
        "LIGHT" -> ThemeMode.LIGHT
        "SYSTEM" -> ThemeMode.SYSTEM
        else -> ThemeMode.SYSTEM
    }
    val fontScale = (activeFont?.size?.toFloat() ?: DEFAULT_FONT_SIZE) / DEFAULT_FONT_SIZE

    val start = when (platform.type) {
        PlatformType.DESKTOP -> Route.Login
        PlatformType.MOBILE -> Route.Splash
        PlatformType.WEB -> Route.Login
    }

    ZapposTheme(themeMode = themeMode, fontScale = fontScale) {
        VMContainer(settingVM = settingVM) {
            NavigationRoot(
                platform = platform,
                startDestination = start,
                splashViewModel = splashViewModel
            )
        }
    }
}