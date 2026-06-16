/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.count

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Stable
interface CounterFacade {
    val count: Int
    fun plus()
    fun minus()
    fun reset()
}

class CounterFacadeImpl(private val vm: CounterViewModel) : CounterFacade {

    private var _state by mutableStateOf(vm.state.value)

    init {
        vm.viewModelScope.launch {
            vm.state.collect { _state = it }
        }
    }

    override val count: Int get() = _state.count

    override fun plus() = vm.plus()
    override fun minus() = vm.minus()
    override fun reset() = vm.reset()
}