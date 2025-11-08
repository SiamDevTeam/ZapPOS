package org.siamdev.core.nostr.keys

expect class NostrSigner {
    companion object {
        fun keys(key: NostrKeys): NostrSigner
    }
    fun getPublicKey(): NostrPublicKey
}