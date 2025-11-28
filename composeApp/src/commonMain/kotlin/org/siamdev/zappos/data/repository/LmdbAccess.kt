
package org.siamdev.zappos.data.repository

import kotlinx.coroutines.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.siamdev.zappos.data.source.local.Lmdb

val StandardJson = Json {
    prettyPrint = false
    ignoreUnknownKeys = true
    isLenient = true
}

class LmdbTransactionBuilder(private val lmdb: Lmdb) {
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
    val lmdb = Lmdb()
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
    val lmdb = Lmdb()
    try {
        val builder = LmdbTransactionBuilder(lmdb)
        builder.block()
        builder.fetch<T>()
    } finally {
        lmdb.close()
    }
}
