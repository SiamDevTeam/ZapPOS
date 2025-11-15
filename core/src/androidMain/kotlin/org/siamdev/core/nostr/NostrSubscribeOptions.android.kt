package org.siamdev.core.nostr

import rust.nostr.sdk.SubscribeOptions as NativeSubscribeOptions

actual class NostrSubscribeOptions internal constructor(
    internal val native: NativeSubscribeOptions
) {

    actual fun closeOn(opts: NostrSubscribeAutoCloseOptions): NostrSubscribeOptions =
        NostrSubscribeOptions(native.closeOn(opts.native))

    actual fun unwrap(): Any = native
}
