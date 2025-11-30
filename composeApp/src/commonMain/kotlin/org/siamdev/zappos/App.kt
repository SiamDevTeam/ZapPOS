/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.zappos

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.screens.demo.CounterScreen
import org.siamdev.zappos.ui.screens.home.HomeScreen
import org.siamdev.zappos.ui.screens.splash.SplashScreen
import org.siamdev.zappos.ui.screens.login.LoginScreen
import org.siamdev.zappos.ui.viewmodel.AppViewModel

val lightColor = Color(color = 0xFFFCBE00)
val darkColor = Color(color = 0xFF515151)

@Composable
fun App(isDesktop: Boolean) {
    val viewModel: AppViewModel = viewModel()
    val lightColors = lightColorScheme(
        primary = lightColor,
        onPrimary = darkColor,
        primaryContainer = lightColor,
        onPrimaryContainer = darkColor
    )
    val darkColors = darkColorScheme(
        primary = lightColor,
        onPrimary = darkColor,
        primaryContainer = lightColor,
        onPrimaryContainer = darkColor
    )
    val colors = if (isSystemInDarkTheme()) darkColors else lightColors

    MaterialTheme(colorScheme = colors) {
        Crossfade(viewModel.showHome) { ready ->
            println("ready: $ready")
            when {
                ready -> {
                   // HomeScreen()
                    LoginScreen()
                   // CounterScreen()
                }
                !isDesktop -> SplashScreen()
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App(isDesktop = false)
}