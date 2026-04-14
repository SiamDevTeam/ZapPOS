//package org.siamdev.zappos.data.source.local
//
//import lmdb.*
//import org.siamdev.core.getDatabasePath
//
//actual class LmdbConfig actual constructor() {
//
//    private val env: Env = Env().apply {
//        maxDatabases = 15u
//        mapSize = (200 * 1024 * 1024).toULong()
//        maxReaders = 128u
//        open(getDatabasePath(), EnvOption.NoTLS)
//    }
//
//    actual fun put(dbName: String, key: String, value: String) {
//        env.beginTxn { txn ->
//            val db = txn.dbiOpen(dbName, DbiOption.Create)
//            txn.put(db, key.encodeToByteArray(), value.encodeToByteArray())
//            txn.commit()
//        }
//    }
//
//    actual fun get(dbName: String, key: String): String? =
//        env.beginTxn { txn ->
//            val db = txn.dbiOpen(dbName, DbiOption.Create)
//            val (code, _, v) = txn.get(db, key.encodeToByteArray())
//            if (code == 0) v.toByteArray()?.decodeToString() else null
//        }
//
//    actual fun delete(dbName: String, key: String) {
//        env.beginTxn { txn ->
//            val db = txn.dbiOpen(dbName, DbiOption.Create)
//            txn.delete(db, key.encodeToByteArray())
//            txn.commit()
//        }
//    }
//
//    actual fun exists(dbName: String, key: String): Boolean =
//        env.beginTxn { txn ->
//            val db = txn.dbiOpen(dbName, DbiOption.Create)
//            val (code, _, _) = txn.get(db, key.encodeToByteArray())
//            code == 0
//        }
//
//    actual fun close() = env.close()
//}
