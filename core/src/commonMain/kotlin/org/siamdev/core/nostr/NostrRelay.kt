package org.siamdev.core.nostr

import org.siamdev.core.nostr.types.NostrRelayStatus

expect class NostrRelay {

    fun ban()
    fun batchMsg(msgs: List<NostrClientMessage>)
    fun connect()
    fun connectionMode(): NostrConnectionMode

    suspend fun countEvents(filter: NostrFilter, timeout: NostrDuration): Result<ULong>

    fun disconnect()
    suspend fun fetchEvents(
        filter: NostrFilter,
        timeout: NostrDuration,
        policy: NostrReqExitPolicy
    ): Result<NostrEvents>

    fun isConnected(): Boolean
    fun queue(): ULong

    suspend fun sendEvent(event: NostrEvent): Result<NostrEventId>

    fun sendMsg(msg: NostrClientMessage)
    fun stats(): NostrRelayConnectionStats
    fun status(): NostrRelayStatus

    suspend fun subscribe(filter: NostrFilter, opts: NostrSubscribeOptions): Result<String>
    suspend fun subscribeWithId(id: String, filter: NostrFilter, opts: NostrSubscribeOptions): Result<Unit>
    suspend fun subscription(id: String): Result<NostrFilter?>
    suspend fun subscriptions(): Result<Map<String, NostrFilter>>
    suspend fun sync(filter: NostrFilter, opts: NostrSyncOptions): Result<NostrReconciliation>
    suspend fun syncWithItems(filter: NostrFilter, items: List<NostrNegentropyItem>, opts: NostrSyncOptions): Result<NostrReconciliation>
    suspend fun tryConnect(timeout: NostrDuration): Result<Unit>
    suspend fun unsubscribe(id: String): Result<Unit>
    suspend fun unsubscribeAll(): Result<Unit>

    fun url(): RelayUrl
}
