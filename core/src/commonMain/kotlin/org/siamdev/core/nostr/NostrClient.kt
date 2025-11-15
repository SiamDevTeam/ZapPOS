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

import org.siamdev.core.nostr.keys.NostrPublicKey

expect class NostrClient {
    suspend fun addDiscoveryRelay(url: RelayUrl): Boolean
    suspend fun addReadRelay(url: RelayUrl): Boolean
    suspend fun addRelay(url: RelayUrl): Boolean
    suspend fun addRelayWithOpts(url: RelayUrl, opts: RelayOptions): Boolean
    suspend fun addWriteRelay(url: RelayUrl): Boolean
    fun automaticAuthentication(enable: Boolean)
    suspend fun connect()
    suspend fun connectRelay(url: RelayUrl)
    suspend fun disconnect()
    suspend fun disconnectRelay(url: RelayUrl)
    suspend fun fetchCombinedEvents(filter: NostrFilter, timeout: NostrDuration): Result<NostrEvents>
    suspend fun fetchEvents(filter: NostrFilter, timeout: NostrDuration): NostrEvents
    suspend fun fetchEventsFrom(urls: List<RelayUrl>, filter: NostrFilter, timeout: NostrDuration): NostrEvents
    suspend fun forceRemoveAllRelays()
    suspend fun forceRemoveRelay(url: RelayUrl)
    suspend fun relay(url: RelayUrl): Relay
    suspend fun relays(): Map<RelayUrl, Relay>
    suspend fun removeAllRelays()
    suspend fun removeRelay(url: RelayUrl)
    suspend fun sendEvent(event: NostrEvent): SendEventOutput
    suspend fun sendEventBuilder(builder: EventBuilder): SendEventOutput
    suspend fun sendEventBuilderTo(urls: List<RelayUrl>, builder: EventBuilder): SendEventOutput
    suspend fun sendEventTo(urls: List<RelayUrl>, event: Event): SendEventOutput
    suspend fun sendMsgTo(urls: List<RelayUrl>, msg: ClientMessage): Output
    suspend fun sendPrivateMsg(receiver: PublicKey, message: String, rumorExtraTags: List<Tag>): SendEventOutput
    suspend fun sendPrivateMsgTo(urls: List<RelayUrl>, receiver: PublicKey, message: String, rumorExtraTags: List<Tag>): SendEventOutput
    suspend fun setMetadata(metadata: Metadata): SendEventOutput
    suspend fun shutdown()
    suspend fun signEventBuilder(builder: EventBuilder): Event
    suspend fun signer(): NostrSigner
}
*/
