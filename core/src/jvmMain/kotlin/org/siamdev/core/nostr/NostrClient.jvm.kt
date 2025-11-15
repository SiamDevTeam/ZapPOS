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
package org.siamdev.core.nostr

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.siamdev.core.nostr.keys.NostrPublicKey
import org.siamdev.core.nostr.keys.NostrSigner
import rust.nostr.sdk.PublicKey
import java.time.Duration
import rust.nostr.sdk.Client as NativeClient
import rust.nostr.sdk.RelayUrl as NativeRelayUrl
import rust.nostr.sdk.RelayOptions as NativeRelayOptions
import rust.nostr.sdk.Event as NativeEvent
import rust.nostr.sdk.EventBuilder as NativeEventBuilder
import rust.nostr.sdk.ClientMessage as NativeClientMessage

actual class NostrClient internal constructor(
    internal val client: NativeClient
) {

    actual fun unwrap(): Any = client

    actual suspend fun addDiscoveryRelay(url: RelayUrl): Boolean =
        withContext(Dispatchers.IO) { client.addDiscoveryRelay(url.unwrap() as NativeRelayUrl) }

    actual suspend fun addReadRelay(url: RelayUrl): Boolean =
        withContext(Dispatchers.IO) { client.addReadRelay(url.unwrap() as NativeRelayUrl) }

    actual suspend fun addRelay(url: RelayUrl): Boolean =
        withContext(Dispatchers.IO) { client.addRelay(url.unwrap() as NativeRelayUrl) }

    actual suspend fun addRelayWithOpts(url: RelayUrl, opts: NostrRelayOptions): Boolean =
        withContext(Dispatchers.IO) { client.addRelayWithOpts(
            url.unwrap() as NativeRelayUrl,
            opts.unwrap() as NativeRelayOptions
        ) }

    actual suspend fun addWriteRelay(url: RelayUrl): Boolean =
        withContext(Dispatchers.IO) { client.addWriteRelay(url.unwrap() as NativeRelayUrl) }

    actual fun automaticAuthentication(enable: Boolean) =
        client.automaticAuthentication(enable)

    actual suspend fun connect() =
        withContext(Dispatchers.IO) { client.connect() }

    actual suspend fun connectRelay(url: RelayUrl) =
        withContext(Dispatchers.IO) { client.connectRelay(url.unwrap() as NativeRelayUrl) }

    actual suspend fun disconnect() =
        withContext(Dispatchers.IO) { client.disconnect() }

    actual suspend fun disconnectRelay(url: RelayUrl) =
        withContext(Dispatchers.IO) { client.disconnectRelay(url.unwrap() as NativeRelayUrl) }

    actual suspend fun fetchCombinedEvents(filter: NostrFilter, timeout: NostrDuration): Result<NostrEvents> =
        runCatching { NostrEvents(client.fetchCombinedEvents(filter.native,
            timeout.unwrap() as Duration
        )) }

    actual suspend fun fetchEvents(filter: NostrFilter, timeout: NostrDuration): NostrEvents =
        NostrEvents(client.fetchEvents(filter.native, timeout.unwrap() as Duration))

    actual suspend fun fetchEventsFrom(urls: List<RelayUrl>, filter: NostrFilter, timeout: NostrDuration): NostrEvents =
        NostrEvents(client.fetchEventsFrom(
            urls.map { it.unwrap() } as List<NativeRelayUrl>,
            filter.native,
            timeout.unwrap() as Duration
        ))

    actual suspend fun forceRemoveAllRelays() =
        withContext(Dispatchers.IO) { client.forceRemoveAllRelays() }

    actual suspend fun forceRemoveRelay(url: RelayUrl) =
        withContext(Dispatchers.IO) { client.forceRemoveRelay(url.unwrap() as NativeRelayUrl) }

    actual suspend fun relay(url: RelayUrl): NostrRelay =
        NostrRelay(client.relay(url.unwrap() as NativeRelayUrl))

    actual suspend fun relays(): Map<RelayUrl, NostrRelay> =
        client.relays().mapKeys { RelayUrl(it.key) }.mapValues { NostrRelay(it.value) }

    actual suspend fun removeAllRelays() =
        withContext(Dispatchers.IO) { client.removeAllRelays() }

    actual suspend fun removeRelay(url: RelayUrl) =
        withContext(Dispatchers.IO) { client.removeRelay(url.unwrap() as NativeRelayUrl) }

    actual suspend fun sendEvent(event: NostrEvent): NostrSendEventOutput =
        NostrSendEventOutput(client.sendEvent(event.unwrap() as NativeEvent))

    actual suspend fun sendEventBuilder(builder: NostrEventBuilder): NostrSendEventOutput =
        NostrSendEventOutput(client.sendEventBuilder(builder.unwrap() as NativeEventBuilder))

    actual suspend fun sendEventBuilderTo(urls: List<RelayUrl>, builder: NostrEventBuilder): NostrSendEventOutput =
        NostrSendEventOutput(client.sendEventBuilderTo(
            urls.map { it.unwrap() } as List<NativeRelayUrl>,
            builder.unwrap() as NativeEventBuilder)
        )

    actual suspend fun sendEventTo(urls: List<RelayUrl>, event: NostrEvent): NostrSendEventOutput =
        NostrSendEventOutput(client.sendEventTo(urls.map { it.unwrap() } as List<NativeRelayUrl>,
            event.unwrap() as NativeEvent
        ))

    actual suspend fun sendMsgTo(urls: List<RelayUrl>, msg: NostrClientMessage): NostrOutput =
        NostrOutput(client.sendMsgTo(urls.map { it.unwrap() } as List<NativeRelayUrl>, msg.unwrap() as NativeClientMessage))

    actual suspend fun sendPrivateMsg(receiver: NostrPublicKey, message: String, rumorExtraTags: List<NostrTag>): NostrSendEventOutput =
        NostrSendEventOutput(client.sendPrivateMsg(
            receiver.unwrap() as PublicKey,
            message,
            rumorExtraTags.map { it.native })
        )

    actual suspend fun sendPrivateMsgTo(urls: List<RelayUrl>, receiver: NostrPublicKey, message: String, rumorExtraTags: List<NostrTag>): NostrSendEventOutput =
        NostrSendEventOutput(
            client.sendPrivateMsgTo(urls.map { it.unwrap() } as List<NativeRelayUrl>,
                receiver.unwrap() as PublicKey,
                message,
                rumorExtraTags.map { it.native })
        )

    actual suspend fun shutdown() =
        withContext(Dispatchers.IO) { client.shutdown() }

    actual suspend fun signEventBuilder(builder: NostrEventBuilder): NostrEvent =
        NostrEvent(client.signEventBuilder(builder.unwrap() as NativeEventBuilder))

    actual suspend fun signer(): NostrSigner =
        NostrSigner(client.signer())
}
