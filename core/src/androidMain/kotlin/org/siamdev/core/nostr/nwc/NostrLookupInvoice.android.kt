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
import rust.nostr.sdk.LookupInvoiceRequest
import org.siamdev.core.nostr.NostrJsonValue
import org.siamdev.core.nostr.NostrTimestamp
import org.siamdev.core.nostr.types.NostrTransactionType
import rust.nostr.sdk.LookupInvoiceResponse

@RequiresApi(Build.VERSION_CODES.O)
actual class NostrLookupInvoiceRequest internal constructor(
    internal val native: LookupInvoiceRequest
) {

    actual val paymentHash: String?
        get() = native.paymentHash

    actual val invoice: String?
        get() = native.invoice
}

@RequiresApi(Build.VERSION_CODES.O)
actual class NostrLookupInvoiceResponse internal constructor(
    internal val native: LookupInvoiceResponse
) {

    actual val transactionType: NostrTransactionType?
        get() = native.transactionType?.let { NostrTransactionType.of(it) }

    actual val invoice: String?
        get() = native.invoice

    actual val description: String?
        get() = native.description

    actual val descriptionHash: String?
        get() = native.descriptionHash

    actual val preimage: String?
        get() = native.preimage

    actual val paymentHash: String
        get() = native.paymentHash

    actual val amount: ULong
        get() = native.amount

    actual val feesPaid: ULong
        get() = native.feesPaid

    actual val createdAt: NostrTimestamp
        get() = NostrTimestamp(native.createdAt)

    actual val expiresAt: NostrTimestamp?
        get() = native.expiresAt?.let { NostrTimestamp(it) }

    actual val settledAt: NostrTimestamp?
        get() = native.settledAt?.let { NostrTimestamp(it) }

    actual val metadata: NostrJsonValue?
        get() = native.metadata?.let { NostrJsonValue.of(it) }
}
