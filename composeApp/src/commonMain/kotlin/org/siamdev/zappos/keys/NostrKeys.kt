package org.siamdev.zappos.keys

expect class NostrKeys {
    companion object {
        fun generate(): NostrKeys
        fun parse(secretKey: String): NostrKeys
        fun getSharedKey(secretKey: NostrSecretKey, publicKey: NostrPublicKey): NostrKeys
    }

    fun secretKey(): NostrSecretKey
    fun publicKey(): NostrPublicKey

    fun sign(msg: String): String

}


expect class NostrPublicKey {
    fun toHex(): String
    fun toBech32(): String
    fun toNostrUri(): String
}

expect class NostrSecretKey {
    companion object {
        fun fromBytes(bytes: ByteArray): NostrSecretKey
    }
    fun toHex(): String
    fun toBech32(): String
}