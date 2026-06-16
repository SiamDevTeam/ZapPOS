/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.count

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {

    data class State(val count: Int = 0)

    sealed class SideEffect

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun plus() {
        _state.update { it.copy(count = it.count + 1) }
    }

    fun minus() {
        _state.update { it.copy(count = it.count - 1) }
    }

    fun reset() {
        _state.value = State()
    }
}