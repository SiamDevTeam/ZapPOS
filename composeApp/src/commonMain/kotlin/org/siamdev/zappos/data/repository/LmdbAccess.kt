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
package org.siamdev.zappos.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.siamdev.zappos.data.source.local.Lmdb

suspend inline fun <T> runLmdb(crossinline block: (Lmdb) -> T): T = withContext(Dispatchers.IO) {
    val lmdb = Lmdb()
    return@withContext try {
        block(lmdb)
    } finally {
        lmdb.close()
    }
}

val StandardJson = Json {
    prettyPrint = false
    ignoreUnknownKeys = true
    isLenient = true
}

inline fun <reified T> Lmdb.set(dbName: String, key: String, obj: T) {
    val stringValue = StandardJson.encodeToString(obj)
    this.put(dbName, key, stringValue)
}

inline fun <reified T> Lmdb.get(dbName: String, key: String): T? {
    val stringValue = this.get(dbName, key)
    return stringValue?.let {
        try {
            StandardJson.decodeFromString<T>(it)
        } catch (e: Exception) {
            println("LMDB Deserialization error for key $key: ${e.message}")
            null
        }
    }
}

fun Lmdb.set(dbName: String, key: String, value: String) = this.put(dbName, key, value)

fun Lmdb.get(dbName: String, key: String): String? = this.get(dbName, key)