/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
        // Prevent the window's default white background from flashing through
        // during Compose AnimatedContent transitions in dark theme.
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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