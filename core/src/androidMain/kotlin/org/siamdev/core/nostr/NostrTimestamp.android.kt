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
import java.time.Duration
import rust.nostr.sdk.Timestamp as NativeTimestamp

@RequiresApi(Build.VERSION_CODES.O)
actual class NostrTimestamp internal constructor(
    internal val native: NativeTimestamp
) {

    actual companion object {
        actual fun fromSecs(secs: ULong): NostrTimestamp =
            NostrTimestamp(NativeTimestamp.fromSecs(secs))

        actual fun max(): NostrTimestamp =
            NostrTimestamp(NativeTimestamp.max())

        actual fun min(): NostrTimestamp =
            NostrTimestamp(NativeTimestamp.min())

        actual fun now(): NostrTimestamp =
            NostrTimestamp(NativeTimestamp.now())
    }

    actual fun asSecs(): ULong = native.asSecs()

    actual fun addDuration(duration: NostrDuration): NostrTimestamp =
        NostrTimestamp(
            native.addDuration(duration.unwrap() as Duration)
        )

    actual fun toHumanDatetime(): String = native.toHumanDatetime()

    actual fun unwrap(): Any = native

}
