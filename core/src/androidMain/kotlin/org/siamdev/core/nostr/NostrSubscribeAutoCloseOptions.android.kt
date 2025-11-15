/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDev by SiamDharmar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.core.nostr

import android.os.Build
import androidx.annotation.RequiresApi
import rust.nostr.sdk.SubscribeAutoCloseOptions as NativeSubscribeAutoCloseOptions
import rust.nostr.sdk.ReqExitPolicy as NativeReqExitPolicy
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
actual class NostrSubscribeAutoCloseOptions internal constructor(
    internal val native: NativeSubscribeAutoCloseOptions
) {

    actual fun exitPolicy(policy: NostrReqExitPolicy): NostrSubscribeAutoCloseOptions {
        val nativePolicy = when (policy) {
            is NostrReqExitPolicy.NostrExitOnEose -> NativeReqExitPolicy.ExitOnEose
            is NostrReqExitPolicy.NostrWaitForEvents -> NativeReqExitPolicy.WaitForEvents(policy.num)
            is NostrReqExitPolicy.NostrWaitForEventsAfterEose -> NativeReqExitPolicy.WaitForEventsAfterEose(policy.num)
            is NostrReqExitPolicy.NostrWaitDurationAfterEose -> NativeReqExitPolicy.WaitDurationAfterEose(
                policy.duration.unwrap() as Duration
            )
        }
        return NostrSubscribeAutoCloseOptions(native.exitPolicy(nativePolicy))
    }

    actual fun idleTimeout(timeout: NostrDuration?): NostrSubscribeAutoCloseOptions =
        NostrSubscribeAutoCloseOptions(
            native.idleTimeout(timeout?.unwrap() as Duration)
        )

    actual fun timeout(timeout: NostrDuration?): NostrSubscribeAutoCloseOptions =
        NostrSubscribeAutoCloseOptions(
            native.timeout(timeout?.unwrap() as Duration)
        )
}
