/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.appDatabase
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

private val database: AppDatabase = appDatabase()

fun MainViewController() = ComposeUIViewController {
    val splashVM = remember { SplashViewModel() }
    val platform = getPlatform()
    App(platform = platform, splashViewModel = splashVM)
}

fun initializeDatabase(): AppDatabase = appDatabase()