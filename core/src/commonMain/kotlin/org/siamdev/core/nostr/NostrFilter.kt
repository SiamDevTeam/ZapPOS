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

expect class NostrFilter {

    companion object {
        @Throws(Exception::class)
        fun fromJson(json: String): NostrFilter

        fun fromRecord(record: NostrFilterRecord): NostrFilter
    }

    fun asJson(): String
    fun asRecord(): NostrFilterRecord

    fun author(author: NostrPublicKey): NostrFilter
    fun authors(authors: List<NostrPublicKey>): NostrFilter

    fun coordinate(coordinate: NostrCoordinate): NostrFilter
    fun coordinates(coordinates: List<NostrCoordinate>): NostrFilter

    fun customTag(tag: NostrSingleLetterTag, content: String): NostrFilter
    fun customTags(tag: NostrSingleLetterTag, contents: List<String>): NostrFilter

    fun event(eventId: NostrEventId): NostrFilter
    fun events(ids: List<NostrEventId>): NostrFilter

    fun hashtag(hashtag: String): NostrFilter
    fun hashtags(hashtags: List<String>): NostrFilter

    fun id(id: NostrEventId): NostrFilter
    fun identifier(identifier: String): NostrFilter
    fun identifiers(identifiers: List<String>): NostrFilter
    fun ids(ids: List<NostrEventId>): NostrFilter

    fun isEmpty(): Boolean

    fun kind(kind: NostrKind): NostrFilter
    fun kinds(kinds: List<NostrKind>): NostrFilter

    fun limit(limit: ULong): NostrFilter

    fun matchEvent(event: NostrEvent): Boolean

    fun pubkey(pubkey: NostrPublicKey): NostrFilter
    fun pubkeys(pubkeys: List<NostrPublicKey>): NostrFilter

    fun reference(reference: String): NostrFilter
    fun references(references: List<String>): NostrFilter

    fun removeAuthors(authors: List<NostrPublicKey>): NostrFilter
    fun removeCoordinates(coordinates: List<NostrCoordinate>): NostrFilter
    fun removeCustomTags(tag: NostrSingleLetterTag, contents: List<String>): NostrFilter
    fun removeEvents(ids: List<NostrEventId>): NostrFilter
    fun removeHashtags(hashtags: List<String>): NostrFilter
    fun removeIdentifiers(identifiers: List<String>): NostrFilter
    fun removeIds(ids: List<NostrEventId>): NostrFilter
    fun removeKinds(kinds: List<NostrKind>): NostrFilter
    fun removeLimit(): NostrFilter
    fun removePubkeys(pubkeys: List<NostrPublicKey>): NostrFilter
    fun removeReferences(references: List<String>): NostrFilter
    fun removeSearch(): NostrFilter
    fun removeSince(): NostrFilter
    fun removeUntil(): NostrFilter

    fun search(text: String): NostrFilter
    fun since(timestamp: NostrTimestamp): NostrFilter
    fun until(timestamp: NostrTimestamp): NostrFilter
    fun unwrap(): Any
}
