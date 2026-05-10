/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.Actor
import org.siamdev.module.db.CrudCtx
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.sys.dao.CurrencyDao
import org.siamdev.module.db.sys.dao.FontDao
import org.siamdev.module.db.sys.dao.SettingsDao
import org.siamdev.module.db.sys.dao.ThemeDao
import org.siamdev.module.db.sys.dao.crud.sysTx
import org.siamdev.module.db.sys.dao.crud.table.SysSettings
import org.siamdev.module.db.sys.schema.M_SYS_CURRENCY
import org.siamdev.module.db.sys.schema.M_SYS_FONT
import org.siamdev.module.db.sys.schema.M_SYS_THEME
import org.siamdev.zappos.utils.DateTimeUtils

class SettingLocalInterfaceImpl(private val db: AppDatabase) : SettingLocalInterface {

    // CrudCtx for all T_SYS_SETTINGS writes.
    // actor is a get() so each transaction captures the current timestamp.
    // userId becomes a real account ID once auth is in place.
    private val ctx: CrudCtx = object : CrudCtx {
        override val actor   get() = Actor("USER")
        override val writeOp = WriteOp.UPDATE
    }

    private val themeDao    = ThemeDao(db)
    private val fontDao     = FontDao(db)
    private val currencyDao = CurrencyDao(db)
    private val settingsDao = SettingsDao(db)

    override suspend fun getThemes(): List<ThemeItem> =
        themeDao.selectAll().map { it.toThemeItem() }

    override suspend fun getActiveTheme(): ThemeItem? {
        val themeId = settingsDao.selectSystem()?.I_THEME_ID ?: return null
        return themeDao.selectById(themeId)?.toThemeItem()
    }

    override suspend fun setActiveTheme(themeId: String): Long {
        db.sysTx(ctx) { SysSettings { I_THEME_ID = themeId } }.getOrThrow()
        return 1L
    }

    override suspend fun seedTheme(id: String, name: String, mode: String, isDefault: Boolean) =
        themeDao.insert(id, name, mode, if (isDefault) 1L else 0L, now(), "SYSTEM")

    override suspend fun getFonts(): List<FontItem> =
        fontDao.selectAll().map { it.toFontItem() }

    override suspend fun getActiveFont(): FontItem? {
        val fontId = settingsDao.selectSystem()?.I_FONT_ID ?: return null
        return fontDao.selectById(fontId)?.toFontItem()
    }

    override suspend fun setActiveFont(fontId: String): Long {
        db.sysTx(ctx) { SysSettings { I_FONT_ID = fontId } }.getOrThrow()
        return 1L
    }

    override suspend fun seedFont(id: String, name: String, size: Double) =
        fontDao.insert(id, name, size, now(), "SYSTEM")

    override suspend fun getCurrencies(): List<CurrencyItem> =
        currencyDao.selectAll().map { it.toCurrencyItem() }

    override suspend fun getPrimaryCurrency(): CurrencyItem? {
        val id = settingsDao.selectSystem()?.I_PRIMARY_CURRENCY_ID ?: return null
        return currencyDao.selectByCode(id)?.toCurrencyItem()
    }

    override suspend fun getSecondaryCurrency(): CurrencyItem? {
        val id = settingsDao.selectSystem()?.I_SECONDARY_CURRENCY_ID ?: return null
        return currencyDao.selectByCode(id)?.toCurrencyItem()
    }

    override suspend fun setPrimaryCurrency(currencyId: String): Long {
        db.sysTx(ctx) { SysSettings { I_PRIMARY_CURRENCY_ID = currencyId } }.getOrThrow()
        return 1L
    }

    override suspend fun setSecondaryCurrency(currencyId: String): Long {
        db.sysTx(ctx) { SysSettings { I_SECONDARY_CURRENCY_ID = currencyId } }.getOrThrow()
        return 1L
    }

    override suspend fun getShowSecondaryCurrency(): Boolean =
        settingsDao.selectSystem()?.I_SHOW_SECONDARY_CURRENCY == 1L

    override suspend fun setShowSecondaryCurrency(show: Boolean): Long {
        settingsDao.updateShowSecondaryCurrency(if (show) 1L else 0L, now(), "USER")
        return 1L
    }

    override suspend fun seedCurrency(id: String, code: String, name: String, symbol: String) =
        currencyDao.insert(code, name, symbol, now(), "SYSTEM")

    override suspend fun initSettings() {
        if (settingsDao.selectSystem() == null) {
            settingsDao.initSystem(null, null, null, now())
        }
    }

    private fun now() = DateTimeUtils.nowEpochMillis()
}

private fun M_SYS_THEME.toThemeItem()       = ThemeItem(I_STH_ID, I_NAME, I_MODE, I_IS_DEFAULT == 1L)
private fun M_SYS_FONT.toFontItem()         = FontItem(I_SF_ID, I_NAME, I_SIZE)
private fun M_SYS_CURRENCY.toCurrencyItem() = CurrencyItem(I_CURRENCY_CODE, I_CURRENCY_CODE, I_NAME, I_SYMBOL)