package org.siamdev.zappos.keys

expect class NostrKeys {
    companion object {
        fun generate(): NostrKeys
        fun parse(secretKey: String): NostrKeys
    }

    fun secretKey(): NostrSecretKey
    fun publicKey(): NostrPublicKey

}


expect class NostrPublicKey {
    fun toHex(): String
    fun toBech32(): String
    fun toNostrUri(): String
}

expect class NostrSecretKey {
    fun toHex(): String
    fun toBech32(): String
}