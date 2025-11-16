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

import rust.nostr.sdk.MakeInvoiceRequest
import rust.nostr.sdk.MakeInvoiceResponse

actual class NostrMakeInvoiceRequest actual constructor(
    amount: ULong,
    description: String?,
    descriptionHash: String?,
    expiry: ULong?
) {
    internal val native: MakeInvoiceRequest = MakeInvoiceRequest(
        amount = amount,
        description = description,
        descriptionHash = descriptionHash,
        expiry = expiry
    )

    actual val amount: ULong
        get() = native.amount

    actual val description: String?
        get() = native.description

    actual val descriptionHash: String?
        get() = native.descriptionHash

    actual val expiry: ULong?
        get() = native.expiry
}

actual class NostrMakeInvoiceResponse actual constructor(
    invoice: String,
    paymentHash: String
) {
    internal val native: MakeInvoiceResponse = MakeInvoiceResponse(
        invoice = invoice,
        paymentHash = paymentHash
    )

    actual val invoice: String
        get() = native.invoice

    actual val paymentHash: String
        get() = native.paymentHash
}
