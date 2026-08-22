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
import org.siamdev.zappos.data.source.local.LocalSources
import org.siamdev.zappos.data.source.local.model.InventoryConfig
import org.siamdev.zappos.data.source.local.model.OptionGroupModel
import org.siamdev.zappos.data.source.local.model.OptionItemModel
import org.siamdev.zappos.data.source.local.model.ProductModel
import org.siamdev.zappos.data.source.local.model.RentalConfig
import org.siamdev.zappos.data.source.local.model.ServiceConfig
import org.siamdev.zappos.utils.DateTimeUtils
import org.siamdev.zappos.utils.TimeValue

class MasterEntryViewModel : ViewModel() {

    enum class Mode { CREATE, EDIT }

    data class State(
        val mode: Mode = Mode.CREATE,
        val isLoading: Boolean = false,
        val entryType: EntryType = EntryType.GOODS,
        val productId: String? = null,
        val showCostPrice: Boolean = false,
        val advancedExpanded: Boolean = false,
        val detail: Detail = Detail(),
        val pricing: Pricing = Pricing(),
        val inventory: Inventory = Inventory(),
        val service: Service = Service(),
        val rental: Rental = Rental(),
        val options: List<OptionGroup> = emptyList(),
        val advanced: Advanced = Advanced(),
    ) {
        data class Detail(
            val name: String = "",
            val category: String = "",
            val subCategory: String? = null,
            val description: String = "",
            val isAvailable: Boolean = true,
            val isRecommended: Boolean = false,
        )

        data class Pricing(
            val price: String = "",
            val unit: String = "piece",
            val chargeVat: Boolean = true,
            val costPrice: String = "",
            val openPrice: Boolean = false,
            val chargedBy: Int = 2,
            val bookingDuration: String = "60",
            val minBooking: String = "1",
        )

        data class Inventory(
            val trackStock: Boolean = true,
            val openingStock: String = "0",
            val maxCapacity: String = "0",
            val lowStockAlert: String = "0",
            val supplier: String = "",
            val trackActive: Boolean = false,
        )

        data class Service(
            val capacity: String = "12",
            val duration: String = "60",
            val opens: TimeValue = TimeValue(9, 0),
            val closes: TimeValue = TimeValue(18, 0),
            val activeDays: Set<Int> = setOf(0, 1, 2, 3, 4),
            val instructor: String = "",
            val requiresBooking: Boolean = false,
        )

        data class Rental(
            val unitsCount: String = "2",
            val buffer: String = "0",
            val opens: TimeValue = TimeValue(8, 0),
            val closes: TimeValue = TimeValue(22, 0),
            val depositAmount: String = "0.00",
            val requiresBooking: Boolean = true,
        )

        data class Advanced(
            val sku: String = "",
            val barcode: String = "",
            val sendOrderTo: String = "None",
            val displayOrder: String = "0",
        )
    }

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
            val product = LocalSources.product?.getById(productId)
            _state.update { if (product != null) it.loadFrom(product) else it.copy(isLoading = false) }
        }
    }

    fun update(block: State.() -> State) = _state.update { it.block() }

    fun save() {
        viewModelScope.launch {
            val model = _state.value.toProductModel()
            val result = LocalSources.product?.save(model)
            if (result == null || result.isSuccess) {
                _effect.emit(SideEffect.SaveSuccess)
            } else {
                _effect.emit(
                    SideEffect.SaveError(
                        result.exceptionOrNull()?.message ?: "Save failed"
                    )
                )
            }
        }
    }

    fun discard() {
        viewModelScope.launch {
            _effect.emit(SideEffect.NavigateBack)
        }
    }
}

private fun MasterEntryViewModel.State.loadFrom(product: ProductModel): MasterEntryViewModel.State {
    val entryType = when (product.kind) {
        "1001" -> EntryType.SERVICE
        "1002" -> EntryType.RENTAL
        else -> EntryType.GOODS
    }
    val inv = product.inventory
    val svc = product.service
    val rnt = product.rental
    return copy(
        mode = MasterEntryViewModel.Mode.EDIT,
        isLoading = false,
        entryType = entryType,
        productId = product.id,
        showCostPrice = product.costPrice != null,
        detail = detail.copy(
            name = product.name,
            category = product.catId,
            description = product.description ?: "",
            isAvailable = product.isAvailable,
            isRecommended = product.isRecommended,
        ),
        pricing = pricing.copy(
            price = product.price.toLong().toString(),
            unit = product.unit,
            chargeVat = product.chargeVat,
            openPrice = product.openPrice,
            costPrice = product.costPrice?.toLong()?.toString() ?: "",
            chargedBy = svc?.chargedBy?.toIntOrNull() ?: rnt?.let { 2 } ?: 2,
            bookingDuration = svc?.durationMin?.toString() ?: rnt?.slotDurationMin?.toString()
            ?: "60",
            minBooking = rnt?.minBooking?.toString() ?: "1",
        ),
        inventory = if (inv != null) inventory.copy(
            trackStock = inv.trackStock,
            maxCapacity = inv.maxCapacity.toLong().toString(),
            lowStockAlert = inv.lowStockAlert.toLong().toString(),
            supplier = inv.supplierName ?: "",
            trackActive = inv.trackExpiry,
        ) else inventory,
        service = if (svc != null) service.copy(
            capacity = svc.capacity.toString(),
            duration = svc.durationMin.toString(),
            opens = TimeValue(svc.opensHh, svc.opensMm),
            closes = TimeValue(svc.closesHh, svc.closesMm),
            activeDays = svc.activeDays,
            instructor = svc.instructor ?: "",
            requiresBooking = svc.requiresBooking,
        ) else service,
        rental = if (rnt != null) rental.copy(
            unitsCount = rnt.unitsCount.toString(),
            buffer = rnt.bufferMin.toString(),
            opens = TimeValue(rnt.opensHh, rnt.opensMm),
            closes = TimeValue(rnt.closesHh, rnt.closesMm),
            depositAmount = rnt.depositAmt.toString(),
            requiresBooking = rnt.requiresBooking,
        ) else rental,
        advanced = advanced.copy(
            sku = product.sku ?: "",
            barcode = product.barcode ?: "",
            sendOrderTo = product.sendOrderTo ?: "None",
            displayOrder = product.displayOrder.toString(),
        ),
        options = product.optionGroups.map { group ->
            OptionGroup(
                id = group.id,
                name = group.name,
                pickMode = if (group.pickMode == "02") PickMode.MANY else PickMode.ONE,
                required = group.required,
                items = group.items.map {
                    OptionItem(
                        id = it.id,
                        name = it.name,
                        priceModifier = it.priceModifier.toInt()
                    )
                },
            )
        },
    )
}

private fun MasterEntryViewModel.State.toProductModel(): ProductModel {
    val kind = when (entryType) {
        EntryType.GOODS -> "1000"
        EntryType.SERVICE -> "1001"
        EntryType.RENTAL -> "1002"
    }
    val inv = inventory
    val svc = service
    val rnt = rental
    return ProductModel(
        id = productId ?: "prd-${DateTimeUtils.nowEpochMillis()}",
        kind = kind,
        catId = detail.category,
        taxId = "",
        name = detail.name,
        description = detail.description.ifBlank { null },
        unit = pricing.unit,
        price = pricing.price.toDoubleOrNull() ?: 0.0,
        costPrice = if (showCostPrice) pricing.costPrice.toDoubleOrNull() else null,
        chargeVat = pricing.chargeVat,
        openPrice = pricing.openPrice,
        sku = advanced.sku.ifBlank { null },
        barcode = advanced.barcode.ifBlank { null },
        sendOrderTo = advanced.sendOrderTo.takeIf { it != "None" },
        displayOrder = advanced.displayOrder.toIntOrNull() ?: 0,
        isAvailable = detail.isAvailable,
        isRecommended = detail.isRecommended,
        isActive = true,
        inventory = if (entryType == EntryType.GOODS) InventoryConfig(
            trackStock = inv.trackStock,
            maxCapacity = inv.maxCapacity.toDoubleOrNull() ?: 0.0,
            lowStockAlert = inv.lowStockAlert.toDoubleOrNull() ?: 0.0,
            supplierName = inv.supplier.ifBlank { null },
            trackExpiry = inv.trackActive,
        ) else null,
        service = if (entryType == EntryType.SERVICE) ServiceConfig(
            chargedBy = pricing.chargedBy.toString().padStart(2, '0'),
            capacity = svc.capacity.toIntOrNull() ?: 12,
            durationMin = svc.duration.toIntOrNull() ?: 60,
            opensHh = svc.opens.hour,
            opensMm = svc.opens.minute,
            closesHh = svc.closes.hour,
            closesMm = svc.closes.minute,
            activeDays = svc.activeDays,
            instructor = svc.instructor.ifBlank { null },
            requiresBooking = svc.requiresBooking,
        ) else null,
        rental = if (entryType == EntryType.RENTAL) RentalConfig(
            unitsCount = rnt.unitsCount.toIntOrNull() ?: 2,
            slotDurationMin = pricing.bookingDuration.toIntOrNull() ?: 60,
            minBooking = pricing.minBooking.toIntOrNull() ?: 1,
            bufferMin = rnt.buffer.toIntOrNull() ?: 0,
            opensHh = rnt.opens.hour,
            opensMm = rnt.opens.minute,
            closesHh = rnt.closes.hour,
            closesMm = rnt.closes.minute,
            depositAmt = rnt.depositAmount.toDoubleOrNull() ?: 0.0,
            requiresBooking = rnt.requiresBooking,
        ) else null,
        media = emptyList(),
        optionGroups = options.mapIndexed { index, group ->
            OptionGroupModel(
                id = group.id,
                name = group.name,
                pickMode = if (group.pickMode == PickMode.MANY) "02" else "01",
                required = group.required,
                sortOrder = index,
                items = group.items.mapIndexed { itemIndex, item ->
                    OptionItemModel(
                        id = item.id,
                        name = item.name,
                        priceModifier = item.priceModifier.toDouble(),
                        sortOrder = itemIndex,
                    )
                },
            )
        },
    )
}