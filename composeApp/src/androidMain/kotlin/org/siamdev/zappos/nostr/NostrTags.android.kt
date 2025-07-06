package org.siamdev.zappos.nostr

import org.siamdev.zappos.nostr.keys.NostrPublicKey
import rust.nostr.sdk.Tags

actual class NostrTags(internal val tags: Tags) {

    actual companion object {
        actual fun fromList(list: List<TagElt>): NostrTags {
            val rustList = list.map { it.tag }
            return NostrTags(Tags.fromList(rustList))
        }

        actual fun fromText(text: String): NostrTags {
            return NostrTags(Tags.fromText(text))
        }

        actual fun parse(tags: List<List<String>>): NostrTags {
            return NostrTags(Tags.parse(tags))
        }
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
