package org.siamdev.zappos.ui.screens.product.entry

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MasterEntryViewModel : ViewModel() {

    data class State(
        val status: Boolean = false,
        val name: String = "",
        val price: String = ""
    )

    sealed class SideEffect {
        data object SaveSuccess : SideEffect()
        data class SaveError(val error: Throwable) : SideEffect()
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()
}