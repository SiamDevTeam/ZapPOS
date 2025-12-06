package org.siamdev.zappos.di

import org.koin.dsl.module
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

val appModule = module {
    single { SplashViewModel() }
}