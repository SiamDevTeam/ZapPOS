/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import org.siamdev.zappos.cache.ImagePreloader
import org.siamdev.zappos.cache.ThumbnailSection
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "SplashVM"

class SplashViewModel : ViewModel() {

    data class State(
        val isReady: Boolean = false,
        val isDone: Boolean = false,
        val preloadProgress: Float = 0f
    )

    sealed class SideEffect {
        data object NavigateAway : SideEffect()
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SideEffect>(replay = 0)
    val sideEffect: SharedFlow<SideEffect> = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            println("[$TAG] Splash started")
            val delayJob = launch { runSplashDelay() }
            val preloadJob = launch { preloadImages() }

            delayJob.join()
            println("[$TAG] Splash delay done – waiting for preload (max 5 s)")
            withTimeoutOrNull(5_000) { preloadJob.join() }

            println("[$TAG] Ready – navigating")
            _state.update { it.copy(isReady = true) }
            _sideEffect.emit(SideEffect.NavigateAway)
        }
    }

    private suspend fun runSplashDelay() {
        delay(3000.milliseconds)
        _state.update { it.copy(isDone = true) }
        println("[$TAG] Splash delay done")
    }

    private suspend fun preloadImages() {
        println("[$TAG] Preload begin")
        ImagePreloader.preloadMenuItems(
            section = ThumbnailSection.PRODUCTS,
            onProgress = { progress ->
                _state.update { it.copy(preloadProgress = progress) }
                val pct = (progress * 100).toInt()
                if (pct % 20 == 0) println("[$TAG] Preload $pct%")
            }
        )
        println("[$TAG] Preload end")
    }
}