/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
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
import kotlinx.coroutines.runBlocking
import org.siamdev.zappos.ui.screens.splash.SplashScreen
import org.siamdev.zappos.ui.screens.splash.SplashViewModel
import org.siamdev.module.db.appDatabase
import javax.swing.SwingUtilities

private val database = runBlocking { appDatabase() }.also { it.registerDependencies() }

fun main() = application {
    database
    val splashVM = remember { SplashViewModel() }
    var showMainWindow by remember { mutableStateOf(false) }
    val platform = getPlatform()

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
            App(platform = platform, splashViewModel = splashVM)
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
