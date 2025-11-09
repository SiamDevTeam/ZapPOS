package org.siamdev.core.nostr

import kotlin.time.Duration

expect class RelayOptions {

    companion object {
        fun default(): RelayOptions
    }

    fun adjustRetryInterval(enable: Boolean): Result<RelayOptions>
    fun banRelayOnMismatch(enable: Boolean): Result<RelayOptions>
    fun setConnectionMode(mode: NostrConnectionMode): Result<RelayOptions>
//    fun setLimits(limits: RelayLimits): Result<RelayOptions>
    fun setPing(enable: Boolean): Result<RelayOptions>
    fun setRead(enable: Boolean): Result<RelayOptions>
    fun enableReconnect(enable: Boolean): Result<RelayOptions>
    fun verifySubscriptions(enable: Boolean): Result<RelayOptions>
    fun setWrite(enable: Boolean): Result<RelayOptions>
}
