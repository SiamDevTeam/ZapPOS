package org.siamdev.zappos.data.source.remote

import rust.nostr.sdk.Client
import rust.nostr.sdk.RelayUrl
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object ConnectionPool {
    private val mutex = Mutex()
    private val pool = mutableMapOf<String, Client>()

    private val defaultRelays = listOf(
        "wss://relay.damus.io",
        "wss://nos.lol",
        "wss://relay.notoshi.win",
        "wss://nostr.mom",
        "wss://relay.snort.social",
        "wss://nostr-01.yakihonne.com",
        "wss://nostr.topeth.info"
    )

    suspend fun initialized() {
        mutex.withLock {
            defaultRelays.forEach { urlStr ->
                if (!pool.containsKey(urlStr)) {
                    val client = Client()
                    try {
                        println("Adding relay: $urlStr")
                        client.automaticAuthentication(false)
                        client.addRelay(RelayUrl.parse(urlStr))
                        client.addWriteRelay(RelayUrl.parse(urlStr))
                        pool[urlStr] = client
                    } catch (e: Exception) {
                        println("Invalid relay: $urlStr -> ${e.message}")
                    }
                }
            }

            pool.values.forEach { it.connect() }
        }
    }

    suspend fun getConnection(): Client {
        mutex.withLock {
            if (pool.isEmpty()) throw IllegalStateException("ConnectionPool not initialized")
            return pool.values.first()
        }
    }

    suspend fun closeAll() {
        mutex.withLock {
            pool.values.forEach { it.destroy() }
            pool.clear()
        }
    }
}
