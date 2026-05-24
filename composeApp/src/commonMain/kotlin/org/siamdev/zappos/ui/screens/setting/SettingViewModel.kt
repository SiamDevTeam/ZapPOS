/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.siamdev.zappos.data.source.local.CurrencyItem
import org.siamdev.zappos.data.source.local.FontItem
import org.siamdev.zappos.data.source.local.SettingLocalInterface
import org.siamdev.zappos.data.source.local.ThemeItem
import org.siamdev.zappos.data.source.local.settingSource

class SettingViewModel : ViewModel() {

    private val source: SettingLocalInterface? get() = settingSource

    private val _themes = MutableStateFlow<List<ThemeItem>>(emptyList())
    val themes = _themes.asStateFlow()

    private val _activeTheme = MutableStateFlow<ThemeItem?>(null)
    val activeTheme = _activeTheme.asStateFlow()

    private val _fonts = MutableStateFlow<List<FontItem>>(emptyList())
    val fonts = _fonts.asStateFlow()

    private val _activeFont = MutableStateFlow<FontItem?>(null)
    val activeFont = _activeFont.asStateFlow()

    private val _currencies = MutableStateFlow<List<CurrencyItem>>(emptyList())
    val currencies = _currencies.asStateFlow()

    private val _primaryCurrency = MutableStateFlow<CurrencyItem?>(null)
    val primaryCurrency = _primaryCurrency.asStateFlow()

    private val _secondaryCurrency = MutableStateFlow<CurrencyItem?>(null)
    val secondaryCurrency = _secondaryCurrency.asStateFlow()

    private val _showSecondaryCurrency = MutableStateFlow(false)
    val showSecondaryCurrency = _showSecondaryCurrency.asStateFlow()

    private val _accentColor = MutableStateFlow<String?>(null)
    val accentColor = _accentColor.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _writeError = MutableStateFlow<Throwable?>(null)
    val writeError = _writeError.asStateFlow()

    init {
        viewModelScope.launch {
            loadAll()
        }
    }

    private suspend fun loadAll() {
        val src = source
        if (src == null) {
            _isLoading.value = false
            return
        }

        _isLoading.value = true

        seedDefaults(src)

        _themes.value = src.getThemes()
        _activeTheme.value = src.getActiveTheme()

        _fonts.value = src.getFonts()
        _activeFont.value = src.getActiveFont()

        _currencies.value = src.getCurrencies()
        _primaryCurrency.value = src.getPrimaryCurrency()
        _secondaryCurrency.value = src.getSecondaryCurrency()

        _showSecondaryCurrency.value = src.getShowSecondaryCurrency()
        _accentColor.value = src.getAccentColor()

        _isLoading.value = false
    }

    private suspend fun seedDefaults(src: SettingLocalInterface) {

        if (src.getThemes().isEmpty()) {
            src.seedTheme(
                id = "theme-system",
                name = "System",
                mode = "SYSTEM",
                isDefault = true
            )

            src.seedTheme(
                id = "theme-light",
                name = "Light",
                mode = "LIGHT",
                isDefault = false
            )

            src.seedTheme(
                id = "theme-dark",
                name = "Dark",
                mode = "DARK",
                isDefault = false
            )
        }

        val existingSizes = src.getFonts().map { it.size.toInt() }.toSet()
        for (size in 12..20) {
            if (size !in existingSizes) {
                src.seedFont(
                    id = "font-size-$size",
                    name = "$size",
                    size = size.toDouble()
                )
            }
        }

        if (src.getCurrencies().isEmpty()) {
            src.seedCurrency(
                id = "ccy-thb",
                code = "THB",
                name = "Thai Baht",
                symbol = "฿"
            )

            src.seedCurrency(
                id = "ccy-usd",
                code = "USD",
                name = "US Dollar",
                symbol = "$"
            )

            src.seedCurrency(
                id = "ccy-btc",
                code = "BTC",
                name = "Bitcoin",
                symbol = "₿"
            )

            src.seedCurrency(
                id = "ccy-sats",
                code = "SATS",
                name = "Satoshi",
                symbol = "⚡"
            )
        }

        src.initSettings()

        if (src.getActiveTheme() == null) {
            src.getThemes()
                .find { it.isDefault }
                ?.let { src.setActiveTheme(it.id) }
        }

        if (src.getActiveFont() == null) {
            src.getFonts()
                .find { it.id == "font-default" }
                ?.let { src.setActiveFont(it.id) }
        }

        if (src.getPrimaryCurrency() == null) {
            src.getCurrencies()
                .find { it.code == "THB" }
                ?.let { src.setPrimaryCurrency(it.id) }
        }
    }

    fun selectTheme(themeId: String) {
        viewModelScope.launch {
            runCatching {
                source?.setActiveTheme(themeId)
            }.onSuccess {
                _activeTheme.value = _themes.value.find { it.id == themeId }
            }.onFailure {
                _writeError.value = it
            }
        }
    }

    fun selectFont(fontId: String) {
        viewModelScope.launch {
            runCatching {
                source?.setActiveFont(fontId)
            }.onSuccess {
                _activeFont.value = _fonts.value.find { it.id == fontId }
            }.onFailure {
                _writeError.value = it
            }
        }
    }

    fun selectPrimaryCurrency(currencyId: String) {
        viewModelScope.launch {
            runCatching {
                source?.setPrimaryCurrency(currencyId)
            }.onSuccess {
                _primaryCurrency.value = _currencies.value.find { it.id == currencyId }
            }.onFailure {
                _writeError.value = it
            }
        }
    }

    fun selectSecondaryCurrency(currencyId: String) {
        viewModelScope.launch {
            runCatching {
                source?.setSecondaryCurrency(currencyId)
            }.onSuccess {
                _secondaryCurrency.value =
                    _currencies.value.find { it.id == currencyId }
            }.onFailure {
                _writeError.value = it
            }
        }
    }

    fun toggleSecondaryCurrency(show: Boolean) {
        viewModelScope.launch {
            runCatching {
                source?.setShowSecondaryCurrency(show)
            }.onSuccess {
                _showSecondaryCurrency.value = show
            }.onFailure {
                _writeError.value = it
            }
        }
    }

    fun selectAccentColor(hex: String) {
        viewModelScope.launch {
            runCatching {
                source?.setAccentColor(hex)
            }.onSuccess {
                _accentColor.value = hex
            }.onFailure {
                _writeError.value = it
            }
        }
    }

    fun clearError() {
        _writeError.value = null
    }
}