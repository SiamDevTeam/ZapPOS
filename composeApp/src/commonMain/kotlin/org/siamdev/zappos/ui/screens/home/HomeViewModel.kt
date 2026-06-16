/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    data class State(val isLoading: Boolean = false)

    sealed class SideEffect

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()
}