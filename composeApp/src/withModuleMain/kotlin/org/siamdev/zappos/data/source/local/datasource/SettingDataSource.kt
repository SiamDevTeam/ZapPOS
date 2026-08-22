/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.datasource

import org.siamdev.module.db.Actor
import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.CrudCtx
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.sys.dao.CurrencyDao
import org.siamdev.module.db.sys.dao.FontDao
import org.siamdev.module.db.sys.dao.SettingsDao
import org.siamdev.module.db.sys.dao.ThemeDao
import org.siamdev.module.db.sys.dao.crud.sysTx
import org.siamdev.module.db.sys.dao.crud.table.SysSettings
import org.siamdev.zappos.data.source.local.contract.SettingSource
import org.siamdev.zappos.data.source.local.mapper.toModel
import org.siamdev.zappos.data.source.local.model.CurrencyItem
import org.siamdev.zappos.data.source.local.model.FontItem
import org.siamdev.zappos.data.source.local.model.ThemeItem
import org.siamdev.zappos.utils.DateTimeUtils

class SettingDataSource(private val db: AppDatabase) : SettingSource {

    private val ctx: CrudCtx = object : CrudCtx {
        override val actor get() = Actor("USER")
        override val writeOp = WriteOp.UPDATE
    }

    private val themeDao = ThemeDao(db)
    private val fontDao = FontDao(db)
    private val currencyDao = CurrencyDao(db)
    private val settingsDao = SettingsDao(db)

    override suspend fun getThemes(): List<ThemeItem> =
        themeDao.selectAll().map { it.toModel() }

    override suspend fun getActiveTheme(): ThemeItem? {
        val themeId = settingsDao.selectSystem()?.I_THEME_ID ?: return null
        return themeDao.selectById(themeId)?.toModel()
    }

    override suspend fun setActiveTheme(themeId: String) {
        db.sysTx(ctx) { SysSettings { I_THEME_ID = themeId } }.getOrThrow()
    }

    override suspend fun seedTheme(id: String, name: String, mode: String, isDefault: Boolean) {
        themeDao.insert(id, name, mode, if (isDefault) 1L else 0L, now(), "SYSTEM")
    }

    override suspend fun getFonts(): List<FontItem> =
        fontDao.selectAll().map { it.toModel() }

    override suspend fun getActiveFont(): FontItem? {
        val fontId = settingsDao.selectSystem()?.I_FONT_ID ?: return null
        return fontDao.selectById(fontId)?.toModel()
    }

    override suspend fun setActiveFont(fontId: String) {
        db.sysTx(ctx) { SysSettings { I_FONT_ID = fontId } }.getOrThrow()
    }

    override suspend fun seedFont(id: String, name: String, size: Double) {
        fontDao.insert(id, name, size, now(), "SYSTEM")
    }

    override suspend fun getCurrencies(): List<CurrencyItem> =
        currencyDao.selectAll().map { it.toModel() }

    override suspend fun getPrimaryCurrency(): CurrencyItem? {
        val primaryCurrencyId = settingsDao.selectSystem()?.I_PRIMARY_CURRENCY_ID ?: return null
        return currencyDao.selectByCode(primaryCurrencyId)?.toModel()
    }

    override suspend fun getSecondaryCurrency(): CurrencyItem? {
        val secondaryCurrencyId = settingsDao.selectSystem()?.I_SECONDARY_CURRENCY_ID ?: return null
        return currencyDao.selectByCode(secondaryCurrencyId)?.toModel()
    }

    override suspend fun setPrimaryCurrency(currencyId: String) {
        db.sysTx(ctx) { SysSettings { I_PRIMARY_CURRENCY_ID = currencyId } }.getOrThrow()
    }

    override suspend fun setSecondaryCurrency(currencyId: String) {
        db.sysTx(ctx) { SysSettings { I_SECONDARY_CURRENCY_ID = currencyId } }.getOrThrow()
    }

    override suspend fun getShowSecondaryCurrency(): Boolean =
        settingsDao.selectSystem()?.I_SHOW_SECONDARY_CURRENCY == 1L

    override suspend fun setShowSecondaryCurrency(show: Boolean) {
        settingsDao.updateShowSecondaryCurrency(if (show) 1L else 0L, now(), "USER")
    }

    override suspend fun seedCurrency(id: String, code: String, name: String, symbol: String) {
        currencyDao.insert(code, name, symbol, now(), "SYSTEM")
    }

    override suspend fun getAccentColor(): String? =
        settingsDao.selectSystem()?.I_ACCENT_COLOR

    override suspend fun setAccentColor(hex: String) {
        settingsDao.updateAccentColor(hex, now(), "USER")
    }

    override suspend fun initSettings() {
        if (settingsDao.selectSystem() == null) {
            settingsDao.initSystem(null, null, null, now())
        }
    }

    private fun now() = DateTimeUtils.nowEpochMillis()
}