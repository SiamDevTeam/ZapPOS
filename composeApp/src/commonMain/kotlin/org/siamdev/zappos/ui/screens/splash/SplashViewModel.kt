package org.siamdev.zappos.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.siamdev.zappos.theme.ThemeMode
import org.siamdev.zappos.data.source.remote.RelayConfig

class SplashViewModel : ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.LIGHT)
    val themeMode = _themeMode.asStateFlow()

    private val _relayReady = MutableStateFlow(false)
    val relayReady = _relayReady.asStateFlow()

    private val _splashDone = MutableStateFlow(false)
    val splashDone = _splashDone.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            runSplashDelay()
            setupRelay()
            updateReadyState()
        }
    }

    private suspend fun runSplashDelay() {
        delay(3000)
        _splashDone.value = true
        updateReadyState()
    }

    private suspend fun setupRelay() {
        RelayConfig.setup()
        _relayReady.value = true
        updateReadyState()
    }

    private fun updateReadyState() {
        _isReady.value = _splashDone.value && _relayReady.value
    }
}
