package org.siamdev.core.database

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteDriver
import org.siamdev.core.database.schema.BizSchema
import org.siamdev.core.database.schema.SysSchema

object DatabaseInitializer {

    fun initialize() {
        val driver = createDatabaseDriver()
        initSysDatabase(driver)
        initBizDatabase(driver)
    }

    private fun initSysDatabase(driver: SQLiteDriver) {
        val conn = driver.open(getDatabasePath("ZapPOS-SYS.db"))
        try {
            SysSchema.statements.forEach { conn.exec(it) }
        } finally {
            conn.close()
        }
    }

    private fun initBizDatabase(driver: SQLiteDriver) {
        val conn = driver.open(getDatabasePath("ZapPOS-BIZ.db"))
        try {
            BizSchema.statements.forEach { conn.exec(it) }
        } finally {
            conn.close()
        }
    }

    private fun SQLiteConnection.exec(sql: String) {
        prepare(sql).also { stmt ->
            try { stmt.step() } finally { stmt.close() }
        }
    }
}