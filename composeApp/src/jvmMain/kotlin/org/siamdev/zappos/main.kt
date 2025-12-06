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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.siamdev.zappos.ui.screens.splash.SplashScreen
import org.siamdev.zappos.ui.screens.splash.SplashViewModel
import javax.swing.SwingUtilities


fun main() = application {
    val splashVM = remember { SplashViewModel() }
    var showMainWindow by remember { mutableStateOf(false) }

    if (!showMainWindow) {
        DesktopSplashWindow(
            splashViewModel = splashVM,
            onFinished = { showMainWindow = true }
        )
    }

    if (showMainWindow) {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ZapPOS",
            state = rememberWindowState(
                placement = WindowPlacement.Maximized,
                position = WindowPosition(Alignment.Center)
            )
        ) {
            App(isDesktop = true, splashViewModel = splashVM)
        }
    }
}

@Composable
fun DesktopSplashWindow(
    splashViewModel: SplashViewModel,
    onFinished: () -> Unit
) {
    var isOpen by remember { mutableStateOf(true) }
    if (isOpen) {
        Window(
            onCloseRequest = {},
            title = "",
            resizable = false,
            undecorated = true,
            transparent = false,
            state = rememberWindowState(
                placement = WindowPlacement.Floating,
                position = WindowPosition(Alignment.Center),
                width = 560.dp,
                height = 370.dp
            )
        ) {
            SplashScreen(viewModel = splashViewModel)
        }

        val isReady by splashViewModel.isReady.collectAsState()
        LaunchedEffect(isReady) {
            if (isReady) {
                isOpen = false
                onFinished()
            }
        }
    }
}


private inline fun <T : Any> runOnMainThreadBlocking(crossinline block: () -> T): T {
    lateinit var result: T
    SwingUtilities.invokeAndWait { result = block() }
    return result
}
