package org.siamdev.core.nostr.keys


actual class NostrSigner private constructor(private val keys: NostrKeys) {
    actual companion object {
        actual fun keys(key: NostrKeys): NostrSigner = NostrSigner(key)
    }
    actual fun getPublicKey(): NostrPublicKey = keys.publicKey()
}