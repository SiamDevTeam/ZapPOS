/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.NumberUnitField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.components.common.SectionHeader
import org.siamdev.zappos.ui.components.common.ToggleItem

@Composable
internal fun InventorySection(state: EntryFormState) {
    val sellUnit = state.unit.ifEmpty { "unit" }

    SectionCard(
        icon = Icons.Default.Inventory,
        title = "Track stock",
        subtitle = "Tap to enable quantity tracking and reorder rules",
        expanded = state.trackStock,
        onExpandChange = { state.trackStock = it },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SectionHeader("Stock levels")
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumberUnitField(
                        value = state.openingStock,
                        onValueChange = { state.openingStock = it },
                        label = "Opening stock",
                        unitLabel = sellUnit,
                        modifier = Modifier.weight(1f),
                    )
                    NumberUnitField(
                        value = state.maxCapacity,
                        onValueChange = { state.maxCapacity = it },
                        label = "Max capacity",
                        unitLabel = sellUnit,
                        modifier = Modifier.weight(1f),
                    )
                }
                NumberUnitField(
                    value = state.lowStockAlert,
                    onValueChange = { state.lowStockAlert = it },
                    label = "Low stock alert",
                    unitLabel = sellUnit,
                )
            }

            SectionHeader("Reorder")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberUnitField(
                    value = state.reorderPoint,
                    onValueChange = { state.reorderPoint = it },
                    label = "Reorder point",
                    unitLabel = sellUnit,
                    modifier = Modifier.weight(1f),
                )
                NumberUnitField(
                    value = state.reorderQty,
                    onValueChange = { state.reorderQty = it },
                    label = "Reorder quantity",
                    unitLabel = sellUnit,
                    modifier = Modifier.weight(1f),
                )
            }

            SectionHeader("Purchase & conversion")
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EntryField(
                        value = state.purchaseUnit,
                        onValueChange = { state.purchaseUnit = it },
                        label = "Purchase unit",
                        placeholder = "e.g. box, case, kg",
                        modifier = Modifier.weight(1f),
                    )
                    NumberUnitField(
                        value = state.unitsPerPurchase,
                        onValueChange = { state.unitsPerPurchase = it },
                        label = "Units per purchase",
                        unitLabel = sellUnit,
                        modifier = Modifier.weight(1f),
                    )
                }
                val conversionHint =
                    remember(state.purchaseUnit, state.unitsPerPurchase, sellUnit) {
                        val qty = state.unitsPerPurchase.toIntOrNull()
                        val pu = state.purchaseUnit.trim()
                        if (qty != null && qty > 1 && pu.isNotEmpty()) "1 $pu = $qty $sellUnit" else null
                    }
                if (conversionHint != null) {
                    Text(
                        text = conversionHint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            SectionHeader("Supplier")
            EntryField(
                value = state.supplier,
                onValueChange = { state.supplier = it },
                label = "Supplier / vendor",
                placeholder = "Name or company supplying this item",
            )

            ToggleItem(
                icon = Icons.Default.DateRange,
                label = "Track expiry dates",
                subtitle = "Record batch expiry — useful for perishable goods",
                checked = state.trackExpiry,
                onCheckedChange = { state.trackExpiry = it },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411, name = "Inventory · tracking on")
@Composable
private fun InventorySectionPreview() {
    MaterialTheme { InventorySection(rememberEntryFormState()) }
}

@Preview(showBackground = true, widthDp = 411, name = "Inventory · tracking off")
@Composable
private fun InventorySectionNoTrackPreview() {
    MaterialTheme { InventorySection(rememberEntryFormState().also { it.trackStock = false }) }
}
