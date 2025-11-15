package org.siamdev.core.nostr

expect class NostrDuration {

    companion object {
        fun milliseconds(value: Long): NostrDuration
        fun seconds(value: Long): NostrDuration
    }

    fun inWholeMilliseconds(): Long
    fun inWholeSeconds(): Long
    fun toNative(): NostrDuration

    internal fun unwrap(): Any

}
