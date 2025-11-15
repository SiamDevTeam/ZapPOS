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

    fun unwrap(): Any

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