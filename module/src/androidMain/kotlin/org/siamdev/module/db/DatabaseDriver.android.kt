/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.siamdev.module.db.biz.ZapPOSBiz
import org.siamdev.module.db.sys.ZapPOSSys

actual suspend fun createBizDriver(): SqlDriver =
    AndroidSqliteDriver(ZapPOSBiz.Schema.synchronous(), AppContext.value, "ZapPOS-BIZ.db")

actual suspend fun createSysDriver(): SqlDriver =
    AndroidSqliteDriver(ZapPOSSys.Schema.synchronous(), AppContext.value, "ZapPOS-SYS.db")