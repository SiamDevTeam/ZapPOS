/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db

import app.cash.sqldelight.async.coroutines.await
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.siamdev.module.db.biz.ZapPOSBiz
import org.siamdev.module.db.sys.ZapPOSSys
import java.io.File

actual suspend fun createBizDriver(): SqlDriver {
    val dir = File(System.getProperty("user.home"), ".zappos/db")
    dir.mkdirs()
    val dbFile = File(dir, "ZapPOS-BIZ.db")
    val isNew = !dbFile.exists()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    if (isNew) {
        ZapPOSBiz.Schema.create(driver).await()
        driver.execute(null, "PRAGMA user_version = ${ZapPOSBiz.Schema.version}", 0)
    } else {
        val oldVersion = driver.getUserVersion()
        val newVersion = ZapPOSBiz.Schema.version
        if (oldVersion < newVersion) {
            ZapPOSBiz.Schema.migrate(driver, oldVersion, newVersion).await()
            driver.execute(null, "PRAGMA user_version = $newVersion", 0)
        }
    }
    return driver
}

actual suspend fun createSysDriver(): SqlDriver {
    val dir = File(System.getProperty("user.home"), ".zappos/db")
    dir.mkdirs()
    val dbFile = File(dir, "ZapPOS-SYS.db")
    val isNew = !dbFile.exists()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    if (isNew) {
        ZapPOSSys.Schema.create(driver).await()
        driver.execute(null, "PRAGMA user_version = ${ZapPOSSys.Schema.version}", 0)
    } else {
        val oldVersion = driver.getUserVersion()
        val newVersion = ZapPOSSys.Schema.version
        if (oldVersion < newVersion) {
            ZapPOSSys.Schema.migrate(driver, oldVersion, newVersion).await()
            driver.execute(null, "PRAGMA user_version = $newVersion", 0)
        }
    }
    return driver
}

private fun SqlDriver.getUserVersion(): Long =
    executeQuery(null, "PRAGMA user_version", { cursor ->
        QueryResult.Value(if (cursor.next().value) cursor.getLong(0) ?: 0L else 0L)
    }, 0, null).value