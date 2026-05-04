/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.schema.M_SYS_FONT

class FontDao(private val db: AppDatabase) {

    suspend fun insert(id: String, name: String, size: Double, createdAt: Long, createdBy: String) =
        db.sys { fONT_CRUDQueries.insert(id, name, size, createdAt, createdBy) }

    suspend fun update(name: String, size: Double, updatedAt: Long, updatedBy: String, id: String) =
        db.sys { fONT_CRUDQueries.update(name, size, updatedAt, updatedBy, id) }

    suspend fun selectAll(): List<M_SYS_FONT> =
        db.sys { fONT_CRUDQueries.selectAll().executeAsList() }

    suspend fun selectById(id: String): M_SYS_FONT? =
        db.sys { fONT_CRUDQueries.selectById(id).executeAsOneOrNull() }
}