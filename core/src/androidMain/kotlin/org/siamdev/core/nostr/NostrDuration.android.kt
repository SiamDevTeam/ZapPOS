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
import java.time.Duration

@androidx.annotation.RequiresApi(Build.VERSION_CODES.O)
actual class NostrDuration internal constructor(
    internal val native: Duration
) {

    actual companion object {
        actual fun milliseconds(value: Long): NostrDuration =
            NostrDuration(Duration.ofMillis(value))

        actual fun seconds(value: Long): NostrDuration =
            NostrDuration(Duration.ofSeconds(value))
    }

    actual fun inWholeMilliseconds(): Long = native.toMillis()
    actual fun inWholeSeconds(): Long = native.seconds
    actual fun toNative(): NostrDuration = NostrDuration(native)

    internal actual fun unwrap(): Any = native
}
