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
package org.siamdev.core.nostr

import rust.nostr.sdk.EventBuilder as NativeEventBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.siamdev.core.nostr.keys.NostrPublicKey

actual class NostrEventBuilder internal constructor(
    internal val native: NativeEventBuilder
) {
    actual fun allowSelfTagging(): NostrEventBuilder {
        native.allowSelfTagging()
        return this
    }

    actual fun build(publicKey: NostrPublicKey): NostrUnsignedEvent =
        NostrUnsignedEvent(native.build(publicKey.unwrap()))

    actual fun customCreatedAt(createdAt: NostrTimestamp): NostrEventBuilder {
        native.customCreatedAt(createdAt.unwrap())
        return this
    }

    actual fun dedupTags(): NostrEventBuilder {
        native.dedupTags()
        return this
    }

    actual fun pow(difficulty: UByte): NostrEventBuilder {
        native.pow(difficulty)
        return this
    }

    actual fun tags(tags: List<NostrTag>): NostrEventBuilder {
        native.tags(tags.map { it.unwrap() })
        return this
    }

    actual suspend fun sign(signer: NostrSigner): Result<NostrEvent> = runCatching {
        withContext(Dispatchers.IO) {
            NostrEvent(native.sign(signer.unwrap()))
        }
    }

    actual fun signWithKeys(keys: NostrKeys): NostrEvent =
        NostrEvent(native.signWithKeys(keys.unwrap()))
}
