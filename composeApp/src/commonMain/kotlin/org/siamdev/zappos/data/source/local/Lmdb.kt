/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDev by SiamDharmar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.zappos.data.source.local


import lmdb.*
import org.siamdev.core.getDatabasePath

class Lmdb {

    private val env: Env = configure()

    private fun configure(): Env {
        val path = getDatabasePath()
        //println("LMDB path: $path")

        return Env().apply {
            maxDatabases = 15u
            mapSize = (200 * 1024 * 1024).toULong()
            maxReaders = 128u
            open(path, EnvOption.NoTLS)
        }
    }

    private fun dbi(txn: Txn, name: String): Dbi =
        txn.dbiOpen(name, DbiOption.Create)

    fun put(dbName: String, key: String, value: String) =
        env.beginTxn { txn ->
            val db = dbi(txn, dbName)
            txn.put(db, key.encodeToByteArray(), value.encodeToByteArray())
            txn.commit()
        }

    fun get(dbName: String, key: String): String? =
        env.beginTxn { txn ->
            val db = dbi(txn, dbName)
            val (code, _, v) = txn.get(db, key.encodeToByteArray())
            if (code == 0) v.toByteArray()?.decodeToString() else null
        }

    fun delete(dbName: String, key: String) =
        env.beginTxn { txn ->
            val db = dbi(txn, dbName)
            txn.delete(db, key.encodeToByteArray())
            txn.commit()
        }

    fun exists(dbName: String, key: String): Boolean =
        env.beginTxn { txn ->
            val db = dbi(txn, dbName)
            val (code, _, _) = txn.get(db, key.encodeToByteArray())
            code == 0
        }

    fun close() = env.close()
}
