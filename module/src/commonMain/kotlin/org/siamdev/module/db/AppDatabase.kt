/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db

import app.cash.sqldelight.db.SqlDriver
import org.siamdev.module.db.biz.ZapPOSBiz
import org.siamdev.module.db.sys.ZapPOSSys

expect suspend fun createBizDriver(): SqlDriver
expect suspend fun createSysDriver(): SqlDriver

class AppDatabase(
    val bizDriver: SqlDriver,
    val sysDriver: SqlDriver
) {
    private val bizDb: ZapPOSBiz
    private val sysDb: ZapPOSSys

    init {
        bizDriver.execute(null, "PRAGMA foreign_keys = ON", 0)
        sysDriver.execute(null, "PRAGMA foreign_keys = ON", 0)
        bizDb = ZapPOSBiz(bizDriver)
        sysDb = ZapPOSSys(sysDriver)
    }

    suspend fun <R> biz(block: suspend ZapPOSBiz.() -> R): R = block(bizDb)
    suspend fun <R> sys(block: suspend ZapPOSSys.() -> R): R = block(sysDb)

    suspend fun <R> bizResult(block: ZapPOSBiz.() -> R): Result<R> = runCatching { block(bizDb) }
    suspend fun <R> sysResult(block: suspend ZapPOSSys.() -> R): Result<R> = runCatching { block(sysDb) }
}

suspend fun appDatabase(): AppDatabase = AppDatabase(createBizDriver(), createSysDriver())