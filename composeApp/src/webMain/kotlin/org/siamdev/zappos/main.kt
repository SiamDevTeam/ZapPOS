/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initDatabase()
    val splashVM = SplashViewModel()
    val platform = getPlatform()

    MainScope().launch {
        ComposeViewport {
            App(platform = platform, splashViewModel = splashVM)
        }
    }
}