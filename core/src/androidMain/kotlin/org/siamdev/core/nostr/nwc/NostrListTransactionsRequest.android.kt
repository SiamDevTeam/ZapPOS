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
import org.siamdev.core.nostr.NostrTimestamp
import org.siamdev.core.nostr.types.NostrTransactionType
import rust.nostr.sdk.ListTransactionsRequest

@RequiresApi(Build.VERSION_CODES.O)
actual class NostrListTransactionsRequest internal constructor(
    internal val native: ListTransactionsRequest
) {

    actual val from: NostrTimestamp?
        get() = native.from?.let { NostrTimestamp(it) }

    actual val until: NostrTimestamp?
        get() = native.until?.let { NostrTimestamp(it) }

    actual val limit: ULong?
        get() = native.limit

    actual val offset: ULong?
        get() = native.offset

    actual val unpaid: Boolean?
        get() = native.unpaid

    actual val transactionType: NostrTransactionType?
        get() = native.transactionType?.let { NostrTransactionType.of(it) }
}