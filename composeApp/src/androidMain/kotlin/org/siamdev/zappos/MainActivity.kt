/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { false }

        enableEdgeToEdge()
        /*        var isActive = true
                lifecycleScope.launch { delay(2000L); isActive = false }
                splashScreen.apply { setKeepOnScreenCondition { false } }*/
        val platform = getPlatform()

        setContent {
            val splashVM: SplashViewModel = viewModel()
            App(platform = platform, splashViewModel = splashVM)
        }

    }
}