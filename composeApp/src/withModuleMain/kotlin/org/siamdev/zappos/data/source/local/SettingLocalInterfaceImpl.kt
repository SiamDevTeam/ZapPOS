/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.dao.CurrencyDao
import org.siamdev.module.db.sys.dao.FontDao
import org.siamdev.module.db.sys.dao.SettingsDao
import org.siamdev.module.db.sys.dao.ThemeDao
import org.siamdev.module.db.sys.schema.M_SYS_CURRENCY
import org.siamdev.module.db.sys.schema.M_SYS_FONT
import org.siamdev.module.db.sys.schema.M_SYS_THEME
import org.siamdev.zappos.utils.DateTimeUtils

class SettingLocalInterfaceImpl(db: AppDatabase) : SettingLocalInterface {

    private val themeDao = ThemeDao(db)
    private val fontDao = FontDao(db)
    private val currencyDao = CurrencyDao(db)
    private val settingsDao = SettingsDao(db)

    override suspend fun getThemes(): List<ThemeItem> =
        themeDao.selectAll().map { it.toThemeItem() }

    override suspend fun getActiveTheme(): ThemeItem? {
        val themeId = settingsDao.selectSystem()?.I_THEME_ID ?: return null
        return themeDao.selectById(themeId)?.toThemeItem()
    }

    override suspend fun setActiveTheme(themeId: String) =
        settingsDao.updateTheme(themeId, now(), "USER")

    override suspend fun seedTheme(id: String, name: String, mode: String, isDefault: Boolean) =
        themeDao.insert(id, name, mode, if (isDefault) 1L else 0L, now(), "SYSTEM")

    override suspend fun getFonts(): List<FontItem> =
        fontDao.selectAll().map { it.toFontItem() }

    override suspend fun getActiveFont(): FontItem? {
        val fontId = settingsDao.selectSystem()?.I_FONT_ID ?: return null
        return fontDao.selectById(fontId)?.toFontItem()
    }

    override suspend fun setActiveFont(fontId: String) =
        settingsDao.updateFont(fontId, now(), "USER")

    override suspend fun seedFont(id: String, name: String, size: Double) =
        fontDao.insert(id, name, size, now(), "SYSTEM")

    // ── Currency ──────────────────────────────────────────────────────────────

    override suspend fun getCurrencies(): List<CurrencyItem> =
        currencyDao.selectAll().map { it.toCurrencyItem() }

    override suspend fun getPrimaryCurrency(): CurrencyItem? {
        val id = settingsDao.selectSystem()?.I_PRIMARY_CURRENCY_ID ?: return null
        return currencyDao.selectById(id)?.toCurrencyItem()
    }

    override suspend fun getSecondaryCurrency(): CurrencyItem? {
        val id = settingsDao.selectSystem()?.I_SECONDARY_CURRENCY_ID ?: return null
        return currencyDao.selectById(id)?.toCurrencyItem()
    }

    override suspend fun setPrimaryCurrency(currencyId: String) =
        settingsDao.updatePrimaryCurrency(currencyId, now(), "USER")

    override suspend fun setSecondaryCurrency(currencyId: String) =
        settingsDao.updateSecondaryCurrency(currencyId, now(), "USER")

    override suspend fun seedCurrency(id: String, code: String, name: String, symbol: String) =
        currencyDao.insert(id, code, name, symbol, now(), "SYSTEM")


    override suspend fun initSettings() {
        if (settingsDao.selectSystem() == null) {
            settingsDao.initSystem(null, null, null, now())
        }
    }
    private fun now() = DateTimeUtils.nowEpochMillis()
}

private fun M_SYS_THEME.toThemeItem() =
    ThemeItem(I_STH_ID, I_NAME, I_MODE, I_IS_DEFAULT == 1L)

private fun M_SYS_FONT.toFontItem() =
    FontItem(I_SF_ID, I_NAME, I_SIZE)

private fun M_SYS_CURRENCY.toCurrencyItem() =
    CurrencyItem(I_SCY_ID, I_CODE, I_NAME, I_SYMBOL)