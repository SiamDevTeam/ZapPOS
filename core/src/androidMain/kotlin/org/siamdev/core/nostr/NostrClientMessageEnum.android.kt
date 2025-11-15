package org.siamdev.core.nostr

import rust.nostr.sdk.Event
import rust.nostr.sdk.ClientMessageEnum as NativeClientMessageEnum

actual sealed class NostrClientMessageEnum {

    actual class EventMsg internal constructor(
        internal val native: NativeClientMessageEnum.EventMsg
    ) : NostrClientMessageEnum() {

        actual constructor(event: NostrEvent) : this(
            NativeClientMessageEnum.EventMsg(event.unwrap() as Event)
        )

        actual val event: NostrEvent
            get() = NostrEvent(native.event)
    }


    actual class Req internal constructor(
        internal val native: NativeClientMessageEnum.Req
    ) : NostrClientMessageEnum() {

        actual constructor(subscriptionId: String, filter: NostrFilter) : this(
            NativeClientMessageEnum.Req(subscriptionId, filter.native)
        )

        actual val subscriptionId: String
            get() = native.subscriptionId

        actual val filter: NostrFilter
            get() = NostrFilter(native.filter)
    }


    actual class ReqMultiFilter internal constructor(
        internal val native: NativeClientMessageEnum.ReqMultiFilter
    ) : NostrClientMessageEnum() {

        actual constructor(subscriptionId: String, filters: List<NostrFilter>) : this(
            NativeClientMessageEnum.ReqMultiFilter(subscriptionId, filters.map { it.native })
        )

        actual val subscriptionId: String
            get() = native.subscriptionId

        actual val filters: List<NostrFilter>
            get() = native.filters.map { NostrFilter(it) }
    }

    actual class Count internal constructor(
        internal val native: NativeClientMessageEnum.Count
    ) : NostrClientMessageEnum() {

        actual constructor(subscriptionId: String, filter: NostrFilter) : this(
            NativeClientMessageEnum.Count(subscriptionId, filter.native)
        )

        actual val subscriptionId: String
            get() = native.subscriptionId

        actual val filter: NostrFilter
            get() = NostrFilter(native.filter)
    }

    actual class Close internal constructor(
        internal val native: NativeClientMessageEnum.Close
    ) : NostrClientMessageEnum() {

        actual constructor(subscriptionId: String) : this(
            NativeClientMessageEnum.Close(subscriptionId)
        )

        actual val subscriptionId: String
            get() = native.subscriptionId
    }

    actual class Auth internal constructor(
        internal val native: NativeClientMessageEnum.Auth
    ) : NostrClientMessageEnum() {

        actual constructor(event: NostrEvent) : this(
            NativeClientMessageEnum.Auth(event.unwrap() as Event)
        )

        actual val event: NostrEvent
            get() = NostrEvent(native.event)
    }

    actual class NegOpen internal constructor(
        internal val native: NativeClientMessageEnum.NegOpen
    ) : NostrClientMessageEnum() {

        actual constructor(
            subscriptionId: String,
            filter: NostrFilter,
            idSize: UByte?,
            initialMessage: String
        ) : this(
            NativeClientMessageEnum.NegOpen(subscriptionId, filter.native, idSize, initialMessage)
        )

        actual val subscriptionId: String
            get() = native.subscriptionId

        actual val filter: NostrFilter
            get() = NostrFilter(native.filter)

        actual val idSize: UByte?
            get() = native.idSize

        actual val initialMessage: String
            get() = native.initialMessage
    }

    actual class NegMsg internal constructor(
        internal val native: NativeClientMessageEnum.NegMsg
    ) : NostrClientMessageEnum() {

        actual constructor(subscriptionId: String, message: String) : this(
            NativeClientMessageEnum.NegMsg(subscriptionId, message)
        )

        actual val subscriptionId: String
            get() = native.subscriptionId

        actual val message: String
            get() = native.message
    }

    actual class NegClose internal constructor(
        internal val native: NativeClientMessageEnum.NegClose
    ) : NostrClientMessageEnum() {

        actual constructor(subscriptionId: String) : this(
            NativeClientMessageEnum.NegClose(subscriptionId)
        )

        actual val subscriptionId: String
            get() = native.subscriptionId
    }

}
