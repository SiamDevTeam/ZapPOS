package org.siamdev.zappos.nostr.keys


expect class NostrKeys {
    companion object {
        fun generate(): NostrKeys
        fun parse(secretKey: String): NostrKeys
        fun getSharedKey(secretKey: NostrSecretKey, publicKey: NostrPublicKey): NostrKeys
        fun NIP04Encrypt(secretKey: NostrSecretKey, publicKey: NostrPublicKey, content: String): String
        fun NIP04Decrypt(secretKey: NostrSecretKey, publicKey: NostrPublicKey, content: String): String

        fun NIP44Encrypt(secretKey: NostrSecretKey, publicKey: NostrPublicKey, content: String, version: NIP44VERSION): String
        fun NIP44Decrypt(secretKey: NostrSecretKey, publicKey: NostrPublicKey, content: String): String
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

expect enum class NIP44VERSION {
    V2
}