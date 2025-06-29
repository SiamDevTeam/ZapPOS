package org.siamdev.zappos.keys

import rust.nostr.sdk.Keys
import rust.nostr.sdk.PublicKey
import rust.nostr.sdk.SecretKey



actual class NostrKeys private constructor(private val keys: Keys) {
    actual companion object {
        actual fun generate(): NostrKeys = NostrKeys(Keys.generate())
        actual fun parse(secretKey: String): NostrKeys = NostrKeys(Keys.parse(secretKey))
    }

    actual fun secretKey(): NostrSecretKey = NostrSecretKey(keys.secretKey())
    actual fun publicKey(): NostrPublicKey = NostrPublicKey(keys.publicKey())
    actual fun sign(msg: String): String = keys.signSchnorr(msg.hexToByteArray())
}

actual class NostrPublicKey internal constructor(private val pk: PublicKey) {
    actual fun toHex(): String = pk.toHex()
    actual fun toBech32(): String = pk.toBech32()
    actual fun toNostrUri(): String = pk.toNostrUri()
}

actual class NostrSecretKey internal constructor(private val sk: SecretKey) {
    actual fun toHex(): String = sk.toHex()
    actual fun toBech32(): String = sk.toBech32()
}