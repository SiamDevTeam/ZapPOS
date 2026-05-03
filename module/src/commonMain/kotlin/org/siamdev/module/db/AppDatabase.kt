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

    fun <R> biz(block: ZapPOSBiz.() -> R): R = block(bizDb)
    fun <R> sys(block: ZapPOSSys.() -> R): R = block(sysDb)

    fun <R> bizResult(block: ZapPOSBiz.() -> R): Result<R> = runCatching { block(bizDb) }
    fun <R> sysResult(block: ZapPOSSys.() -> R): Result<R> = runCatching { block(sysDb) }
}

suspend fun appDatabase(): AppDatabase = AppDatabase(createBizDriver(), createSysDriver())