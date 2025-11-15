package org.siamdev.core.nostr

import org.siamdev.core.nostr.keys.NostrKeys
import org.siamdev.core.nostr.keys.NostrPublicKey
import org.siamdev.core.nostr.keys.NostrSigner

actual class NostrEventBuilder {
    actual fun allowSelfTagging(): NostrEventBuilder {
        TODO("Not yet implemented")
    }

    actual fun build(publicKey: NostrPublicKey): NostrUnsignedEvent {
        TODO("Not yet implemented")
    }

    actual fun customCreatedAt(createdAt: NostrTimestamp): NostrEventBuilder {
        TODO("Not yet implemented")
    }

    actual fun dedupTags(): NostrEventBuilder {
        TODO("Not yet implemented")
    }

    actual fun pow(difficulty: UByte): NostrEventBuilder {
        TODO("Not yet implemented")
    }

    actual fun tags(tags: List<NostrTag>): NostrEventBuilder {
        TODO("Not yet implemented")
    }

    actual suspend fun sign(signer: NostrSigner): Result<NostrEvent> {
        TODO("Not yet implemented")
    }

    actual fun signWithKeys(keys: NostrKeys): NostrEvent {
        TODO("Not yet implemented")
    }

    actual fun unwrap(): Any {
        TODO("Not yet implemented")
    }
}