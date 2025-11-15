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

import rust.nostr.sdk.Reconciliation as NativeReconciliation
import rust.nostr.sdk.ReconciliationSendFailureItem as NativeReconciliationSendFailureItem

actual class NostrReconciliation internal constructor(
    internal val native: NativeReconciliation
) {
    actual val local: List<NostrEventId>
        get() = native.local.map { NostrEventId(it) }

    actual val remote: List<NostrEventId>
        get() = native.remote.map { NostrEventId(it) }

    actual val sent: List<NostrEventId>
        get() = native.sent.map { NostrEventId(it) }

    actual val received: List<NostrEventId>
        get() = native.received.map { NostrEventId(it) }

    actual val sendFailures: Map<RelayUrl, List<NostrReconciliationSendFailureItem>>
        get() = native.sendFailures.mapKeys { RelayUrl(it.key) }
            .mapValues { entry -> entry.value.map { NostrReconciliationSendFailureItem(it) } }
}

actual class NostrReconciliationSendFailureItem internal constructor(
    internal val native: NativeReconciliationSendFailureItem
) {
    actual val id: NostrEventId
        get() = NostrEventId(native.id)

    actual val error: String
        get() = native.error
}
