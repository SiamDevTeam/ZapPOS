/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.siamdev.zappos.data.source.local.LocalSources
import org.siamdev.zappos.data.source.local.contract.SettingSource
import org.siamdev.zappos.data.source.local.model.CurrencyItem
import org.siamdev.zappos.data.source.local.model.FontItem
import org.siamdev.zappos.data.source.local.model.ThemeItem

class SettingViewModel : ViewModel() {

    data class State(
        val isLoading: Boolean = false,
        val theme: Theme = Theme(),
        val font: Font = Font(),
        val currency: Currency = Currency(),
        val appearance: Appearance = Appearance()
    ) {
        data class Theme(
            val options: List<ThemeItem> = emptyList(),
            val active: ThemeItem? = null
        )

        data class Font(
            val options: List<FontItem> = emptyList(),
            val active: FontItem? = null
        )

        data class Currency(
            val options: List<CurrencyItem> = emptyList(),
            val primary: CurrencyItem? = null,
            val secondary: CurrencyItem? = null,
            val showSecondary: Boolean = false
        )

        data class Appearance(val accentColor: String? = null)
    }

    sealed class SideEffect {
        data class WriteError(val error: Throwable) : SideEffect()
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SideEffect>(replay = 0)
    val sideEffect: SharedFlow<SideEffect> = _sideEffect.asSharedFlow()

    private val source: SettingSource? get() = LocalSources.setting

    init {
        viewModelScope.launch { loadAll() }
    }

    private suspend fun loadAll() {
        val src = source
        if (src == null) {
            _state.update { it.copy(isLoading = false) }
            return
        }

        _state.update { it.copy(isLoading = true) }

        seedDefaults(src)

        _state.update {
            it.copy(
                isLoading = false,
                theme = State.Theme(
                    options = src.getThemes(),
                    active = src.getActiveTheme()
                ),
                font = State.Font(
                    options = src.getFonts(),
                    active = src.getActiveFont()
                ),
                currency = State.Currency(
                    options = src.getCurrencies(),
                    primary = src.getPrimaryCurrency(),
                    secondary = src.getSecondaryCurrency(),
                    showSecondary = src.getShowSecondaryCurrency()
                ),
                appearance = State.Appearance(
                    accentColor = src.getAccentColor()
                )
            )
        }
    }

    private suspend fun seedDefaults(src: SettingSource) {
        if (src.getThemes().isEmpty()) {
            src.seedTheme(id = "theme-system", name = "System", mode = "SYSTEM", isDefault = true)
            src.seedTheme(id = "theme-light", name = "Light", mode = "LIGHT", isDefault = false)
            src.seedTheme(id = "theme-dark", name = "Dark", mode = "DARK", isDefault = false)
        }

        val existingSizes = src.getFonts().map { it.size.toInt() }.toSet()
        for (size in 12..20) {
            if (size !in existingSizes) {
                src.seedFont(id = "font-size-$size", name = "$size", size = size.toDouble())
            }
        }

        if (src.getCurrencies().isEmpty()) {
            src.seedCurrency(id = "ccy-thb", code = "THB", name = "Thai Baht", symbol = "฿")
            src.seedCurrency(id = "ccy-usd", code = "USD", name = "US Dollar", symbol = "$")
            src.seedCurrency(id = "ccy-btc", code = "BTC", name = "Bitcoin", symbol = "₿")
            src.seedCurrency(id = "ccy-sats", code = "SATS", name = "Satoshi", symbol = "⚡")
        }

        src.initSettings()

        if (src.getActiveTheme() == null) {
            src.getThemes().find { it.isDefault }?.let { src.setActiveTheme(it.id) }
        }

        if (src.getActiveFont() == null) {
            src.getFonts().find { it.id == "font-default" }?.let { src.setActiveFont(it.id) }
        }

        if (src.getPrimaryCurrency() == null) {
            src.getCurrencies().find { it.code == "THB" }?.let { src.setPrimaryCurrency(it.id) }
        }
    }

    fun selectTheme(themeId: String) {
        viewModelScope.launch {
            val previous = _state.value.theme.active
            _state.update { it.copy(theme = it.theme.copy(active = it.theme.options.find { t -> t.id == themeId })) }
            runCatching {
                source?.setActiveTheme(themeId)
            }.onFailure { error ->
                _state.update { it.copy(theme = it.theme.copy(active = previous)) }
                _sideEffect.emit(SideEffect.WriteError(error))
            }
        }
    }

    fun selectFont(fontId: String) {
        viewModelScope.launch {
            val previous = _state.value.font.active
            _state.update { it.copy(font = it.font.copy(active = it.font.options.find { f -> f.id == fontId })) }
            runCatching {
                source?.setActiveFont(fontId)
            }.onFailure { error ->
                _state.update { it.copy(font = it.font.copy(active = previous)) }
                _sideEffect.emit(SideEffect.WriteError(error))
            }
        }
    }

    fun selectPrimaryCurrency(currencyId: String) {
        viewModelScope.launch {
            val previous = _state.value.currency.primary
            _state.update { it.copy(currency = it.currency.copy(primary = it.currency.options.find { c -> c.id == currencyId })) }
            runCatching {
                source?.setPrimaryCurrency(currencyId)
            }.onFailure { error ->
                _state.update { it.copy(currency = it.currency.copy(primary = previous)) }
                _sideEffect.emit(SideEffect.WriteError(error))
            }
        }
    }

    fun selectSecondaryCurrency(currencyId: String) {
        viewModelScope.launch {
            val previous = _state.value.currency.secondary
            _state.update { it.copy(currency = it.currency.copy(secondary = it.currency.options.find { c -> c.id == currencyId })) }
            runCatching {
                source?.setSecondaryCurrency(currencyId)
            }.onFailure { error ->
                _state.update { it.copy(currency = it.currency.copy(secondary = previous)) }
                _sideEffect.emit(SideEffect.WriteError(error))
            }
        }
    }

    fun toggleSecondaryCurrency(show: Boolean) {
        viewModelScope.launch {
            val previous = _state.value.currency.showSecondary
            _state.update { it.copy(currency = it.currency.copy(showSecondary = show)) }
            runCatching {
                source?.setShowSecondaryCurrency(show)
            }.onFailure { error ->
                _state.update { it.copy(currency = it.currency.copy(showSecondary = previous)) }
                _sideEffect.emit(SideEffect.WriteError(error))
            }
        }
    }

    fun selectAccentColor(hex: String) {
        viewModelScope.launch {
            val previous = _state.value.appearance.accentColor
            _state.update { it.copy(appearance = it.appearance.copy(accentColor = hex)) }
            runCatching {
                source?.setAccentColor(hex)
            }.onFailure { error ->
                _state.update { it.copy(appearance = it.appearance.copy(accentColor = previous)) }
                _sideEffect.emit(SideEffect.WriteError(error))
            }
        }
    }
}