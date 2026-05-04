/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.schema.M_SYS_THEME

class ThemeDao(private val db: AppDatabase) {

    suspend fun insert(id: String, name: String, mode: String, isDefault: Long, createdAt: Long, createdBy: String) =
        db.sys { tHEME_CRUDQueries.insert(id, name, mode, isDefault, createdAt, createdBy) }

    suspend fun selectAll(): List<M_SYS_THEME> =
        db.sys { tHEME_CRUDQueries.selectAll().executeAsList() }

    suspend fun selectById(id: String): M_SYS_THEME? =
        db.sys { tHEME_CRUDQueries.selectById(id).executeAsOneOrNull() }

    suspend fun setDefault(id: String, updatedAt: Long, updatedBy: String) =
        db.sys { tHEME_CRUDQueries.setDefault(id, updatedAt, updatedBy) }
}