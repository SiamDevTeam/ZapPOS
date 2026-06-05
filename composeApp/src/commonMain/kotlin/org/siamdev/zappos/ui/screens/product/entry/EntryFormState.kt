/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.runtime.*
import org.siamdev.zappos.utils.TimeValue
import org.siamdev.zappos.utils.DateTimeUtils

internal enum class EntryType { GOODS, SERVICE, RENTAL }

internal enum class PickMode { ONE, MANY }

internal data class OptionItem(
    val id: Long = DateTimeUtils.nowEpochMillis(),
    val name: String = "",
    val priceModifier: Int = 0,
)

internal data class OptionGroup(
    val id: Long = DateTimeUtils.nowEpochMillis(),
    val name: String = "Size",
    val pickMode: PickMode = PickMode.ONE,
    val required: Boolean = false,
    val items: List<OptionItem> = emptyList(),
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
    var trackExpiry by mutableStateOf(false)

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

    val isFormValid: Boolean get() = name.isNotBlank() && price.isNotBlank()

    val unitOptions: List<String> get() =
        when (entryType) {
            EntryType.GOODS -> listOf("cup", "plate", "bowl", "piece", "skewer", "bottle", "pack", "kg", "box")
            EntryType.SERVICE -> listOf("session", "person", "hour", "course", "class", "month")
            EntryType.RENTAL -> listOf("hour", "court", "field", "table", "room", "day")
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
