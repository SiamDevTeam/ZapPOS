/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao

import kotlinx.datetime.Clock
import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.biz.schema.T_SALE_ORDER_H
import org.siamdev.module.db.sys.dao.NumberingDao

class SaleOrderDao(
    private val db: AppDatabase,
    private val numbering: NumberingDao
) {

    suspend fun insert(cusId: String?, currencyCode: String, remark: String?, createdBy: String): String {
        val soId = numbering.generate("SO", "SO-yyyymmdd-xxxx")
        val now = Clock.System.now().toEpochMilliseconds()
        db.biz { sALE_ORDER_H_CRUDQueries.insert(soId, cusId, currencyCode, now, remark, now, createdBy) }
        return soId
    }

    suspend fun selectById(soId: String): T_SALE_ORDER_H? =
        db.biz { sALE_ORDER_H_CRUDQueries.selectById(soId).executeAsOneOrNull() }

    suspend fun selectAll(): List<T_SALE_ORDER_H> =
        db.biz { sALE_ORDER_H_CRUDQueries.selectAll().executeAsList() }
}