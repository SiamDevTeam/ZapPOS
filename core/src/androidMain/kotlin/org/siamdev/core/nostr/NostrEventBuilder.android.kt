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

import android.os.Build
import androidx.annotation.RequiresApi
import rust.nostr.sdk.EventBuilder as NativeEventBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.siamdev.core.nostr.keys.NostrKeys
import org.siamdev.core.nostr.keys.NostrPublicKey
import org.siamdev.core.nostr.keys.NostrSigner
import rust.nostr.sdk.Keys
import rust.nostr.sdk.PublicKey
import rust.nostr.sdk.Tag
import rust.nostr.sdk.Timestamp


@RequiresApi(Build.VERSION_CODES.O)
actual class NostrEventBuilder internal constructor(
    internal val native: NativeEventBuilder
) {
    actual fun allowSelfTagging(): NostrEventBuilder =
        NostrEventBuilder(native.allowSelfTagging())

    actual fun build(publicKey: NostrPublicKey): NostrUnsignedEvent =
        NostrUnsignedEvent(native.build(publicKey.unwrap() as PublicKey))

    actual fun customCreatedAt(createdAt: NostrTimestamp): NostrEventBuilder =
        NostrEventBuilder(native.customCreatedAt(createdAt.unwrap() as Timestamp))

    actual fun dedupTags(): NostrEventBuilder =
        NostrEventBuilder(native.dedupTags())

    actual fun pow(difficulty: UByte): NostrEventBuilder =
        NostrEventBuilder(native.pow(difficulty))

    actual fun tags(tags: List<NostrTag>): NostrEventBuilder =
        NostrEventBuilder(native.tags(tags.map { it.unwrap() } as List<Tag>))

    actual suspend fun sign(signer: NostrSigner): Result<NostrEvent> = runCatching {
        withContext(Dispatchers.IO) {
            NostrEvent(native.sign(signer.unwrap() as rust.nostr.sdk.NostrSigner))
        }
    }

    actual fun signWithKeys(keys: NostrKeys): NostrEvent =
        NostrEvent(native.signWithKeys(keys.unwrap() as Keys))

    actual fun unwrap(): Any = native
}
