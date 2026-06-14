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
import org.siamdev.zappos.data.source.EventKind
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.utils.DateTimeUtils
import org.siamdev.zappos.utils.TimeValue

internal enum class EntryType {
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
internal class EntryFormState {
    // type
    var entryType by mutableStateOf(EntryType.GOODS)

    // identity — null in create mode; set after save or when editing an existing item
    var productId by mutableStateOf<String?>(null)

    // product details
    var name by mutableStateOf("")
    var category by mutableStateOf("")
    var subCategory by mutableStateOf<String?>(null)
    var description by mutableStateOf("")
    var isAvailable by mutableStateOf(true)
    var isRecommended by mutableStateOf(false)

    // pricing – shared
    var price by mutableStateOf("")
    var unit by mutableStateOf("piece")
    var chargeVat by mutableStateOf(true)
    var costPrice by mutableStateOf("")
    var showCostPrice by mutableStateOf(false)

    // pricing – goods only
    var openPrice by mutableStateOf(false)

    // pricing – service only  (0 = per person, 1 = per session, 2 = per hour)
    var chargedBy by mutableStateOf(2)

    // pricing – rental only
    var bookingDuration by mutableStateOf("60")
    var minBooking by mutableStateOf("1")

    // inventory (goods)
    var trackStock by mutableStateOf(true)
    var openingStock by mutableStateOf("0")
    var maxCapacity by mutableStateOf("0")
    var lowStockAlert by mutableStateOf("0")
    var supplier by mutableStateOf("")
    var trackActive by mutableStateOf(false)

    // schedule & capacity (service)
    var serviceCapacity by mutableStateOf("12")
    var serviceDuration by mutableStateOf("60")
    var serviceOpens by mutableStateOf(TimeValue(9, 0))
    var serviceCloses by mutableStateOf(TimeValue(18, 0))
    var activeDays by mutableStateOf(setOf(0, 1, 2, 3, 4))
    var instructor by mutableStateOf("")
    var serviceRequiresBooking by mutableStateOf(false)

    // resources & booking (rental)
    var rentalUnitsCount by mutableStateOf("2")
    var rentalBuffer by mutableStateOf("0")
    var rentalOpens by mutableStateOf(TimeValue(8, 0))
    var rentalCloses by mutableStateOf(TimeValue(22, 0))
    var depositAmount by mutableStateOf("0.00")
    var rentalRequiresBooking by mutableStateOf(true)

    // options & add-ons
    var optionGroups by mutableStateOf(emptyList<OptionGroup>())

    // advanced
    var advancedExpanded by mutableStateOf(false)
    var sku by mutableStateOf("")
    var barcode by mutableStateOf("")
    var sendOrderTo by mutableStateOf("None")
    var displayOrder by mutableStateOf("0")

    val isFormValid: Boolean
        get() = name.isNotBlank() && price.isNotBlank()

    val unitOptions: List<String>
        get() =
            when (entryType) {
                EntryType.GOODS ->
                    listOf("cup", "plate", "bowl", "piece", "skewer", "bottle", "pack", "kg", "box")
                EntryType.SERVICE -> listOf("session", "person", "hour", "course", "class", "month")
                EntryType.RENTAL -> listOf("hour", "court", "field", "table", "room", "day")
            }
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
