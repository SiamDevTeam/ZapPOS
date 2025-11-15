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

import rust.nostr.sdk.Event

actual class NostrEvent(
    internal val event: Event
) {

    actual companion object {
        actual fun fromJson(json: String): NostrEvent {
            val event = Event.fromJson(json)
            return NostrEvent(event)
        }
    }

    actual fun toJson(): String = event.asJson()

    actual val id: String
        get() = event.id().toHex()

    actual val pubkey: String
        get() = event.author().toHex()

    actual val createdAt: ULong
        get() = event.createdAt().asSecs()

    actual val kind: UShort
        get() = event.kind().asU16()

    actual val content: String
        get() = event.content()

    actual val sig: String
        get() = event.signature()

    actual val tags: NostrTags
        get() = NostrTags(event.tags())


    actual fun hashtags(): List<String> = event.tags().hashtags()

    actual fun taggedPublicKeys(): List<String> = event.tags().publicKeys().map { it.toHex() }

    actual fun taggedEventIds(): List<String> = event.tags().eventIds().map { it.toHex() }

    actual fun identifier(): String? = event.tags().identifier()
}
