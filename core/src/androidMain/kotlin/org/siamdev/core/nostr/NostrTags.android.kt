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

import org.siamdev.core.nostr.keys.NostrPublicKey
import rust.nostr.sdk.Tags

actual class NostrTags(internal val tags: Tags) {

    actual companion object {
        actual fun fromList(list: List<TagElt>): NostrTags {
            val rustList = list.map { it.tag }
            return NostrTags(Tags.fromList(rustList))
        }

        actual fun parse(tags: List<List<String>>): NostrTags = NostrTags(Tags.parse(tags))
    }

    actual fun isEmpty(): Boolean = tags.isEmpty()

    actual fun hashtags(): List<String> = tags.hashtags()

    actual fun identifier(): String? = tags.identifier()

    actual fun len(): ULong = tags.len()

    actual fun publicKeys(): List<NostrPublicKey> {
        return tags.publicKeys().map { NostrPublicKey(it) }
    }

    actual fun first(): TagElt? = tags.first()?.let { TagElt(it) }

    actual fun toVec(): List<TagElt> = tags.toVec().map { TagElt(it) }
}
