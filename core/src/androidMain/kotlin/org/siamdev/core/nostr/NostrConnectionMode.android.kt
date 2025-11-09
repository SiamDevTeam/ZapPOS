package org.siamdev.core.nostr

import rust.nostr.sdk.ConnectionMode as NativeConnectionMode

actual sealed class NostrConnectionMode {

    internal abstract fun new(): NativeConnectionMode

    actual class NostrProxy internal constructor(
        internal val prop: NativeConnectionMode.Proxy
    ) : NostrConnectionMode() {

        actual val ip: String
            get() = prop.ip
        actual val port: UShort
            get() = prop.port

        actual constructor(ip: String, port: UShort)
                : this(NativeConnectionMode.Proxy(ip, port))

        override fun new(): NativeConnectionMode = prop
    }
}
