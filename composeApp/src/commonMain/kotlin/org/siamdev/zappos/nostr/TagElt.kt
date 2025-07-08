package org.siamdev.zappos.nostr

import org.siamdev.zappos.nostr.keys.NostrPublicKey

expect class TagElt {


    companion object {
        fun alt(summary: String): TagElt

        fun publicKey(publicKey: NostrPublicKey): TagElt

        fun title(title: String): TagElt

    }

    fun toVec(): List<String>
    fun content(): String?

}