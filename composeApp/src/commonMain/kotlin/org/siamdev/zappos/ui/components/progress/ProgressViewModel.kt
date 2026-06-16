/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.progress

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProgressViewModel : ViewModel() {

    data class State(
        val steps: List<String> = emptyList(),
        val currentStep: Int = 0
    )

    sealed class SideEffect

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun setup(steps: List<String>, currentStep: Int) {
        _state.update { it.copy(steps = steps, currentStep = currentStep) }
    }
}