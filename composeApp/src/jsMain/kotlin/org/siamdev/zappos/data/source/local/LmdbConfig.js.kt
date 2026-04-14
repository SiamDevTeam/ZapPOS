//package org.siamdev.zappos.data.source.local
//
//actual class LmdbConfig {
//
//    private val store = mutableMapOf<String, MutableMap<String, String>>()
//
//    actual fun put(dbName: String, key: String, value: String) {
//        store.getOrPut(dbName) { mutableMapOf() }[key] = value
//    }
//
//    actual fun get(dbName: String, key: String): String? =
//        store[dbName]?.get(key)
//
//    actual fun delete(dbName: String, key: String) {
//        store[dbName]?.remove(key)
//    }
//
//    actual fun exists(dbName: String, key: String): Boolean =
//        store[dbName]?.containsKey(key) == true
//
//    actual fun close() {}
//}
