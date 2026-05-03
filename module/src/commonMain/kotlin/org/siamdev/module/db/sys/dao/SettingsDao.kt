package org.siamdev.module.db.sys.dao

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.M_SYS_SETTINGS

class SettingsDao(private val db: AppDatabase) {

    suspend fun selectSystem(): M_SYS_SETTINGS? =
        db.sys { settingsQueries.selectSystem().executeAsOneOrNull() }

    suspend fun initSystem(themeId: String?, fontId: String?, primaryCurrencyId: String?, createdAt: Long) =
        db.sys { settingsQueries.initSystem(themeId, fontId, primaryCurrencyId, createdAt) }

    suspend fun updateTheme(themeId: String, updatedAt: Long, updatedBy: String) =
        db.sys { settingsQueries.updateTheme(themeId, updatedAt, updatedBy) }

    suspend fun updateFont(fontId: String, updatedAt: Long, updatedBy: String) =
        db.sys { settingsQueries.updateFont(fontId, updatedAt, updatedBy) }

    suspend fun updatePrimaryCurrency(currencyId: String, updatedAt: Long, updatedBy: String) =
        db.sys { settingsQueries.updatePrimaryCurrency(currencyId, updatedAt, updatedBy) }

    suspend fun updateSecondaryCurrency(currencyId: String, updatedAt: Long, updatedBy: String) =
        db.sys { settingsQueries.updateSecondaryCurrency(currencyId, updatedAt, updatedBy) }
}