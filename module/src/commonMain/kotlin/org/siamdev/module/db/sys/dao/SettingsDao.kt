/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.schema.T_SYS_SETTINGS

class SettingsDao(private val db: AppDatabase) {

    suspend fun selectSystem(): T_SYS_SETTINGS? =
        db.sys { sETTINGS_CRUDQueries.selectSystem().executeAsOneOrNull() }

    suspend fun initSystem(themeId: String?, fontId: String?, primaryCurrencyId: String?, createdAt: Long) =
        db.sys { sETTINGS_CRUDQueries.initSystem(themeId, fontId, primaryCurrencyId, createdAt) }

    suspend fun updateTheme(themeId: String, updatedAt: Long, updatedBy: String) =
        db.sys { sETTINGS_CRUDQueries.updateTheme(themeId, updatedAt, updatedBy) }

    suspend fun updateFont(fontId: String, updatedAt: Long, updatedBy: String) =
        db.sys { sETTINGS_CRUDQueries.updateFont(fontId, updatedAt, updatedBy) }

    suspend fun updatePrimaryCurrency(currencyId: String, updatedAt: Long, updatedBy: String) =
        db.sys { sETTINGS_CRUDQueries.updatePrimaryCurrency(currencyId, updatedAt, updatedBy) }

    suspend fun updateSecondaryCurrency(currencyId: String, updatedAt: Long, updatedBy: String) =
        db.sys { sETTINGS_CRUDQueries.updateSecondaryCurrency(currencyId, updatedAt, updatedBy) }
}