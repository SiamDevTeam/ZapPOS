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
package org.siamdev.zappos.data.source.remote

import rust.nostr.sdk.Client
import rust.nostr.sdk.RelayUrl
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object RelayConfig {

    private val client = Client()
    private var ready = false

    private val relayUrls = mutableSetOf(
        "wss://relay.damus.io",
        "wss://nos.lol",
        "wss://relay.notoshi.win",
        "wss://nostr.mom",
        "wss://relay.snort.social",
        "wss://nostr-01.yakihonne.com",
        "wss://nostr.topeth.info",
        "wss://fenrir-s.notoshi.win"
    )

    suspend fun setup(timeout: Duration = 5.seconds) {
        if (ready) return

        client.automaticAuthentication(false)

        relayUrls.forEach { url ->
            val relay = RelayUrl.parse(url)
            client.addRelay(relay)
            client.addWriteRelay(relay)
        }

        val result = client.tryConnect(timeout)

        result.success.forEach { relay ->
            println("Connected: $relay")
        }

        result.failed.forEach { (relay, err) ->
            println("Failed: $relay -> $err")
        }

        ready = true
        println("RelayConnect ready (${result.success.size} relays)")
    }

    fun getConnection(): Client {
        check(ready) { "RelayConnect not initialized. Call initialize() first." }
        return client
    }

    suspend fun addRelay(url: String) {
        val relay = RelayUrl.parse(url)
        client.addRelay(relay)
        client.addWriteRelay(relay)
        relayUrls.add(url)
        println("Added: $url")
    }

    suspend fun removeRelay(url: String) {
        val relay = RelayUrl.parse(url)
        client.removeRelay(relay)
        relayUrls.remove(url)
        println("Removed: $url")
    }

    fun close() {
        if (ready) {
            client.destroy()
            ready = false
            println("RelayConnect closed")
        }
    }

}
