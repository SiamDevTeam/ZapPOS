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

import org.siamdev.core.nostr.RelayUrl
import org.siamdev.core.nostr.types.NostrRelayStatus
import rust.nostr.sdk.Nwc as NativeNwc

actual class NostrNwc actual constructor(uri: NostrWalletConnectUri) {
    internal val native: NativeNwc = NativeNwc(uri.native)

    actual suspend fun getBalance(): Result<ULong> =
        runCatching { native.getBalance() }

    actual suspend fun getInfo(): Result<NostrGetInfoResponse> =
        runCatching { NostrGetInfoResponse(native.getInfo()) }

    actual suspend fun listTransactions(params: NostrListTransactionsRequest): Result<List<NostrLookupInvoiceResponse>> =
        runCatching { native.listTransactions(params.native).map { NostrLookupInvoiceResponse(it) } }

    actual suspend fun lookupInvoice(params: NostrLookupInvoiceRequest): Result<NostrLookupInvoiceResponse> =
        runCatching { NostrLookupInvoiceResponse(native.lookupInvoice(params.native)) }

    actual suspend fun makeInvoice(params: NostrMakeInvoiceRequest): Result<NostrMakeInvoiceResponse> =
        runCatching {
            val nativeResponse = native.makeInvoice(params.native)
            NostrMakeInvoiceResponse(
                invoice = nativeResponse.invoice,
                paymentHash = nativeResponse.paymentHash
            )
        }

    actual suspend fun payInvoice(params: NostrPayInvoiceRequest): Result<NostrPayInvoiceResponse> =
        runCatching {
            val nativeResponse = native.payInvoice(params.native)
            NostrPayInvoiceResponse(
                preimage = nativeResponse.preimage,
                feesPaid = nativeResponse.feesPaid
            )
        }

    actual suspend fun payKeysend(params: NostrPayKeysendRequest): Result<NostrPayKeysendResponse> =
        runCatching { NostrPayKeysendResponse(native.payKeysend(params.native)) }

    actual suspend fun status(): Result<Map<RelayUrl, NostrRelayStatus>> =
        runCatching {
            native.status().mapKeys { RelayUrl(it.key) }
                .mapValues { NostrRelayStatus.of(it.value) }
        }
}
