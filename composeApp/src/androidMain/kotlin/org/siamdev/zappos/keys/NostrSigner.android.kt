package org.siamdev.zappos.keys


actual class NostrSigner private constructor(private val keys: NostrKeys) {
    actual companion object {
        actual fun keys(key: NostrKeys): org.siamdev.zappos.keys.NostrSigner = NostrSigner(key)
    }
    actual fun getPublicKey(): NostrPublicKey = keys.publicKey()
}