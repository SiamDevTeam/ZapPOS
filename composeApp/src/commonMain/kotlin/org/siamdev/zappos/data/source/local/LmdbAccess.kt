/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

val StandardJson = Json {
    prettyPrint = false
    ignoreUnknownKeys = true
    isLenient = true
}

class LmdbTransactionBuilder(private val lmdb: LmdbConfig) {
    var name: String? = null
    var key: Any? = null
    var value: Any? = null

    fun commit() {
        val dbName = name ?: error("Database name is required")
        val k = serializeKey(key ?: error("Key is required"))
        val v = serializeValue(value ?: error("Value is required"))
        lmdb.put(dbName, k, v)
    }

    fun serializeKey(key: Any): String =
        when (key) {
            is String -> key
            else -> StandardJson.encodeToString(key)
        }

    fun serializeValue(value: Any): String =
        when (value) {
            is String -> value
            else -> StandardJson.encodeToString(value)
        }

    internal inline fun <reified T> fetch(key: Any? = this.key): T? {
        val dbName = name ?: error("Database name is required")
        val k = serializeKey(key ?: error("Key is required"))

        val stringValue = lmdb.get(dbName, k) ?: return null

        if (T::class == String::class) return stringValue as T
        return runCatching {
            StandardJson.decodeFromString<T>(stringValue)
        }.getOrElse {
            println("LMDB Deserialization error for key $k: ${it.message}")
            null
        }
    }

}

suspend inline fun transaction(
    crossinline block: suspend LmdbTransactionBuilder.() -> Unit
) = withContext(Dispatchers.IO) {
    val lmdb = LmdbConfig()
    try {
        println("Opening LMDB")
        val builder = LmdbTransactionBuilder(lmdb)
        builder.block()
        builder.commit()
    } finally {
        println("Closing LMDB")
        lmdb.close()
    }
}


internal suspend inline fun <reified T> fetch(
    crossinline block: LmdbTransactionBuilder.() -> Unit
): T? = withContext(Dispatchers.IO) {
    val lmdb = LmdbConfig()
    try {
        val builder = LmdbTransactionBuilder(lmdb)
        builder.block()
        builder.fetch<T>()
    } finally {
        lmdb.close()
    }
}
