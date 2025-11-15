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

import rust.nostr.sdk.Events as NativeEvents

actual class NostrEvents internal constructor(
    internal val native: NativeEvents
) {

    actual fun contains(event: NostrEvent): Boolean =
        native.contains(event.event)

    actual fun first(): NostrEvent? =
        native.first()?.let { NostrEvent(it) }

    actual fun isEmpty(): Boolean = native.isEmpty()

    actual fun len(): ULong = native.len()

    @Throws(Exception::class)
    actual fun merge(other: NostrEvents): NostrEvents =
        NostrEvents(native.merge(other.native))

    @Throws(Exception::class)
    actual fun toList(): List<NostrEvent> =
        native.toVec().map { NostrEvent(it) }
}
