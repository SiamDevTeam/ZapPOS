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
    override val isFormValid get() = _state.detail.name.isNotBlank() && _state.pricing.price.isNotBlank()
    override val unitOptions
        get() = when (_state.entryType) {
            EntryType.GOODS   -> listOf("cup", "plate", "bowl", "piece", "skewer", "bottle", "pack", "kg", "box")
            EntryType.SERVICE -> listOf("session", "person", "hour", "course", "class", "month")
            EntryType.RENTAL  -> listOf("hour", "court", "field", "table", "room", "day")
        }

    // ── Identity ──────────────────────────────────────────────────────────────

    override var entryType: EntryType
        get() = _state.entryType
        set(value) = update {
            copy(
                entryType = value,
                pricing = pricing.copy(
                    unit = when (value) {
                        EntryType.GOODS   -> "piece"
                        EntryType.SERVICE -> "hour"
                        EntryType.RENTAL  -> "hour"
                    },
                ),
            )
        }

    override var productId: String?
        get() = _state.productId
        set(value) = update { copy(productId = value) }

    // ── Detail ────────────────────────────────────────────────────────────────

    override var name: String
        get() = _state.detail.name
        set(value) = update { copy(detail = detail.copy(name = value)) }

    override var category: String
        get() = _state.detail.category
        set(value) = update { copy(detail = detail.copy(category = value)) }

    override var subCategory: String?
        get() = _state.detail.subCategory
        set(value) = update { copy(detail = detail.copy(subCategory = value)) }

    override var description: String
        get() = _state.detail.description
        set(value) = update { copy(detail = detail.copy(description = value)) }

    override var isAvailable: Boolean
        get() = _state.detail.isAvailable
        set(value) = update { copy(detail = detail.copy(isAvailable = value)) }

    override var isRecommended: Boolean
        get() = _state.detail.isRecommended
        set(value) = update { copy(detail = detail.copy(isRecommended = value)) }

    // ── Pricing ───────────────────────────────────────────────────────────────

    override var price: String
        get() = _state.pricing.price
        set(value) = update { copy(pricing = pricing.copy(price = value)) }

    override var unit: String
        get() = _state.pricing.unit
        set(value) = update { copy(pricing = pricing.copy(unit = value)) }

    override var chargeVat: Boolean
        get() = _state.pricing.chargeVat
        set(value) = update { copy(pricing = pricing.copy(chargeVat = value)) }

    override var costPrice: String
        get() = _state.pricing.costPrice
        set(value) = update { copy(pricing = pricing.copy(costPrice = value)) }

    override var openPrice: Boolean
        get() = _state.pricing.openPrice
        set(value) = update { copy(pricing = pricing.copy(openPrice = value)) }

    override var chargedBy: Int
        get() = _state.pricing.chargedBy
        set(value) = update { copy(pricing = pricing.copy(chargedBy = value)) }

    override var bookingDuration: String
        get() = _state.pricing.bookingDuration
        set(value) = update { copy(pricing = pricing.copy(bookingDuration = value)) }

    override var minBooking: String
        get() = _state.pricing.minBooking
        set(value) = update { copy(pricing = pricing.copy(minBooking = value)) }

    // ── UI-only ───────────────────────────────────────────────────────────────

    override var showCostPrice: Boolean
        get() = _state.showCostPrice
        set(value) = update { copy(showCostPrice = value) }

    override var advancedExpanded: Boolean
        get() = _state.advancedExpanded
        set(value) = update { copy(advancedExpanded = value) }

    // ── Inventory ─────────────────────────────────────────────────────────────

    override var trackStock: Boolean
        get() = _state.inventory.trackStock
        set(value) = update { copy(inventory = inventory.copy(trackStock = value)) }

    override var openingStock: String
        get() = _state.inventory.openingStock
        set(value) = update { copy(inventory = inventory.copy(openingStock = value)) }

    override var maxCapacity: String
        get() = _state.inventory.maxCapacity
        set(value) = update { copy(inventory = inventory.copy(maxCapacity = value)) }

    override var lowStockAlert: String
        get() = _state.inventory.lowStockAlert
        set(value) = update { copy(inventory = inventory.copy(lowStockAlert = value)) }

    override var supplier: String
        get() = _state.inventory.supplier
        set(value) = update { copy(inventory = inventory.copy(supplier = value)) }

    override var trackActive: Boolean
        get() = _state.inventory.trackActive
        set(value) = update { copy(inventory = inventory.copy(trackActive = value)) }

    // ── Service ───────────────────────────────────────────────────────────────

    override var serviceCapacity: String
        get() = _state.service.capacity
        set(value) = update { copy(service = service.copy(capacity = value)) }

    override var serviceDuration: String
        get() = _state.service.duration
        set(value) = update { copy(service = service.copy(duration = value)) }

    override var serviceOpens: TimeValue
        get() = _state.service.opens
        set(value) = update { copy(service = service.copy(opens = value)) }

    override var serviceCloses: TimeValue
        get() = _state.service.closes
        set(value) = update { copy(service = service.copy(closes = value)) }

    override var activeDays: Set<Int>
        get() = _state.service.activeDays
        set(value) = update { copy(service = service.copy(activeDays = value)) }

    override var instructor: String
        get() = _state.service.instructor
        set(value) = update { copy(service = service.copy(instructor = value)) }

    override var serviceRequiresBooking: Boolean
        get() = _state.service.requiresBooking
        set(value) = update { copy(service = service.copy(requiresBooking = value)) }

    // ── Rental ────────────────────────────────────────────────────────────────

    override var rentalUnitsCount: String
        get() = _state.rental.unitsCount
        set(value) = update { copy(rental = rental.copy(unitsCount = value)) }

    override var rentalBuffer: String
        get() = _state.rental.buffer
        set(value) = update { copy(rental = rental.copy(buffer = value)) }

    override var rentalOpens: TimeValue
        get() = _state.rental.opens
        set(value) = update { copy(rental = rental.copy(opens = value)) }

    override var rentalCloses: TimeValue
        get() = _state.rental.closes
        set(value) = update { copy(rental = rental.copy(closes = value)) }

    override var depositAmount: String
        get() = _state.rental.depositAmount
        set(value) = update { copy(rental = rental.copy(depositAmount = value)) }

    override var rentalRequiresBooking: Boolean
        get() = _state.rental.requiresBooking
        set(value) = update { copy(rental = rental.copy(requiresBooking = value)) }

    // ── Options ───────────────────────────────────────────────────────────────

    override var optionGroups: List<OptionGroup>
        get() = _state.options
        set(value) = update { copy(options = value) }

    // ── Advanced ──────────────────────────────────────────────────────────────

    override var sku: String
        get() = _state.advanced.sku
        set(value) = update { copy(advanced = advanced.copy(sku = value)) }

    override var barcode: String
        get() = _state.advanced.barcode
        set(value) = update { copy(advanced = advanced.copy(barcode = value)) }

    override var sendOrderTo: String
        get() = _state.advanced.sendOrderTo
        set(value) = update { copy(advanced = advanced.copy(sendOrderTo = value)) }

    override var displayOrder: String
        get() = _state.advanced.displayOrder
        set(value) = update { copy(advanced = advanced.copy(displayOrder = value)) }

    // ── Effects & actions ─────────────────────────────────────────────────────

    override val effect: SharedFlow<MasterEntryViewModel.SideEffect> get() = vm.effect

    override fun save() = vm.save()
    override fun discard() = vm.discard()
}