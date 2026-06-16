/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.progress

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Stable
interface ProgressFacade {
    val steps: List<String>
    val currentStep: Int
    fun setup(steps: List<String>, currentStep: Int)
}

class ProgressFacadeImpl(private val vm: ProgressViewModel) : ProgressFacade {

    private var _state by mutableStateOf(vm.state.value)

    init {
        vm.viewModelScope.launch {
            vm.state.collect { _state = it }
        }
    }

    override val steps: List<String> get() = _state.steps
    override val currentStep: Int get() = _state.currentStep

    override fun setup(steps: List<String>, currentStep: Int) = vm.setup(steps, currentStep)
}