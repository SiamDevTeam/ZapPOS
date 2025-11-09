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

import rust.nostr.sdk.FilterRecord
import rust.nostr.sdk.GenericTag

actual class NostrFilterRecord internal constructor(
    internal val native: FilterRecord
) {

    actual val ids: List<String>?
        get() = native.ids?.map { it.toHex() }

    actual val authors: List<String>?
        get() = native.authors?.map { it.toHex() }

    actual val kinds: List<UShort>?
        get() = native.kinds?.map { it.asU16() }

    actual val search: String?
        get() = native.search

    actual val since: ULong?
        get() = native.since?.asSecs()

    actual val until: ULong?
        get() = native.until?.asSecs()

    actual val limit: ULong?
        get() = native.limit

    actual val genericTags: List<NostrGenericTag>
        get() = native.genericTags.map { NostrGenericTag(it) }

}

actual class NostrGenericTag internal constructor(
    internal val native: GenericTag
) {

    actual val key: NostrSingleLetterTag
        get() = NostrSingleLetterTag(native.key)

    actual val value: List<String>
        get() = native.value

}
