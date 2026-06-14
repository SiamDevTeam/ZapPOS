/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.screens.product.entry.EntryFormState
import org.siamdev.zappos.ui.screens.product.entry.EntryType
import org.siamdev.zappos.utils.formatPrice

@Composable
internal fun ItemInfoCard(state: EntryFormState) {
    val catEntry = DefaultProductCategories.find { it.id == state.category }
    val categoryDisplay = run {
        val cat = catEntry?.name ?: state.category.ifBlank { null } ?: return@run null
        val sub = catEntry?.subCategories?.find { it.id == state.subCategory }?.name
        if (sub != null) "$cat · $sub" else cat
    }
    val priceValue = state.price.toDoubleOrNull() ?: 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).padding(10.dp)) {
            Text(
                "ITEM INFO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            val pairs = buildList<Pair<String, String>> {
                if (state.productId != null) add("Product ID" to state.productId!!)
                if (categoryDisplay != null) add("Category" to categoryDisplay)
                add("Price" to "${priceValue.formatPrice()} / ${state.unit}")
                if (state.openPrice && state.entryType == EntryType.GOODS) add("Pricing" to "Open / variable price")
                if (!state.chargeVat) add("VAT" to "Not charged")
                if (state.showCostPrice) {
                    val cost = state.costPrice.toDoubleOrNull() ?: 0.0
                    if (cost > 0) add("Cost price" to "฿ ${cost.formatPrice()} / ${state.unit}")
                }

                when (state.entryType) {
                    EntryType.GOODS -> {
                        if (state.trackStock) {
                            val qty = state.openingStock.toIntOrNull() ?: 0
                            val cap = state.maxCapacity.toIntOrNull() ?: 0
                            if (qty > 0 || cap > 0) add("Stock" to "$qty / $cap ${state.unit}")
                            val alert = state.lowStockAlert.toIntOrNull() ?: 0
                            if (alert > 0) add("Low stock alert" to "$alert ${state.unit}")
                        }
                        if (state.supplier.isNotBlank()) add("Supplier" to state.supplier)
                    }

                    EntryType.SERVICE -> {
                        val chargedByLabel = listOf("person", "session", "hour")
                            .getOrElse(state.chargedBy) { "hour" }
                        add("Charged per" to chargedByLabel)
                        val cap = state.serviceCapacity.toIntOrNull() ?: 0
                        if (cap > 0) add("Capacity" to "$cap people")
                        val dur = state.serviceDuration.toIntOrNull() ?: 0
                        if (dur > 0) add("Duration" to "$dur min / session")
                        add("Hours" to "${state.serviceOpens} – ${state.serviceCloses}")
                        if (state.activeDays.isNotEmpty()) {
                            val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            add(
                                "Active days" to state.activeDays.sorted()
                                    .joinToString(", ") { dayLabels.getOrElse(it) { "?" } })
                        }
                        if (state.instructor.isNotBlank()) add("Instructor" to state.instructor)
                        if (state.serviceRequiresBooking) add("Booking" to "Required")
                    }

                    EntryType.RENTAL -> {
                        val units = state.rentalUnitsCount.toIntOrNull() ?: 0
                        if (units > 0) add("Units available" to "$units")
                        val dur = state.bookingDuration.toIntOrNull() ?: 0
                        if (dur > 0) add("Slot duration" to "$dur min")
                        val minBook = state.minBooking.toIntOrNull() ?: 0
                        if (minBook > 1) add("Min. booking" to "$minBook slot(s)")
                        add("Hours" to "${state.rentalOpens} – ${state.rentalCloses}")
                        val buf = state.rentalBuffer.toIntOrNull() ?: 0
                        if (buf > 0) add("Buffer" to "$buf min")
                        val dep = state.depositAmount.toDoubleOrNull() ?: 0.0
                        if (dep > 0) add("Deposit" to "฿ ${dep.formatPrice()}")
                        if (state.rentalRequiresBooking) add("Booking" to "Required")
                    }
                }
            }

            pairs.forEachIndexed { i, (label, value) ->
                if (i > 0) HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        value,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            if (state.description.isNotBlank()) {
                if (pairs.isNotEmpty()) HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Text(
                    "Description",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    state.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemInfoCardPreview() {
    val state = EntryFormState().apply {
        name = "Matcha Latte"
        category = "beverages"
        subCategory = "tea_coffee"
        price = "85"
        unit = "cup"
        trackStock = true
        openingStock = "50"
        maxCapacity = "200"
        description = "Premium Japanese matcha with creamy milk."
    }
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            ItemInfoCard(state = state)
        }
    }
}
