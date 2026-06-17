/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.siamdev.zappos.utils.TimeValue

@Stable
internal interface MasterEntrySurface {
    val isEditMode: Boolean
    val isFormValid: Boolean
    val unitOptions: List<String>

    var entryType: EntryType
    var productId: String?
    var name: String
    var category: String
    var subCategory: String?
    var description: String
    var isAvailable: Boolean
    var isRecommended: Boolean
    var price: String
    var unit: String
    var chargeVat: Boolean
    var costPrice: String
    var showCostPrice: Boolean
    var openPrice: Boolean
    var chargedBy: Int
    var bookingDuration: String
    var minBooking: String
    var trackStock: Boolean
    var openingStock: String
    var maxCapacity: String
    var lowStockAlert: String
    var supplier: String
    var trackActive: Boolean
    var serviceCapacity: String
    var serviceDuration: String
    var serviceOpens: TimeValue
    var serviceCloses: TimeValue
    var activeDays: Set<Int>
    var instructor: String
    var serviceRequiresBooking: Boolean
    var rentalUnitsCount: String
    var rentalBuffer: String
    var rentalOpens: TimeValue
    var rentalCloses: TimeValue
    var depositAmount: String
    var rentalRequiresBooking: Boolean
    var optionGroups: List<OptionGroup>
    var advancedExpanded: Boolean
    var sku: String
    var barcode: String
    var sendOrderTo: String
    var displayOrder: String

    val effect: SharedFlow<MasterEntryViewModel.SideEffect>

    fun save()
    fun discard()
}

internal class MasterEntrySurfaceImpl(
    private val vm: MasterEntryViewModel,
) : MasterEntrySurface {

    private var _state by mutableStateOf(vm.state.value)

    init {
        vm.viewModelScope.launch {
            vm.state.collect { _state = it }
        }
    }

    private fun update(block: MasterEntryViewModel.State.() -> MasterEntryViewModel.State) =
        vm.update(block)

    override val isEditMode get() = _state.mode == MasterEntryViewModel.Mode.EDIT
    override val isFormValid get() = _state.name.isNotBlank() && _state.price.isNotBlank()
    override val unitOptions
        get() = when (_state.entryType) {
            EntryType.GOODS -> listOf("cup", "plate", "bowl", "piece", "skewer", "bottle", "pack", "kg", "box")
            EntryType.SERVICE -> listOf("session", "person", "hour", "course", "class", "month")
            EntryType.RENTAL -> listOf("hour", "court", "field", "table", "room", "day")
        }

    override var entryType: EntryType
        get() = _state.entryType
        set(value) = update {
            copy(
                entryType = value,
                unit = when (value) {
                    EntryType.GOODS -> "piece"
                    EntryType.SERVICE -> "hour"
                    EntryType.RENTAL -> "hour"
                },
            )
        }

    override var productId: String?
        get() = _state.productId
        set(value) = update { copy(productId = value) }

    override var name: String
        get() = _state.name
        set(value) = update { copy(name = value) }

    override var category: String
        get() = _state.category
        set(value) = update { copy(category = value) }

    override var subCategory: String?
        get() = _state.subCategory
        set(value) = update { copy(subCategory = value) }

    override var description: String
        get() = _state.description
        set(value) = update { copy(description = value) }

    override var isAvailable: Boolean
        get() = _state.isAvailable
        set(value) = update { copy(isAvailable = value) }

    override var isRecommended: Boolean
        get() = _state.isRecommended
        set(value) = update { copy(isRecommended = value) }

    override var price: String
        get() = _state.price
        set(value) = update { copy(price = value) }

    override var unit: String
        get() = _state.unit
        set(value) = update { copy(unit = value) }

    override var chargeVat: Boolean
        get() = _state.chargeVat
        set(value) = update { copy(chargeVat = value) }

    override var costPrice: String
        get() = _state.costPrice
        set(value) = update { copy(costPrice = value) }

    override var showCostPrice: Boolean
        get() = _state.showCostPrice
        set(value) = update { copy(showCostPrice = value) }

    override var openPrice: Boolean
        get() = _state.openPrice
        set(value) = update { copy(openPrice = value) }

    override var chargedBy: Int
        get() = _state.chargedBy
        set(value) = update { copy(chargedBy = value) }

    override var bookingDuration: String
        get() = _state.bookingDuration
        set(value) = update { copy(bookingDuration = value) }

    override var minBooking: String
        get() = _state.minBooking
        set(value) = update { copy(minBooking = value) }

    override var trackStock: Boolean
        get() = _state.trackStock
        set(value) = update { copy(trackStock = value) }

    override var openingStock: String
        get() = _state.openingStock
        set(value) = update { copy(openingStock = value) }

    override var maxCapacity: String
        get() = _state.maxCapacity
        set(value) = update { copy(maxCapacity = value) }

    override var lowStockAlert: String
        get() = _state.lowStockAlert
        set(value) = update { copy(lowStockAlert = value) }

    override var supplier: String
        get() = _state.supplier
        set(value) = update { copy(supplier = value) }

    override var trackActive: Boolean
        get() = _state.trackActive
        set(value) = update { copy(trackActive = value) }

    override var serviceCapacity: String
        get() = _state.serviceCapacity
        set(value) = update { copy(serviceCapacity = value) }

    override var serviceDuration: String
        get() = _state.serviceDuration
        set(value) = update { copy(serviceDuration = value) }

    override var serviceOpens: TimeValue
        get() = _state.serviceOpens
        set(value) = update { copy(serviceOpens = value) }

    override var serviceCloses: TimeValue
        get() = _state.serviceCloses
        set(value) = update { copy(serviceCloses = value) }

    override var activeDays: Set<Int>
        get() = _state.activeDays
        set(value) = update { copy(activeDays = value) }

    override var instructor: String
        get() = _state.instructor
        set(value) = update { copy(instructor = value) }

    override var serviceRequiresBooking: Boolean
        get() = _state.serviceRequiresBooking
        set(value) = update { copy(serviceRequiresBooking = value) }

    override var rentalUnitsCount: String
        get() = _state.rentalUnitsCount
        set(value) = update { copy(rentalUnitsCount = value) }

    override var rentalBuffer: String
        get() = _state.rentalBuffer
        set(value) = update { copy(rentalBuffer = value) }

    override var rentalOpens: TimeValue
        get() = _state.rentalOpens
        set(value) = update { copy(rentalOpens = value) }

    override var rentalCloses: TimeValue
        get() = _state.rentalCloses
        set(value) = update { copy(rentalCloses = value) }

    override var depositAmount: String
        get() = _state.depositAmount
        set(value) = update { copy(depositAmount = value) }

    override var rentalRequiresBooking: Boolean
        get() = _state.rentalRequiresBooking
        set(value) = update { copy(rentalRequiresBooking = value) }

    override var optionGroups: List<OptionGroup>
        get() = _state.optionGroups
        set(value) = update { copy(optionGroups = value) }

    override var advancedExpanded: Boolean
        get() = _state.advancedExpanded
        set(value) = update { copy(advancedExpanded = value) }

    override var sku: String
        get() = _state.sku
        set(value) = update { copy(sku = value) }

    override var barcode: String
        get() = _state.barcode
        set(value) = update { copy(barcode = value) }

    override var sendOrderTo: String
        get() = _state.sendOrderTo
        set(value) = update { copy(sendOrderTo = value) }

    override var displayOrder: String
        get() = _state.displayOrder
        set(value) = update { copy(displayOrder = value) }

    override val effect: SharedFlow<MasterEntryViewModel.SideEffect> get() = vm.effect

    override fun save() = vm.save()
    override fun discard() = vm.discard()
}