/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SplashViewModel : ViewModel() {

    private val _splashDone = MutableStateFlow(false)
    val splashDone = _splashDone.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            runSplashDelay()
            updateReadyState()
        }
    }

    private suspend fun runSplashDelay() {
        delay(3000.milliseconds)
        _splashDone.value = true
    }

    private fun updateReadyState() {
        _isReady.value = _splashDone.value
    }
}
