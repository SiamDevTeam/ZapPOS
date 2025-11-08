/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDev by SiamDharmar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        ): String = nip04Encrypt(secretKey.sk, publicKey.pk, content)

        actual fun NIP04Decrypt(
            secretKey: NostrSecretKey,
            publicKey: NostrPublicKey,
            content: String
        ): String = nip04Decrypt(secretKey.sk, publicKey.pk, content)

        actual fun NIP44Encrypt(
            secretKey: NostrSecretKey,
            publicKey: NostrPublicKey,
            content: String,
            version: NIP44VERSION
        ): String = nip44Encrypt(secretKey.sk, publicKey.pk, content, version.version)

        actual fun NIP44Decrypt(
            secretKey: NostrSecretKey,
            publicKey: NostrPublicKey,
            content: String
        ): String = nip44Decrypt(secretKey.sk, publicKey.pk, content)

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