/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.siamdev.zappos.data.source.local.CurrencyItem
import org.siamdev.zappos.data.source.local.FontItem
import org.siamdev.zappos.data.source.local.ThemeItem

@Stable
interface SettingFacade {
    val themes: List<ThemeItem>
    val activeTheme: ThemeItem?
    val fonts: List<FontItem>
    val activeFont: FontItem?
    val currencyOptions: List<CurrencyItem>
    val primaryCurrency: CurrencyItem?
    val secondaryCurrency: CurrencyItem?
    val showSecondaryCurrency: Boolean
    val accentColorHex: String?
    val isLoading: Boolean
    val errors: Flow<String>

    fun selectTheme(id: String)
    fun selectFont(id: String)
    fun selectPrimaryCurrency(id: String)
    fun selectSecondaryCurrency(id: String)
    fun toggleSecondaryCurrency(show: Boolean)
    fun selectAccentColor(hex: String)
}

class SettingFacadeImpl(private val vm: SettingViewModel) : SettingFacade {

    private var _state by mutableStateOf(vm.state.value)

    init {
        vm.viewModelScope.launch {
            vm.state.collect { _state = it }
        }
    }

    override val themes: List<ThemeItem> get() = _state.theme.options
    override val activeTheme: ThemeItem? get() = _state.theme.active
    override val fonts: List<FontItem> get() = _state.font.options
    override val activeFont: FontItem? get() = _state.font.active
    override val currencyOptions: List<CurrencyItem> get() = _state.currency.options
    override val primaryCurrency: CurrencyItem? get() = _state.currency.primary
    override val secondaryCurrency: CurrencyItem? get() = _state.currency.secondary
    override val showSecondaryCurrency: Boolean get() = _state.currency.showSecondary
    override val accentColorHex: String? get() = _state.appearance.accentColor
    override val isLoading: Boolean get() = _state.isLoading

    override val errors: Flow<String> = vm.sideEffect.map { effect ->
        when (effect) {
            is SettingViewModel.SideEffect.WriteError -> effect.error.message ?: "Operation failed"
        }
    }

    override fun selectTheme(id: String) = vm.selectTheme(id)
    override fun selectFont(id: String) = vm.selectFont(id)
    override fun selectPrimaryCurrency(id: String) = vm.selectPrimaryCurrency(id)
    override fun selectSecondaryCurrency(id: String) = vm.selectSecondaryCurrency(id)
    override fun toggleSecondaryCurrency(show: Boolean) = vm.toggleSecondaryCurrency(show)
    override fun selectAccentColor(hex: String) = vm.selectAccentColor(hex)
}