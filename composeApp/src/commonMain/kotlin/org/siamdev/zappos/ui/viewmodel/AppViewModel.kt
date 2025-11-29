package org.siamdev.zappos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.siamdev.zappos.data.source.remote.RelayConfig

class AppViewModel: ViewModel() {

    var relayReady by mutableStateOf(false)
        private set

    var splashDone by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            RelayConfig.setup()
            relayReady = true
        }

        viewModelScope.launch(Dispatchers.Main) {
            delay(2000L)
            splashDone = true
        }
    }

    val showHome: Boolean
        get() = relayReady && splashDone
}
