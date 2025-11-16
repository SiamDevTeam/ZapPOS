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

import rust.nostr.sdk.PayInvoiceRequest
import rust.nostr.sdk.PayInvoiceResponse

actual class NostrPayInvoiceRequest actual constructor(
    invoice: String,
    amount: ULong?,
    id: String?
) {
    internal val native: PayInvoiceRequest = PayInvoiceRequest(
        invoice = invoice,
        amount = amount,
        id = id
    )

    actual val id: String?
        get() = native.id

    actual val invoice: String
        get() = native.invoice

    actual val amount: ULong?
        get() = native.amount
}

actual class NostrPayInvoiceResponse actual constructor(
    preimage: String,
    feesPaid: ULong?
) {
    internal val native: PayInvoiceResponse = PayInvoiceResponse(
        preimage = preimage,
        feesPaid = feesPaid
    )

    actual val preimage: String
        get() = native.preimage

    actual val feesPaid: ULong?
        get() = native.feesPaid
}
