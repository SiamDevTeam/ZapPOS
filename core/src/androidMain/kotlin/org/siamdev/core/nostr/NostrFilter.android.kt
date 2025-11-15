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
import rust.nostr.sdk.Filter as NativeFilter

actual class NostrFilter internal constructor(
    internal val native: NativeFilter
) {

    actual companion object {
        @Throws(Exception::class)
        actual fun fromJson(json: String): NostrFilter =
            NostrFilter(NativeFilter.fromJson(json))

        actual fun fromRecord(record: NostrFilterRecord): NostrFilter =
            NostrFilter(NativeFilter.fromRecord(record.native))
    }

    actual fun asJson(): String = native.asJson()

    actual fun asRecord(): NostrFilterRecord = NostrFilterRecord(native.asRecord())

    actual fun author(author: NostrPublicKey): NostrFilter = NostrFilter(native.author(author.pk))
    actual fun authors(authors: List<NostrPublicKey>): NostrFilter =
        NostrFilter(native.authors(authors.map { it.pk }))

    actual fun coordinate(coordinate: NostrCoordinate): NostrFilter =
        NostrFilter(native.coordinate(coordinate.coordinate))
    actual fun coordinates(coordinates: List<NostrCoordinate>): NostrFilter =
        NostrFilter(native.coordinates(coordinates.map { it.coordinate }))

    actual fun customTag(tag: NostrSingleLetterTag, content: String): NostrFilter =
        NostrFilter(native.customTag(tag.native, content))
    actual fun customTags(tag: NostrSingleLetterTag, contents: List<String>): NostrFilter =
        NostrFilter(native.customTags(tag.native, contents))

    actual fun event(eventId: NostrEventId): NostrFilter = NostrFilter(native.event(eventId.native))
    actual fun events(ids: List<NostrEventId>): NostrFilter =
        NostrFilter(native.events(ids.map { it.native }))

    actual fun hashtag(hashtag: String): NostrFilter = NostrFilter(native.hashtag(hashtag))
    actual fun hashtags(hashtags: List<String>): NostrFilter = NostrFilter(native.hashtags(hashtags))

    actual fun id(id: NostrEventId): NostrFilter = NostrFilter(native.id(id.native))
    actual fun identifier(identifier: String): NostrFilter = NostrFilter(native.identifier(identifier))
    actual fun identifiers(identifiers: List<String>): NostrFilter = NostrFilter(native.identifiers(identifiers))
    actual fun ids(ids: List<NostrEventId>): NostrFilter = NostrFilter(native.ids(ids.map { it.native }))

    actual fun isEmpty(): Boolean = native.isEmpty()

    actual fun kind(kind: NostrKind): NostrFilter = NostrFilter(native.kind(kind.native))
    actual fun kinds(kinds: List<NostrKind>): NostrFilter = NostrFilter(native.kinds(kinds.map { it.native }))

    actual fun limit(limit: ULong): NostrFilter = NostrFilter(native.limit(limit))

    actual fun matchEvent(event: NostrEvent): Boolean = native.matchEvent(event.event)

    actual fun pubkey(pubkey: NostrPublicKey): NostrFilter = NostrFilter(native.pubkey(pubkey.pk))
    actual fun pubkeys(pubkeys: List<NostrPublicKey>): NostrFilter = NostrFilter(native.pubkeys(pubkeys.map { it.pk }))

    actual fun reference(reference: String): NostrFilter = NostrFilter(native.reference(reference))
    actual fun references(references: List<String>): NostrFilter = NostrFilter(native.references(references))

    actual fun removeAuthors(authors: List<NostrPublicKey>): NostrFilter =
        NostrFilter(native.removeAuthors(authors.map { it.pk }))

    actual fun removeCoordinates(coordinates: List<NostrCoordinate>): NostrFilter =
        NostrFilter(native.removeCoordinates(coordinates.map { it.coordinate }))

    actual fun removeCustomTags(tag: NostrSingleLetterTag, contents: List<String>): NostrFilter =
        NostrFilter(native.removeCustomTags(tag.native, contents))

    actual fun removeEvents(ids: List<NostrEventId>): NostrFilter =
        NostrFilter(native.removeEvents(ids.map { it.native }))

    actual fun removeHashtags(hashtags: List<String>): NostrFilter = NostrFilter(native.removeHashtags(hashtags))
    actual fun removeIdentifiers(identifiers: List<String>): NostrFilter = NostrFilter(native.removeIdentifiers(identifiers))
    actual fun removeIds(ids: List<NostrEventId>): NostrFilter = NostrFilter(native.removeIds(ids.map { it.native }))
    actual fun removeKinds(kinds: List<NostrKind>): NostrFilter = NostrFilter(native.removeKinds(kinds.map { it.native }))
    actual fun removeLimit(): NostrFilter = NostrFilter(native.removeLimit())
    actual fun removePubkeys(pubkeys: List<NostrPublicKey>): NostrFilter = NostrFilter(native.removePubkeys(pubkeys.map { it.pk }))
    actual fun removeReferences(references: List<String>): NostrFilter = NostrFilter(native.removeReferences(references))
    actual fun removeSearch(): NostrFilter = NostrFilter(native.removeSearch())
    actual fun removeSince(): NostrFilter = NostrFilter(native.removeSince())
    actual fun removeUntil(): NostrFilter = NostrFilter(native.removeUntil())

    actual fun search(text: String): NostrFilter = NostrFilter(native.search(text))
    actual fun since(timestamp: NostrTimestamp): NostrFilter = NostrFilter(native.since(timestamp.native))
    actual fun until(timestamp: NostrTimestamp): NostrFilter = NostrFilter(native.until(timestamp.native))

    actual fun unwrap(): Any = native
}
