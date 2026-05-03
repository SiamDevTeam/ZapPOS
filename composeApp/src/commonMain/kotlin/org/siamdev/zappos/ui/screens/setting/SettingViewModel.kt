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

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch { loadAll() }
    }

    private suspend fun loadAll() {
        val src = source ?: return
        _isLoading.value = true
        seedDefaults(src)
        _themes.value = src.getThemes()
        _activeTheme.value = src.getActiveTheme()
        _fonts.value = src.getFonts()
        _activeFont.value = src.getActiveFont()
        _currencies.value = src.getCurrencies()
        _primaryCurrency.value = src.getPrimaryCurrency()
        _secondaryCurrency.value = src.getSecondaryCurrency()
        _isLoading.value = false
    }

    private suspend fun seedDefaults(src: SettingLocalInterface) {
        if (src.getThemes().isEmpty()) {
            src.seedTheme("theme-system", "System", "SYSTEM", true)
            src.seedTheme("theme-light", "Light", "LIGHT", false)
            src.seedTheme("theme-dark", "Dark", "DARK", false)
        }
        if (src.getFonts().isEmpty()) {
            src.seedFont("font-default", "Default", 14.0)
            src.seedFont("font-large", "Large", 18.0)
            src.seedFont("font-xlarge", "Extra Large", 22.0)
        }
        if (src.getCurrencies().isEmpty()) {
            src.seedCurrency("ccy-thb", "THB", "Thai Baht", "฿")
            src.seedCurrency("ccy-usd", "USD", "US Dollar", "$")
            src.seedCurrency("ccy-btc", "BTC", "Bitcoin", "₿")
            src.seedCurrency("ccy-sats", "SATS", "Satoshi", "⚡")
        }
        src.initSettings()
        if (src.getActiveTheme() == null) {
            src.getThemes().find { it.isDefault }?.let { src.setActiveTheme(it.id) }
        }
        if (src.getActiveFont() == null) {
            src.getFonts().firstOrNull()?.let { src.setActiveFont(it.id) }
        }
        if (src.getPrimaryCurrency() == null) {
            src.getCurrencies().find { it.code == "THB" }?.let { src.setPrimaryCurrency(it.id) }
        }
    }

    fun selectTheme(themeId: String) {
        val src = source ?: return
        viewModelScope.launch {
            src.setActiveTheme(themeId)
            _activeTheme.value = src.getActiveTheme()
        }
    }

    fun selectFont(fontId: String) {
        val src = source ?: return
        viewModelScope.launch {
            src.setActiveFont(fontId)
            _activeFont.value = src.getActiveFont()
        }
    }

    fun selectPrimaryCurrency(currencyId: String) {
        val src = source ?: return
        viewModelScope.launch {
            src.setPrimaryCurrency(currencyId)
            _primaryCurrency.value = src.getPrimaryCurrency()
        }
    }

    fun selectSecondaryCurrency(currencyId: String) {
        val src = source ?: return
        viewModelScope.launch {
            src.setSecondaryCurrency(currencyId)
            _secondaryCurrency.value = src.getSecondaryCurrency()
        }
    }
}