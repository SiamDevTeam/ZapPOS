/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.schema.M_SYS_CURRENCY

class CurrencyDao(private val db: AppDatabase) {

    suspend fun insert(code: String, name: String, symbol: String, createdAt: Long, createdBy: String) =
        db.sys { cURRENCY_CRUDQueries.insert(code, name, symbol, createdAt, createdBy) }

    suspend fun update(name: String, symbol: String, updatedAt: Long, updatedBy: String, code: String) =
        db.sys { cURRENCY_CRUDQueries.update(name, symbol, updatedAt, updatedBy, code) }

    suspend fun selectAll(): List<M_SYS_CURRENCY> =
        db.sys { cURRENCY_CRUDQueries.selectAll().executeAsList() }

    suspend fun selectByCode(code: String): M_SYS_CURRENCY? =
        db.sys { cURRENCY_CRUDQueries.selectByCode(code).executeAsOneOrNull() }
}