package org.siamdev.zappos

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val splashVM = SplashViewModel()

    MainScope().launch {
        ComposeViewport {
            App(
                isDesktop = false,
                splashViewModel = splashVM
            )
        }
    }
}