package org.siamdev.zappos

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import org.siamdev.module.db.AppDatabase

suspend fun checkDatabase(db: AppDatabase) {
    console.log("[BizDB] tables:", db.bizDriver.listTables())
    console.log("[SysDB] tables:", db.sysDriver.listTables())
}

private suspend fun SqlDriver.listTables(): String =
    executeQuery(
        identifier = null,
        sql = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name",
        mapper = { cursor: SqlCursor ->
            QueryResult.AsyncValue {
                buildList {
                    while (cursor.next().await()) {
                        add(cursor.getString(0) ?: "?")
                    }
                }
            }
        },
        parameters = 0
    ).await().joinToString()