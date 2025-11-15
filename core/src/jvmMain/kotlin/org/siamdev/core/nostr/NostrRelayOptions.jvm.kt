package org.siamdev.core.nostr

import rust.nostr.sdk.RelayOptions as NativeRelayOptions

actual class RelayOptions internal constructor(
    private val native: NativeRelayOptions
) {

    actual companion object {
        actual fun default(): RelayOptions = RelayOptions(NativeRelayOptions())
    }

    actual fun adjustRetryInterval(enable: Boolean): Result<RelayOptions> =
        runCatching { RelayOptions(native.adjustRetryInterval(enable)) }

    actual fun banRelayOnMismatch(enable: Boolean): Result<RelayOptions> =
        runCatching { RelayOptions(native.banRelayOnMismatch(enable)) }

    actual fun setConnectionMode(mode: NostrConnectionMode): Result<RelayOptions> =
        runCatching { RelayOptions(native.connectionMode(mode.new())) }

    actual fun setPing(enable: Boolean): Result<RelayOptions> =
        runCatching { RelayOptions(native.ping(enable)) }

    actual fun setRead(enable: Boolean): Result<RelayOptions> =
        runCatching { RelayOptions(native.read(enable)) }

    actual fun enableReconnect(enable: Boolean): Result<RelayOptions> =
        runCatching { RelayOptions(native.reconnect(enable)) }

    actual fun verifySubscriptions(enable: Boolean): Result<RelayOptions> =
        runCatching { RelayOptions(native.verifySubscriptions(enable)) }

    actual fun setWrite(enable: Boolean): Result<RelayOptions> =
        runCatching { RelayOptions(native.write(enable)) }
}
