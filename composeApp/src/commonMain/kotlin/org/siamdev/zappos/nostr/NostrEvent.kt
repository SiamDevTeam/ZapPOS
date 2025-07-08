package org.siamdev.zappos.nostr

expect class NostrEvent {

    companion object {
        fun fromJson(json: String): NostrEvent
    }

    fun toJson(): String

    val id: String
    val pubkey: String
    val createdAt: ULong
    val kind: UShort
    val content: String
    val sig: String

    val tags: NostrTags


    fun hashtags(): List<String>
    fun taggedPublicKeys(): List<String>
    fun taggedEventIds(): List<String>
    fun identifier(): String?
}
