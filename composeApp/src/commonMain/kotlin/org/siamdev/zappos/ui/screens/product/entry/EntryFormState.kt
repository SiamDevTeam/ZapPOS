/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.siamdev.zappos.data.source.EventKind
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.utils.DateTimeUtils
import org.siamdev.zappos.utils.TimeValue

enum class EntryType {
    GOODS,
    SERVICE,
    RENTAL
}

enum class PickMode {
    ONE,
    MANY
}

data class OptionItem(
    val id: Long = DateTimeUtils.nowEpochMillis(),
    val name: String = "",
    val priceModifier: Int = 0,
)

data class OptionGroup(
    val id: Long = DateTimeUtils.nowEpochMillis(),
    val name: String = "Size",
    val pickMode: PickMode = PickMode.ONE,
    val required: Boolean = false,
    val items: List<OptionItem> = emptyList(),
)

internal val entryTabs: List<TabItem> =
    listOf(
        TabItem("Goods", Icons.Default.Inventory),
        TabItem("Service", Icons.Default.Person),
        TabItem("Rental", Icons.Default.Schedule),
    )

@Stable
internal class EntryFormState : MasterEntrySurface {
    // type
    override var entryType by mutableStateOf(EntryType.GOODS)

    // identity
    override var productId by mutableStateOf<String?>(null)

    // product details
    override var name by mutableStateOf("")
    override var category by mutableStateOf("")
    override var subCategory by mutableStateOf<String?>(null)
    override var description by mutableStateOf("")
    override var isAvailable by mutableStateOf(true)
    override var isRecommended by mutableStateOf(false)

    // pricing – shared
    override var price by mutableStateOf("")
    override var unit by mutableStateOf("piece")
    override var chargeVat by mutableStateOf(true)
    override var costPrice by mutableStateOf("")
    override var showCostPrice by mutableStateOf(false)

    // pricing – goods only
    override var openPrice by mutableStateOf(false)

    // pricing – service only  (0 = per person, 1 = per session, 2 = per hour)
    override var chargedBy by mutableStateOf(2)

    // pricing – rental only
    override var bookingDuration by mutableStateOf("60")
    override var minBooking by mutableStateOf("1")

    // inventory (goods)
    override var trackStock by mutableStateOf(true)
    override var openingStock by mutableStateOf("0")
    override var maxCapacity by mutableStateOf("0")
    override var lowStockAlert by mutableStateOf("0")
    override var supplier by mutableStateOf("")
    override var trackActive by mutableStateOf(false)

    // schedule & capacity (service)
    override var serviceCapacity by mutableStateOf("12")
    override var serviceDuration by mutableStateOf("60")
    override var serviceOpens by mutableStateOf(TimeValue(9, 0))
    override var serviceCloses by mutableStateOf(TimeValue(18, 0))
    override var activeDays by mutableStateOf(setOf(0, 1, 2, 3, 4))
    override var instructor by mutableStateOf("")
    override var serviceRequiresBooking by mutableStateOf(false)

    // resources & booking (rental)
    override var rentalUnitsCount by mutableStateOf("2")
    override var rentalBuffer by mutableStateOf("0")
    override var rentalOpens by mutableStateOf(TimeValue(8, 0))
    override var rentalCloses by mutableStateOf(TimeValue(22, 0))
    override var depositAmount by mutableStateOf("0.00")
    override var rentalRequiresBooking by mutableStateOf(true)

    // options & add-ons
    override var optionGroups by mutableStateOf(emptyList<OptionGroup>())

    // advanced
    override var advancedExpanded by mutableStateOf(false)
    override var sku by mutableStateOf("")
    override var barcode by mutableStateOf("")
    override var sendOrderTo by mutableStateOf("None")
    override var displayOrder by mutableStateOf("0")

    override val isFormValid: Boolean
        get() = name.isNotBlank() && price.isNotBlank()

    override val unitOptions: List<String>
        get() =
            when (entryType) {
                EntryType.GOODS ->
                    listOf("cup", "plate", "bowl", "piece", "skewer", "bottle", "pack", "kg", "box")
                EntryType.SERVICE -> listOf("session", "person", "hour", "course", "class", "month")
                EntryType.RENTAL -> listOf("hour", "court", "field", "table", "room", "day")
            }

    override val isEditMode: Boolean get() = false

    override val effect: SharedFlow<MasterEntryViewModel.SideEffect> =
        MutableSharedFlow()

    override fun save() = Unit
    override fun discard() = Unit
}

internal fun EntryFormState.loadFrom(event: MasterEvent) {
    productId = event.id
    entryType =
        when (event.kind) {
            EventKind.SERVICE -> EntryType.SERVICE
            EventKind.RENTAL -> EntryType.RENTAL
            else -> EntryType.GOODS
        }
    name = event.name
    category = event.category
    subCategory = event.subCategory
    description = event.description
    isAvailable = event.isAvailable
    isRecommended = event.isRecommended
    price = event.price.toLong().toString()
    unit = event.unit
    sku = event.sku
    chargeVat = event.chargeVat
    openPrice = event.openPrice
    supplier = event.supplier
    // Local vals required: member properties with custom getters cannot be smart-cast
    val costPriceVal = event.costPrice
    val stockQtyVal = event.stockQty
    val stockMaxVal = event.stockMax
    val lowStockAlertVal = event.lowStockAlert
    if (costPriceVal != null) {
        showCostPrice = true
        costPrice = costPriceVal.toLong().toString()
    }
    trackStock = stockQtyVal != null
    if (stockQtyVal != null) openingStock = stockQtyVal.toString()
    if (stockMaxVal != null) maxCapacity = stockMaxVal.toString()
    if (lowStockAlertVal != null) lowStockAlert = lowStockAlertVal.toString()
    optionGroups =
        event.optionGroups.map { group ->
            OptionGroup(
                name = group.name,
                pickMode = if (group.multiSelect) PickMode.MANY else PickMode.ONE,
                required = group.required,
                items =
                    group.items.map {
                        OptionItem(name = it.name, priceModifier = it.priceModifier)
                    },
            )
        }
}

@Composable
internal fun rememberEntryFormState(): EntryFormState {
    val state = remember { EntryFormState() }
    LaunchedEffect(state.entryType) {
        state.unit =
            when (state.entryType) {
                EntryType.GOODS -> "piece"
                EntryType.SERVICE -> "hour"
                EntryType.RENTAL -> "hour"
            }
    }
    return state
}