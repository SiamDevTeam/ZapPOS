package org.siamdev.core.nostr.keys

import rust.nostr.sdk.Keys
import rust.nostr.sdk.Nip44Version
import rust.nostr.sdk.PublicKey
import rust.nostr.sdk.SecretKey
import rust.nostr.sdk.generateSharedKey
import rust.nostr.sdk.nip04Decrypt
import rust.nostr.sdk.nip44Decrypt
import rust.nostr.sdk.nip44Encrypt
import rust.nostr.sdk.nip04Encrypt

@OptIn(ExperimentalStdlibApi::class)
actual class NostrKeys private constructor(private val keys: Keys) {
    actual companion object {

        actual fun generate(): NostrKeys = NostrKeys(Keys.generate())

        actual fun parse(secretKey: String): NostrKeys = NostrKeys(Keys.parse(secretKey))

        actual fun getSharedKey(secretKey: NostrSecretKey, publicKey: NostrPublicKey): NostrKeys {
            val sharedKeyBytes = generateSharedKey(secretKey.sk, publicKey.pk)
            val sharedSecretKey = SecretKey.fromBytes(sharedKeyBytes)
            val sharedKeys = Keys.parse(sharedSecretKey.toHex())
            return NostrKeys(sharedKeys)
        }

        actual fun NIP04Encrypt(
            secretKey: NostrSecretKey,
            publicKey: NostrPublicKey,
            content: String
        ): String {
            return nip04Encrypt(secretKey.sk, publicKey.pk, content)
        }

        actual fun NIP04Decrypt(
            secretKey: NostrSecretKey,
            publicKey: NostrPublicKey,
            content: String
        ): String {
            return nip04Decrypt(secretKey.sk, publicKey.pk, content)
        }

        actual fun NIP44Encrypt(
            secretKey: NostrSecretKey,
            publicKey: NostrPublicKey,
            content: String,
            version: NIP44VERSION
        ): String {
            return nip44Encrypt(secretKey.sk, publicKey.pk, content, version.version)
        }

        actual fun NIP44Decrypt(
            secretKey: NostrSecretKey,
            publicKey: NostrPublicKey,
            content: String
        ): String {
            return nip44Decrypt(secretKey.sk, publicKey.pk, content)
        }

    }

    actual fun secretKey(): NostrSecretKey = NostrSecretKey(keys.secretKey())
    actual fun publicKey(): NostrPublicKey = NostrPublicKey(keys.publicKey())
    actual fun sign(msg: String): String = keys.signSchnorr(msg.hexToByteArray())
}

actual class NostrPublicKey internal constructor(internal val pk: PublicKey) {
    actual fun toHex(): String = pk.toHex()
    actual fun toBech32(): String = pk.toBech32()
    actual fun toNostrUri(): String = pk.toNostrUri()
}

actual class NostrSecretKey internal constructor(internal val sk: SecretKey) {
    actual companion object {
        actual fun fromBytes(bytes: ByteArray): NostrSecretKey {
            val secretKey = SecretKey.fromBytes(bytes)
            return NostrSecretKey(secretKey)
        }
    }
    actual fun toHex(): String = sk.toHex()
    actual fun toBech32(): String = sk.toBech32()
}

actual enum class NIP44VERSION(internal val version: Nip44Version) {
    V2(Nip44Version.V2)
}