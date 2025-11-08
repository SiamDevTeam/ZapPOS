package org.siamdev.core.nostr

import org.siamdev.core.nostr.keys.NostrPublicKey
import rust.nostr.sdk.Tag

actual class TagElt internal constructor(internal val tag: Tag) {

    actual companion object {
        actual fun alt(summary: String): TagElt = TagElt(Tag.alt(summary))

        actual fun publicKey(publicKey: NostrPublicKey): TagElt = TagElt(Tag.publicKey(publicKey.pk))

        actual  fun title(title: String): TagElt = TagElt(Tag.title(title))
    }

    actual fun toVec(): List<String> = tag.asVec()
    actual fun content(): String? = tag.content()

}