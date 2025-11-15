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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.siamdev.core.nostr.NostrEvent
import org.siamdev.core.nostr.NostrUnsignedEvent
import rust.nostr.sdk.PublicKey
import rust.nostr.sdk.NostrSigner as NativeSigner

actual class NostrSigner internal constructor(
    internal val native: NativeSigner
) {

    actual suspend fun getPublicKey(): Result<NostrPublicKey> =
        runCatching {
            val pub = withContext(Dispatchers.IO) { native.getPublicKey() }
            NostrPublicKey(pub)
        }

    actual suspend fun nip04Decrypt(
        publicKey: NostrPublicKey,
        encryptedContent: String
    ): Result<String> = runCatching {
        withContext(Dispatchers.IO) {
            native.nip04Decrypt(publicKey.unwrap() as PublicKey, encryptedContent)
        }
    }

    actual suspend fun nip04Encrypt(
        publicKey: NostrPublicKey,
        content: String
    ): Result<String> = runCatching {
        withContext(Dispatchers.IO) {
            native.nip04Encrypt(publicKey.unwrap() as PublicKey, content)
        }
    }

    actual suspend fun nip44Decrypt(
        publicKey: NostrPublicKey,
        payload: String
    ): Result<String> = runCatching {
        withContext(Dispatchers.IO) {
            native.nip44Decrypt(publicKey.pk, payload)
        }
    }

    actual suspend fun nip44Encrypt(
        publicKey: NostrPublicKey,
        content: String
    ): Result<String> = runCatching {
        withContext(Dispatchers.IO) {
            native.nip44Encrypt(publicKey.unwrap() as PublicKey, content)
        }
    }

    actual suspend fun signEvent(unsignedEvent: NostrUnsignedEvent): Result<NostrEvent> =
        runCatching {
            val signed = withContext(Dispatchers.IO) {
                native.signEvent(unsignedEvent.native)
            }
            NostrEvent(signed)
        }

    actual fun unwrap(): Any = native
}