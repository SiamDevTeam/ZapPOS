package org.siamdev.core.nostr

import android.os.Build
import androidx.annotation.RequiresApi
import rust.nostr.sdk.Relay as NativeRelay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.siamdev.core.nostr.types.NostrRelayStatus
import rust.nostr.sdk.ClientMessage
import rust.nostr.sdk.Event
import rust.nostr.sdk.SubscribeOptions
import rust.nostr.sdk.SyncOptions
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
actual class NostrRelay internal constructor(
    internal val native: NativeRelay
) {

    actual fun ban() = native.ban()
    actual fun batchMsg(msgs: List<NostrClientMessage>) =
        native.batchMsg(msgs.map { it.unwrap() } as List<ClientMessage>)

    actual fun connect() = native.connect()
    actual fun connectionMode(): NostrConnectionMode =
        NostrConnectionMode.fromNative(native.connectionMode())

    actual suspend fun countEvents(filter: NostrFilter, timeout: NostrDuration): Result<ULong> =
        runCatching {
            withContext(Dispatchers.IO) {
                native.countEvents(filter.native, timeout.unwrap() as Duration)
            }
        }

    actual fun disconnect() = native.disconnect()

    actual suspend fun fetchEvents(
        filter: NostrFilter,
        timeout: NostrDuration,
        policy: NostrReqExitPolicy
    ): Result<NostrEvents> = runCatching {
        withContext(Dispatchers.IO) {
            NostrEvents(native.fetchEvents(filter.native, timeout.unwrap() as Duration, policy.unwrap()))
        }
    }

    actual fun isConnected(): Boolean = native.isConnected()
    actual fun queue(): ULong = native.queue()

    actual suspend fun sendEvent(event: NostrEvent): Result<NostrEventId> =
        runCatching { withContext(Dispatchers.IO) { NostrEventId(native.sendEvent(event.unwrap() as Event)) } }

    actual fun sendMsg(msg: NostrClientMessage) = native.sendMsg(msg.unwrap() as ClientMessage)
    actual fun stats(): NostrRelayConnectionStats = NostrRelayConnectionStats(native.stats())
    actual fun status(): NostrRelayStatus =
        NostrRelayStatus.of(native.status())


    actual suspend fun subscribe(filter: NostrFilter, opts: NostrSubscribeOptions): Result<String> =
        runCatching { withContext(Dispatchers.IO) { native.subscribe(filter.native,
            opts.unwrap() as SubscribeOptions
        ) } }

    actual suspend fun subscribeWithId(id: String, filter: NostrFilter, opts: NostrSubscribeOptions): Result<Unit> =
        runCatching { withContext(Dispatchers.IO) { native.subscribeWithId(id, filter.native,
            opts.unwrap() as SubscribeOptions
        ) } }

    actual suspend fun subscription(id: String): Result<NostrFilter?> =
        runCatching {
            withContext(Dispatchers.IO) {
                native.subscription(id)?.let { NostrFilter(it) }
            }
        }

    actual suspend fun subscriptions(): Result<Map<String, NostrFilter>> =
        runCatching {
            withContext(Dispatchers.IO) {
                native.subscriptions().mapValues { NostrFilter(it.value) }
            }
        }

    actual suspend fun sync(filter: NostrFilter, opts: NostrSyncOptions): Result<NostrReconciliation> =
        runCatching { withContext(Dispatchers.IO) { NostrReconciliation(native.sync(filter.native,
            opts.unwrap() as SyncOptions
        )) } }

    actual suspend fun syncWithItems(filter: NostrFilter, items: List<NostrNegentropyItem>, opts: NostrSyncOptions): Result<NostrReconciliation> =
        runCatching {
            withContext(Dispatchers.IO) {
                NostrReconciliation(native.syncWithItems(filter.native, items.map { it.native },
                    opts.unwrap() as SyncOptions
                ))
            }
        }

    actual suspend fun tryConnect(timeout: NostrDuration): Result<Unit> =
        runCatching { withContext(Dispatchers.IO) { native.tryConnect(timeout.unwrap() as Duration ) } }

    actual suspend fun unsubscribe(id: String): Result<Unit> =
        runCatching { withContext(Dispatchers.IO) { native.unsubscribe(id) } }

    actual suspend fun unsubscribeAll(): Result<Unit> =
        runCatching { withContext(Dispatchers.IO) { native.unsubscribeAll() } }

    actual fun url(): RelayUrl = RelayUrl(native.url())
}
