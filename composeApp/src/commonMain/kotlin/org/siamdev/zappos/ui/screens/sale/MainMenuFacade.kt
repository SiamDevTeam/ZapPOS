/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Stable
interface MainMenuFacade {
    val items: List<MenuItem>
    val selectedKeys: List<Int>
    val totalFiat: String
    val totalSat: String
    val isLoading: Boolean

    fun ensureLoaded()
    fun addItem(id: Int)
    fun reduceItem(id: Int)
    fun setItemCount(id: Int, count: UInt)
    fun clearAllItems()
    fun reloadProductsData()
}

class MainMenuFacadeImpl(private val vm: MainMenuViewModel) : MainMenuFacade {

    private var _state by mutableStateOf(vm.state.value)

    init {
        vm.viewModelScope.launch {
            vm.state.collect { _state = it }
        }
    }

    override val items: List<MenuItem> get() = _state.catalog.items
    override val selectedKeys: List<Int> get() = _state.order.selectedKeys
    override val totalFiat: String get() = _state.order.totalFiat
    override val totalSat: String get() = _state.order.totalSat
    override val isLoading: Boolean get() = _state.isLoading

    override fun ensureLoaded() = vm.ensureLoaded()
    override fun addItem(id: Int) = vm.addItem(id)
    override fun reduceItem(id: Int) = vm.reduceItem(id)
    override fun setItemCount(id: Int, count: UInt) = vm.setItemCount(id, count)
    override fun clearAllItems() = vm.clearAllItems()
    override fun reloadProductsData() = vm.reloadProductsData()
}