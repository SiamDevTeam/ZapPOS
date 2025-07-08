package org.siamdev.zappos.nostr

import org.siamdev.zappos.nostr.keys.NostrPublicKey

expect class NostrTags {
    companion object {
        fun fromList(list: List<TagElt>): NostrTags
        fun fromText(text: String): NostrTags
        fun parse(tags: List<List<String>>): NostrTags
    }

    fun first(): TagElt?
    fun isEmpty(): Boolean
    fun hashtags(): List<String>
    fun identifier(): String?
    fun len(): ULong
    fun publicKeys(): List<NostrPublicKey>
    fun toVec(): List<TagElt>
}
