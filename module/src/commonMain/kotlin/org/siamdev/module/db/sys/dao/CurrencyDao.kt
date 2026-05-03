package org.siamdev.module.db.sys.dao

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.M_SYS_CURRENCY

class CurrencyDao(private val db: AppDatabase) {

    suspend fun insert(id: String, code: String, name: String, symbol: String, createdAt: Long, createdBy: String) =
        db.sys { currencyQueries.insert(id, code, name, symbol, createdAt, createdBy) }

    suspend fun update(code: String, name: String, symbol: String, updatedAt: Long, updatedBy: String, id: String) =
        db.sys { currencyQueries.update(code, name, symbol, updatedAt, updatedBy, id) }

    suspend fun selectAll(): List<M_SYS_CURRENCY> =
        db.sys { currencyQueries.selectAll().executeAsList() }

    suspend fun selectById(id: String): M_SYS_CURRENCY? =
        db.sys { currencyQueries.selectById(id).executeAsOneOrNull() }

    suspend fun selectByCode(code: String): M_SYS_CURRENCY? =
        db.sys { currencyQueries.selectByCode(code).executeAsOneOrNull() }
}