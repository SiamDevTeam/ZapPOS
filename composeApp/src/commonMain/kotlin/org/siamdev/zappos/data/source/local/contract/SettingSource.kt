/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.contract

import org.siamdev.zappos.data.source.local.model.CurrencyItem
import org.siamdev.zappos.data.source.local.model.FontItem
import org.siamdev.zappos.data.source.local.model.ThemeItem

interface SettingSource {

    suspend fun getThemes(): List<ThemeItem>
    suspend fun getActiveTheme(): ThemeItem?
    suspend fun setActiveTheme(themeId: String)
    suspend fun seedTheme(id: String, name: String, mode: String, isDefault: Boolean)

    suspend fun getFonts(): List<FontItem>
    suspend fun getActiveFont(): FontItem?
    suspend fun setActiveFont(fontId: String)
    suspend fun seedFont(id: String, name: String, size: Double)

    suspend fun getCurrencies(): List<CurrencyItem>
    suspend fun getPrimaryCurrency(): CurrencyItem?
    suspend fun getSecondaryCurrency(): CurrencyItem?
    suspend fun setPrimaryCurrency(currencyId: String)
    suspend fun setSecondaryCurrency(currencyId: String)
    suspend fun getShowSecondaryCurrency(): Boolean
    suspend fun setShowSecondaryCurrency(show: Boolean)
    suspend fun seedCurrency(id: String, code: String, name: String, symbol: String)

    suspend fun getAccentColor(): String?
    suspend fun setAccentColor(hex: String)

    suspend fun initSettings()
}