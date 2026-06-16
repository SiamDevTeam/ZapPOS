/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.nav

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
interface NavigationSurface {
    val activeNavId: String
    val activeSectionId: String?

    fun setActiveNav(id: String)
    fun setFilter(sectionId: String?)
}

class NavigationSurfaceImpl(private val vm: NavigationViewModel) : NavigationSurface {

    private var _state by mutableStateOf(vm.state.value)

    init {
        vm.viewModelScope.launch {
            vm.state.collect { _state = it }
        }
    }

    override val activeNavId: String get() = _state.activeNavId
    override val activeSectionId: String? get() = _state.activeSectionId

    override fun setActiveNav(id: String) = vm.setActiveNav(id)
    override fun setFilter(sectionId: String?) = vm.setFilter(sectionId)
}

class NavigationViewModel : ViewModel() {

    data class State(
        val activeNavId: String = "home",
        val activeSectionId: String? = "SALES"
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun setActiveNav(id: String) {
        _state.update { it.copy(activeNavId = id) }
    }

    fun setFilter(sectionId: String?) {
        _state.update { it.copy(activeSectionId = sectionId) }
    }
}