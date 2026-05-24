/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withTimeoutOrNull
import org.siamdev.zappos.cache.ImagePreloader
import org.siamdev.zappos.cache.ThumbnailSection
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "SplashVM"

class SplashViewModel : ViewModel() {

    private val _splashDone = MutableStateFlow(false)
    val splashDone = _splashDone.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _preloadProgress = MutableStateFlow(0f)
    val preloadProgress = _preloadProgress.asStateFlow()

    init {
        viewModelScope.launch {
            println("[$TAG] Splash started")
            val delayJob   = launch { runSplashDelay() }
            val preloadJob = launch { preloadImages() }

            delayJob.join()
            println("[$TAG] Splash delay done – waiting for preload (max 5 s)")
            withTimeoutOrNull(5_000) { preloadJob.join() }

            println("[$TAG] Ready – navigating")
            _isReady.value = true
        }
    }

    private suspend fun runSplashDelay() {
        delay(3000.milliseconds)
        _splashDone.value = true
    }

    private suspend fun preloadImages() {
        println("[$TAG] Preload begin")
        ImagePreloader.preloadMenuItems(
            section    = ThumbnailSection.PRODUCTS,
            onProgress = { progress ->
                _preloadProgress.value = progress
                val pct = (progress * 100).toInt()
                if (pct % 20 == 0) println("[$TAG] Preload $pct%")
            }
        )
        println("[$TAG] Preload end")
    }
}