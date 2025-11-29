package org.siamdev.zappos

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.siamdev.core.nostr.nwc.*

class NostrNwcTest {

    @Test
    fun testNip47Flow() = runBlocking {
        val uri = NostrWalletConnectUri.parse("nostr+walletconnect://mockuri")
        val nwc = NostrNwc(uri)

        val infoResult = nwc.getInfo()
        assertTrue(infoResult.isSuccess)
        println("Supported methods: ${infoResult.getOrThrow().methods}")

        val balanceResult = nwc.getBalance()
        assertTrue(balanceResult.isSuccess)
        println("Balance: ${balanceResult.getOrThrow()} mSAT")

        val payInvoiceParams = NostrPayInvoiceRequest(invoice = "lnbcmock", amount = null, id = null)
        val payResult = nwc.payInvoice(payInvoiceParams)
        assertTrue(payResult.isSuccess)
        println("PayInvoice preimage: ${payResult.getOrThrow().preimage}")

        val makeInvoiceParams = NostrMakeInvoiceRequest(amount = 100u, description = null, descriptionHash = null, expiry = null)
        val makeResult = nwc.makeInvoice(makeInvoiceParams)
        assertTrue(makeResult.isSuccess)
        println("Invoice: ${makeResult.getOrThrow().invoice}")
    }
}
