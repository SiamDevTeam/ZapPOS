package org.siamdev.zappos.data.source.local

import lmdb.*
import org.siamdev.core.getDatabasePath


class Lmdb {

    private val env: Env = configure()
    private val dbCache = mutableMapOf<String, Dbi>()

    private fun configure(): Env {
        val path = getDatabasePath()
        return Env().apply {
            maxDatabases = 10u
            mapSize = (50 * 1024 * 1024).toULong()
            maxReaders = 126u
            open(path, EnvOption.NoTLS, EnvOption.NoSubDir)
        }
    }

    private fun dbi(name: String, txn: Txn): Dbi =
        dbCache.getOrPut(name) {
            txn.dbiOpen(name, DbiOption.Create)
        }

    fun put(dbName: String, key: String, value: String) =
        env.beginTxn { txn ->
            val db = dbi(dbName, txn)
            txn.put(db, key.encodeToByteArray(), value.encodeToByteArray())
            txn.commit()
        }

    fun get(dbName: String, key: String): String? =
        env.beginTxn { txn ->
            val db = dbi(dbName, txn)
            val (code, _, v) = txn.get(db, key.encodeToByteArray())
            if (code == 0) v.toByteArray()?.decodeToString() else null
        }

    fun delete(dbName: String, key: String) =
        env.beginTxn { txn ->
            val db = dbi(dbName, txn)
            txn.delete(db, key.encodeToByteArray())
            txn.commit()
        }

    fun exists(dbName: String, key: String): Boolean =
        env.beginTxn { txn ->
            val db = dbi(dbName, txn)
            val (code, _, _) = txn.get(db, key.encodeToByteArray())
            code == 0
        }

    fun close() {
        env.close()
        dbCache.clear()
    }
}
