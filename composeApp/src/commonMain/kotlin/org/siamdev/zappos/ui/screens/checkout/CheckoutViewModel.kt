/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.siamdev.zappos.LocalMenuVM

enum class PaymentMethod(val label: String) {
    NFC_LIGHTNING("NFC Lightning Card"),
    BITCOIN_LIGHTNING("Bitcoin Lightning"),
    PROMPT_PAY("Prompt Pay"),
    CASH("Cash")
}

enum class CheckoutStep {
    ORDER, SELECT_PAYMENT, CASH_CALCULATOR, PROCESSING, SUCCESS
}

data class CheckoutItem(
    val name: String,
    val count: UInt,
    val priceBaht: String,
    val priceSat: String
)

class CheckoutViewModel : ViewModel() {

    var orderItems by mutableStateOf<List<CheckoutItem>>(emptyList())
        private set

    var totalFiat by mutableStateOf("0.00")
        private set

    var totalSat by mutableStateOf("0")
        private set

    var step by mutableStateOf(CheckoutStep.ORDER)
        private set

    var selectedMethod by mutableStateOf<PaymentMethod?>(null)
        private set

    var receivedAmount by mutableStateOf("")
        private set

    var taxPercent by mutableStateOf(7f)
        private set

    fun syncFromMenu(
        items: List<CheckoutItem>,
        fiat: String,
        sat: String
    ) {
        orderItems = items
        totalFiat = fiat
        totalSat = sat
    }

    fun setTax(percent: Float) { taxPercent = percent }

    val grandTotalFiat: String
        get() {
            val subtotal = totalFiat.replace(",", "").toDoubleOrNull() ?: 0.0
            return formatDouble(subtotal * (1 + taxPercent / 100))
        }

    val grandTotalSat: String
        get() {
            val subtotal = totalSat.replace(",", "").toDoubleOrNull() ?: 0.0
            return formatDouble(subtotal * (1 + taxPercent / 100))
        }

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
        receivedAmount = when {
            digit != "." && (receivedAmount == "0" || receivedAmount.isEmpty()) -> digit
            else -> receivedAmount + digit
        }
    }

    fun deleteCashDigit() { receivedAmount = receivedAmount.dropLast(1) }

    fun clearReceivedAmount() { receivedAmount = "" }

    fun appendQuickAmount(amount: String) {
        val add = amount.toDoubleOrNull() ?: return
        val current = receivedAmount.replace(",", "").toDoubleOrNull() ?: 0.0
        val result = current + add
        receivedAmount = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            val intPart = result.toLong()
            val decPart = ((result - intPart) * 100).toLong()
            "$intPart.${decPart.toString().padStart(2, '0')}"
        }
    }

    fun confirmCash() { if (isChangeValid) step = CheckoutStep.PROCESSING }

    fun confirmProcessing() { step = CheckoutStep.SUCCESS }

    fun formatChange(): String = formatDouble(changeAmount)
}