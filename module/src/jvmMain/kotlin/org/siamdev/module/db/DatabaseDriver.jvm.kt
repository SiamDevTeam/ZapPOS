package org.siamdev.module.db

import app.cash.sqldelight.async.coroutines.await
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
    if (isNew) ZapPOSBiz.Schema.create(driver).await()
    return driver
}

actual suspend fun createSysDriver(): SqlDriver {
    val dir = File(System.getProperty("user.home"), ".zappos/db")
    dir.mkdirs()
    val dbFile = File(dir, "ZapPOS-SYS.db")
    val isNew = !dbFile.exists()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    if (isNew) ZapPOSSys.Schema.create(driver).await()
    return driver
}