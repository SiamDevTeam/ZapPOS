/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale.checkout

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    data class State(
        val step: CheckoutStep = CheckoutStep.ORDER,
        val order: Order = Order(),
        val payment: Payment = Payment()
    ) {
        data class Order(
            val items: List<CheckoutItem> = emptyList(),
            val totalFiat: String = "0.00",
            val totalSat: String = "0",
            val taxPercent: Float = 7f,
            val grandTotalFiat: String = "0.00",
            val grandTotalSat: String = "0"
        )

        data class Payment(
            val selectedMethod: PaymentMethod? = null,
            val receivedAmount: String = "",
            val changeAmount: Double = 0.0,
            val isChangeValid: Boolean = false
        )
    }

    sealed class SideEffect

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private fun State.recomputed(): State {
        val subtotalFiat = order.totalFiat.replace(",", "").toDoubleOrNull() ?: 0.0
        val subtotalSat = order.totalSat.replace(",", "").toDoubleOrNull() ?: 0.0
        val tax = order.taxPercent / 100f
        val received = payment.receivedAmount.replace(",", "").toDoubleOrNull() ?: 0.0
        val change = (received - subtotalFiat).coerceAtLeast(0.0)
        return copy(
            order = order.copy(
                grandTotalFiat = formatDouble(subtotalFiat * (1 + tax)),
                grandTotalSat = formatDouble(subtotalSat * (1 + tax))
            ),
            payment = payment.copy(
                changeAmount = change,
                isChangeValid = received >= subtotalFiat
            )
        )
    }

    fun syncFromMenu(items: List<CheckoutItem>, fiat: String, sat: String) {
        _state.update {
            it.copy(
                order = it.order.copy(items = items, totalFiat = fiat, totalSat = sat)
            ).recomputed()
        }
    }

    fun setTax(percent: Float) {
        _state.update { it.copy(order = it.order.copy(taxPercent = percent)).recomputed() }
    }

    fun formatChange(): String = formatDouble(_state.value.payment.changeAmount)

    fun openSelectPayment() {
        _state.update { it.copy(step = CheckoutStep.SELECT_PAYMENT) }
    }

    fun backToOrder() {
        _state.update { it.copy(step = CheckoutStep.ORDER) }
    }

    fun backToSelectPayment() {
        _state.update { it.copy(step = CheckoutStep.SELECT_PAYMENT) }
    }

    fun selectMethod(method: PaymentMethod) {
        _state.update { it.copy(payment = it.payment.copy(selectedMethod = method)) }
    }

    fun confirmPayment() {
        val method = _state.value.payment.selectedMethod ?: return
        val nextStep =
            if (method == PaymentMethod.CASH) CheckoutStep.CASH_CALCULATOR else CheckoutStep.PROCESSING
        _state.update { it.copy(step = nextStep) }
    }

    fun appendCashDigit(digit: String) {
        val current = _state.value.payment.receivedAmount
        if (digit == "." && current.contains(".")) return
        val next = when {
            digit != "." && (current == "0" || current.isEmpty()) -> digit
            else -> current + digit
        }
        _state.update { it.copy(payment = it.payment.copy(receivedAmount = next)).recomputed() }
    }

    fun deleteCashDigit() {
        val next = _state.value.payment.receivedAmount.dropLast(1)
        _state.update { it.copy(payment = it.payment.copy(receivedAmount = next)).recomputed() }
    }

    fun clearReceivedAmount() {
        _state.update { it.copy(payment = it.payment.copy(receivedAmount = "")).recomputed() }
    }

    fun appendQuickAmount(amount: String) {
        val add = amount.toDoubleOrNull() ?: return
        val current = _state.value.payment.receivedAmount.replace(",", "").toDoubleOrNull() ?: 0.0
        val result = current + add
        val next = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            val intPart = result.toLong()
            val decPart = ((result - intPart) * 100).toLong()
            "$intPart.${decPart.toString().padStart(2, '0')}"
        }
        _state.update { it.copy(payment = it.payment.copy(receivedAmount = next)).recomputed() }
    }

    fun confirmCash() {
        if (_state.value.payment.isChangeValid) {
            _state.update { it.copy(step = CheckoutStep.PROCESSING) }
        }
    }

    fun confirmProcessing() {
        _state.update { it.copy(step = CheckoutStep.SUCCESS) }
    }

    fun reset() {
        _state.update {
            it.copy(
                step = CheckoutStep.ORDER,
                payment = it.payment.copy(selectedMethod = null, receivedAmount = "")
            ).recomputed()
        }
    }
}

internal fun formatDouble(value: Double): String {
    val intPart = value.toLong()
    val decPart = ((value - intPart) * 100).toInt()
    val intStr = intPart.toString().reversed().chunked(3).joinToString(",").reversed()
    return "$intStr.${decPart.toString().padStart(2, '0')}"
}