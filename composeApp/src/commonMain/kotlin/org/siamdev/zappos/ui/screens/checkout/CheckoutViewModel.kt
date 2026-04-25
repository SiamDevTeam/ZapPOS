package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class PaymentMethod(val label: String) {
    NFC_LIGHTNING("NFC Lightning Card"),
    BITCOIN_LIGHTNING("Bitcoin Lightning"),
    PROMPT_PAY("Prompt Pay"),
    CASH("Cash")
}

enum class CheckoutStep {
    ORDER,
    SELECT_PAYMENT,
    CASH_CALCULATOR,
    PROCESSING,
    SUCCESS
}

data class CheckoutItem(
    val name: String,
    val count: UInt,
    val priceBaht: String,
    val priceSat: String
)

class CheckoutViewModel(
    val orderItems: List<CheckoutItem>,
    val totalFiat: String,
    val totalSat: String
) : ViewModel() {

    var step by mutableStateOf(CheckoutStep.ORDER)
        private set

    var selectedMethod by mutableStateOf<PaymentMethod?>(null)
        private set

    var receivedAmount by mutableStateOf("")
        private set

    val changeAmount: Double
        get() {
            val total = totalFiat.replace(",", "").toDoubleOrNull() ?: 0.0
            val received = receivedAmount.replace(",", "").toDoubleOrNull() ?: 0.0
            return (received - total).coerceAtLeast(0.0)
        }

    val isChangeValid: Boolean
        get() {
            val total = totalFiat.replace(",", "").toDoubleOrNull() ?: 0.0
            val received = receivedAmount.replace(",", "").toDoubleOrNull() ?: 0.0
            return received >= total
        }

    fun openSelectPayment() { step = CheckoutStep.SELECT_PAYMENT }
    fun backToOrder() { step = CheckoutStep.ORDER }
    fun backToSelectPayment() { step = CheckoutStep.SELECT_PAYMENT }

    fun selectMethod(method: PaymentMethod) { selectedMethod = method }

    fun confirmPayment() {
        val method = selectedMethod ?: return
        step = if (method == PaymentMethod.CASH) CheckoutStep.CASH_CALCULATOR
        else CheckoutStep.PROCESSING
    }

    fun appendCashDigit(digit: String) {
        if (digit == "." && receivedAmount.contains(".")) return
        receivedAmount += digit
    }

    fun deleteCashDigit() {
        receivedAmount = receivedAmount.dropLast(1)
    }

    fun confirmCash() {
        if (isChangeValid) step = CheckoutStep.PROCESSING
    }

    fun confirmProcessing() { step = CheckoutStep.SUCCESS }

    fun formatChange(): String {
        val v = changeAmount
        val intPart = v.toLong()
        val decPart = ((v - intPart) * 100).toInt()
        val intStr = intPart.toString().reversed().chunked(3).joinToString(",").reversed()
        return "$intStr.${decPart.toString().padStart(2, '0')}"
    }

}