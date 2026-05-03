package org.siamdev.module.db

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.siamdev.module.db.biz.ZapPOSBiz
import org.siamdev.module.db.sys.ZapPOSSys

actual suspend fun createBizDriver(): SqlDriver =
    NativeSqliteDriver(ZapPOSBiz.Schema.synchronous(), "ZapPOS-BIZ.db")

actual suspend fun createSysDriver(): SqlDriver =
    NativeSqliteDriver(ZapPOSSys.Schema.synchronous(), "ZapPOS-SYS.db")