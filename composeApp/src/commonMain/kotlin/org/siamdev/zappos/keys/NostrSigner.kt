package org.siamdev.zappos.keys

expect class NostrSigner {
    companion object {
        fun keys(key: NostrKeys): NostrSigner
    }
    fun getPublicKey(): NostrPublicKey
}