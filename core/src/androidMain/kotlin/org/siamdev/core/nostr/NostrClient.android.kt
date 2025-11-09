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

/*
package org.siamdev.core.nostr

import rust.nostr.sdk.Client
import rust.nostr.sdk.ClientMessage
import rust.nostr.sdk.Event
import rust.nostr.sdk.Events
import rust.nostr.sdk.EventBuilder
import rust.nostr.sdk.Filter
import rust.nostr.sdk.HandleNotification
import rust.nostr.sdk.NostrDatabase
import rust.nostr.sdk.PublicKey
import rust.nostr.sdk.RelayOptions
import rust.nostr.sdk.RelayUrl
import rust.nostr.sdk.Tag
import rust.nostr.sdk.UnsignedEvent

actual class NostrClient internal constructor(
    internal val client: Client
) {

    suspend fun addDiscoveryRelay(url: RelayUrl) = runCatching { client.addDiscoveryRelay(url) }
    suspend fun addReadRelay(url: RelayUrl) = runCatching { client.addReadRelay(url) }
    suspend fun addRelay(url: RelayUrl) = runCatching { client.addRelay(url) }
    suspend fun addRelayWithOpts(url: RelayUrl, opts: RelayOptions) = runCatching { client.addRelayWithOpts(url, opts) }
    suspend fun addWriteRelay(url: RelayUrl) = runCatching { client.addWriteRelay(url) }
    fun automaticAuthentication(enable: Boolean) = client.automaticAuthentication(enable)
    suspend fun connect() = runCatching { client.connect() }
    suspend fun connectRelay(url: RelayUrl) = runCatching { client.connectRelay(url) }
    fun database(): NostrDatabase = client.database()
    suspend fun disconnect() = runCatching { client.disconnect() }
    suspend fun disconnectRelay(url: RelayUrl) = runCatching { client.disconnectRelay(url) }
    suspend fun fetchCombinedEvents(filter: Filter, timeout: java.time.Duration) = runCatching { client.fetchCombinedEvents(
        filter: Filter, timeout) }
    suspend fun fetchEvents(filter: Filter, timeout: java.time.Duration) = runCatching { client.fetchEvents(
        filter: Filter, timeout) }
    suspend fun fetchEventsFrom(urls: List<RelayUrl>, filter: Filter, timeout: java.time.Duration) = runCatching { client.fetchEventsFrom(urls,
        filter: Filter, timeout) }
    suspend fun fetchMetadata(publicKey: PublicKey, timeout: java.time.Duration) = runCatching { client.fetchMetadata(publicKey, timeout) }
    actual suspend fun forceRemoveAllRelays() = runCatching { client.forceRemoveAllRelays() }
    suspend fun forceRemoveRelay(url: RelayUrl) = runCatching { client.forceRemoveRelay(url) }
    suspend fun giftWrap(receiver: PublicKey, rumor: UnsignedEvent, extraTags: List<Tag>) = runCatching { client.giftWrap(receiver, rumor, extraTags) }
    suspend fun giftWrapTo(urls: List<RelayUrl>, receiver: PublicKey, rumor: UnsignedEvent, extraTags: List<Tag>) = runCatching { client.giftWrapTo(urls, receiver, rumor, extraTags) }
    suspend fun handleNotifications(handler: HandleNotification) = runCatching { client.handleNotifications(handler) }
    suspend fun relay(url: RelayUrl) = runCatching { client.relay(url) }
    actual suspend fun relays() = runCatching { client.relays() }
    actual suspend fun removeAllRelays() = runCatching { client.removeAllRelays() }
    suspend fun removeRelay(url: RelayUrl) = runCatching { client.removeRelay(url) }
    suspend fun sendEvent(event: Event) = runCatching { client.sendEvent(event) }
    suspend fun sendEventBuilder(builder: EventBuilder) = runCatching { client.sendEventBuilder(builder) }
    suspend fun sendEventBuilderTo(urls: List<RelayUrl>, builder: EventBuilder) = runCatching { client.sendEventBuilderTo(urls, builder) }
    suspend fun sendEventTo(urls: List<RelayUrl>, event: Event) = runCatching { client.sendEventTo(urls, event) }
    suspend fun sendMsgTo(urls: List<RelayUrl>, msg: ClientMessage) = runCatching { client.sendMsgTo(urls, msg) }
    suspend fun sendPrivateMsg(receiver: PublicKey, message: String, rumorExtraTags: List<Tag>) = runCatching { client.sendPrivateMsg(receiver, message, rumorExtraTags) }
    suspend fun sendPrivateMsgTo(urls: List<RelayUrl>, receiver: PublicKey, message: String, rumorExtraTags: List<Tag>) = runCatching { client.sendPrivateMsgTo(urls, receiver, message, rumorExtraTags) }
    suspend fun setMetadata(metadata: Metadata) = runCatching { client.setMetadata(metadata) }
    suspend fun shutdown() = runCatching { client.shutdown() }
    suspend fun signEventBuilder(builder: EventBuilder) = runCatching { client.signEventBuilder(builder) }
    suspend fun signer() = runCatching { client.signer() }
}

* */