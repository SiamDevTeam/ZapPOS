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

import java.time.Duration
import rust.nostr.sdk.ReqExitPolicy as NativeReqExitPolicy

actual sealed class NostrReqExitPolicy {

    internal abstract fun unwrap(): NativeReqExitPolicy

    actual class NostrExitOnEose internal constructor(
        internal val native: NativeReqExitPolicy.ExitOnEose
    ) : NostrReqExitPolicy() {

        constructor() : this(NativeReqExitPolicy.ExitOnEose)

        override fun unwrap(): NativeReqExitPolicy = native
    }

    actual class NostrWaitForEvents internal constructor(
        internal val native: NativeReqExitPolicy.WaitForEvents
    ) : NostrReqExitPolicy() {

        actual constructor(num: UShort) : this(NativeReqExitPolicy.WaitForEvents(num))

        actual val num: UShort
            get() = native.num

        override fun unwrap(): NativeReqExitPolicy = native
    }

    actual class NostrWaitForEventsAfterEose internal constructor(
        internal val native: NativeReqExitPolicy.WaitForEventsAfterEose
    ) : NostrReqExitPolicy() {

        actual constructor(num: UShort) : this(NativeReqExitPolicy.WaitForEventsAfterEose(num))

        actual val num: UShort
            get() = native.num

        override fun unwrap(): NativeReqExitPolicy = native
    }

    actual class NostrWaitDurationAfterEose internal constructor(
        internal val native: NativeReqExitPolicy.WaitDurationAfterEose
    ) : NostrReqExitPolicy() {

        actual constructor(duration: NostrDuration) : this(
            NativeReqExitPolicy.WaitDurationAfterEose(duration.unwrap() as Duration)
        )

        actual val duration: NostrDuration
            get() = NostrDuration(native.duration)

        override fun unwrap(): NativeReqExitPolicy = native
    }
}
