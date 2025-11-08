package org.siamdev.core.nostr

import rust.nostr.sdk.Event

actual class NostrEvent private constructor(
    internal val event: Event
) {

    actual companion object {
        actual fun fromJson(json: String): NostrEvent {
            val event = Event.fromJson(json)
            return NostrEvent(event)
        }
    }

    actual fun toJson(): String {
        return event.asJson()
    }

    actual val id: String
        get() = event.id().toHex()

    actual val pubkey: String
        get() = event.author().toHex()

    actual val createdAt: ULong
        get() = event.createdAt().asSecs()

    actual val kind: UShort
        get() = event.kind().asU16()

    actual val content: String
        get() = event.content()

    actual val sig: String
        get() = event.signature()

    actual val tags: NostrTags
        get() = NostrTags(event.tags())


    actual fun hashtags(): List<String> = event.tags().hashtags()

    actual fun taggedPublicKeys(): List<String> = event.tags().publicKeys().map { it.toHex() }

    actual fun taggedEventIds(): List<String> = event.tags().eventIds().map { it.toHex() }

    actual fun identifier(): String? = event.tags().identifier()
}
