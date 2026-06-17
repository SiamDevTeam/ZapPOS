/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.siamdev.zappos.data.source.EventKind
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.screens.product.goods.sampleProducts
import org.siamdev.zappos.utils.TimeValue

class MasterEntryViewModel : ViewModel() {

    enum class Mode { CREATE, EDIT }

    data class State(
        val mode: Mode = Mode.CREATE,
        val isLoading: Boolean = false,
        val entryType: EntryType = EntryType.GOODS,
        val productId: String? = null,
        val name: String = "",
        val category: String = "",
        val subCategory: String? = null,
        val description: String = "",
        val isAvailable: Boolean = true,
        val isRecommended: Boolean = false,
        val price: String = "",
        val unit: String = "piece",
        val chargeVat: Boolean = true,
        val costPrice: String = "",
        val showCostPrice: Boolean = false,
        val openPrice: Boolean = false,
        val chargedBy: Int = 2,
        val bookingDuration: String = "60",
        val minBooking: String = "1",
        val trackStock: Boolean = true,
        val openingStock: String = "0",
        val maxCapacity: String = "0",
        val lowStockAlert: String = "0",
        val supplier: String = "",
        val trackActive: Boolean = false,
        val serviceCapacity: String = "12",
        val serviceDuration: String = "60",
        val serviceOpens: TimeValue = TimeValue(9, 0),
        val serviceCloses: TimeValue = TimeValue(18, 0),
        val activeDays: Set<Int> = setOf(0, 1, 2, 3, 4),
        val instructor: String = "",
        val serviceRequiresBooking: Boolean = false,
        val rentalUnitsCount: String = "2",
        val rentalBuffer: String = "0",
        val rentalOpens: TimeValue = TimeValue(8, 0),
        val rentalCloses: TimeValue = TimeValue(22, 0),
        val depositAmount: String = "0.00",
        val rentalRequiresBooking: Boolean = true,
        val optionGroups: List<OptionGroup> = emptyList(),
        val advancedExpanded: Boolean = false,
        val sku: String = "",
        val barcode: String = "",
        val sendOrderTo: String = "None",
        val displayOrder: String = "0",
    )

    sealed class SideEffect {
        data object SaveSuccess : SideEffect()
        data class SaveError(val message: String) : SideEffect()
        data object NavigateBack : SideEffect()
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SideEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<SideEffect> = _effect.asSharedFlow()

    fun init(productId: String?) {
        if (productId == null) {
            _state.update { State() }
            return
        }
        _state.update { it.copy(mode = Mode.EDIT, productId = productId, isLoading = true) }
        viewModelScope.launch {
            val event = sampleProducts().find { it.id == productId }
            _state.update { if (event != null) it.loadFrom(event) else it.copy(isLoading = false) }
        }
    }

    fun update(block: State.() -> State) = _state.update { it.block() }

    fun save() {
        viewModelScope.launch {
            // TODO: persist via repository
            _effect.emit(SideEffect.SaveSuccess)
        }
    }

    fun discard() {
        viewModelScope.launch {
            _effect.emit(SideEffect.NavigateBack)
        }
    }
}

private fun MasterEntryViewModel.State.loadFrom(event: MasterEvent): MasterEntryViewModel.State {
    val entryType = when (event.kind) {
        EventKind.SERVICE -> EntryType.SERVICE
        EventKind.RENTAL -> EntryType.RENTAL
        else -> EntryType.GOODS
    }
    val costPriceVal = event.costPrice
    val stockQtyVal = event.stockQty
    val stockMaxVal = event.stockMax
    val lowStockAlertVal = event.lowStockAlert
    return copy(
        mode = MasterEntryViewModel.Mode.EDIT,
        isLoading = false,
        entryType = entryType,
        productId = event.id,
        name = event.name,
        category = event.category,
        subCategory = event.subCategory,
        description = event.description,
        isAvailable = event.isAvailable,
        isRecommended = event.isRecommended,
        price = event.price.toLong().toString(),
        unit = event.unit,
        sku = event.sku,
        chargeVat = event.chargeVat,
        openPrice = event.openPrice,
        supplier = event.supplier,
        showCostPrice = costPriceVal != null,
        costPrice = costPriceVal?.toLong()?.toString() ?: "",
        trackStock = stockQtyVal != null,
        openingStock = stockQtyVal?.toString() ?: "0",
        maxCapacity = stockMaxVal?.toString() ?: "0",
        lowStockAlert = lowStockAlertVal?.toString() ?: "0",
        optionGroups = event.optionGroups.map { group ->
            OptionGroup(
                name = group.name,
                pickMode = if (group.multiSelect) PickMode.MANY else PickMode.ONE,
                required = group.required,
                items = group.items.map { OptionItem(name = it.name, priceModifier = it.priceModifier) },
            )
        },
    )
}