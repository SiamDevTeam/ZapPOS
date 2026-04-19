package org.siamdev.zappos.data.source.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class NostrClient(
    block: suspend NostrClient.() -> Unit
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val client = HttpClient {
        install(WebSockets)
    }

    private val relayUrls = setOf(
        "wss://relay.damus.io",
        "wss://nos.lol",
        "wss://relay.notoshi.win",
        "wss://nostr.mom",
        "wss://relay.snort.social",
        "wss://nostr-01.yakihonne.com",
        "wss://nostr.topeth.info",
        "wss://fenrir-s.notoshi.win"
    )

    private val pool = RelayPool(relayUrls, client, scope)

    init {
        scope.launch {
            pool.connectAll()
            block()
        }
    }

    suspend fun fetch(req: String) {
        pool.broadcast(req)
    }

    suspend fun Event(event: String) {
        pool.broadcast(event)
    }

    // 🔥 listen response
    fun onMessage(handler: (String) -> Unit) {
        scope.launch {
            pool.messages.collect {
                handler(it)
            }
        }
    }

    fun close() {
        pool.disconnectAll()
        scope.cancel()
    }
}