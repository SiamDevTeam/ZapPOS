package org.siamdev.core.nostr

expect sealed class NostrConnectionMode {

    class NostrProxy : NostrConnectionMode {
        val ip: String
        val port: UShort

        constructor(ip: String, port: UShort)
    }
}
