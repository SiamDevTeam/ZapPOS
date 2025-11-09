package org.siamdev.core.nostr

import org.siamdev.core.nostr.keys.NostrPublicKey

expect class NostrCoordinate {
    val identifier: String
    val kind: NostrKind
    val publicKey: NostrPublicKey

    fun verify(): Boolean

    companion object {
        @Throws(Exception::class)
        fun parse(coordinate: String): NostrCoordinate
    }
}
