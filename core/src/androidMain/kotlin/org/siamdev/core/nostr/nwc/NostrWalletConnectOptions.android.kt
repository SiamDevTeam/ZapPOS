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
package org.siamdev.core.nostr.nwc

import android.os.Build
import androidx.annotation.RequiresApi
import org.siamdev.core.nostr.NostrDuration
import org.siamdev.core.nostr.NostrRelayOptions
import rust.nostr.sdk.NostrWalletConnectOptions as NativeWalletConnectOptions
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
actual class NostrWalletConnectOptions internal constructor(
    internal val native: NativeWalletConnectOptions
) {
    actual constructor(): this(NativeWalletConnectOptions())

    actual fun relay(opts: NostrRelayOptions): NostrWalletConnectOptions =
        NostrWalletConnectOptions(native.relay(opts.native))

    actual fun timeout(timeout: NostrDuration): NostrWalletConnectOptions =
        NostrWalletConnectOptions(native.timeout(timeout.unwrap() as Duration))
}
