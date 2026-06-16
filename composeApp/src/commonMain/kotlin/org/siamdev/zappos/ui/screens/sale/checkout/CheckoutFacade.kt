/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale.checkout

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Stable
interface CheckoutFacade {
    val step: CheckoutStep
    val orderItems: List<CheckoutItem>
    val totalFiat: String
    val totalSat: String
    val taxPercent: Float
    val grandTotalFiat: String
    val grandTotalSat: String
    val selectedPaymentMethod: PaymentMethod?
    val receivedAmount: String
    val changeAmount: Double
    val isChangeValid: Boolean

    fun syncFromMenu(items: List<CheckoutItem>, fiat: String, sat: String)
    fun setTax(percent: Float)
    fun formatChange(): String
    fun openSelectPayment()
    fun backToOrder()
    fun backToSelectPayment()
    fun selectMethod(method: PaymentMethod)
    fun confirmPayment()
    fun appendCashDigit(digit: String)
    fun deleteCashDigit()
    fun clearReceivedAmount()
    fun appendQuickAmount(amount: String)
    fun confirmCash()
    fun confirmProcessing()
    fun reset()
}

class CheckoutFacadeImpl(private val vm: CheckoutViewModel) : CheckoutFacade {

    private var _state by mutableStateOf(vm.state.value)

    init {
        vm.viewModelScope.launch {
            vm.state.collect { _state = it }
        }
    }

    override val step: CheckoutStep get() = _state.step
    override val orderItems: List<CheckoutItem> get() = _state.order.items
    override val totalFiat: String get() = _state.order.totalFiat
    override val totalSat: String get() = _state.order.totalSat
    override val taxPercent: Float get() = _state.order.taxPercent
    override val grandTotalFiat: String get() = _state.order.grandTotalFiat
    override val grandTotalSat: String get() = _state.order.grandTotalSat
    override val selectedPaymentMethod: PaymentMethod? get() = _state.payment.selectedMethod
    override val receivedAmount: String get() = _state.payment.receivedAmount
    override val changeAmount: Double get() = _state.payment.changeAmount
    override val isChangeValid: Boolean get() = _state.payment.isChangeValid

    override fun syncFromMenu(items: List<CheckoutItem>, fiat: String, sat: String) =
        vm.syncFromMenu(items, fiat, sat)

    override fun setTax(percent: Float) = vm.setTax(percent)
    override fun formatChange(): String = vm.formatChange()
    override fun openSelectPayment() = vm.openSelectPayment()
    override fun backToOrder() = vm.backToOrder()
    override fun backToSelectPayment() = vm.backToSelectPayment()
    override fun selectMethod(method: PaymentMethod) = vm.selectMethod(method)
    override fun confirmPayment() = vm.confirmPayment()
    override fun appendCashDigit(digit: String) = vm.appendCashDigit(digit)
    override fun deleteCashDigit() = vm.deleteCashDigit()
    override fun clearReceivedAmount() = vm.clearReceivedAmount()
    override fun appendQuickAmount(amount: String) = vm.appendQuickAmount(amount)
    override fun confirmCash() = vm.confirmCash()
    override fun confirmProcessing() = vm.confirmProcessing()
    override fun reset() = vm.reset()
}