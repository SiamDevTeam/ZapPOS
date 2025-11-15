package org.siamdev.core.nostr

expect class NostrSubscribeOptions {

    fun closeOn(opts: NostrSubscribeAutoCloseOptions): NostrSubscribeOptions

    fun unwrap(): Any
}
