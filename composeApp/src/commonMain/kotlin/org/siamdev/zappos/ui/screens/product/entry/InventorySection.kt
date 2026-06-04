/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.NumberUnitField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.components.common.ToggleItem

@Composable
internal fun InventorySection(state: EntryFormState) {
    val sellUnit = state.unit.ifEmpty { "unit" }

    SectionCard(
        icon = Icons.Default.Inventory,
        title = "Stock notification",
        subtitle = "Tap to enable quantity tracking",
        expanded = state.trackStock,
        onExpandChange = { state.trackStock = it },
    ) {
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

        EntryField(
            value = state.supplier,
            onValueChange = { state.supplier = it },
            label = "Supplier / vendor",
            placeholder = "Name or company",
        )

        ToggleItem(
            icon = Icons.Default.Notifications,
            label = "Expiry alerts",
            subtitle = if (state.trackExpiry) "Active"
                       else "Inactive",
            checked = state.trackExpiry,
            onCheckedChange = { state.trackExpiry = it },
        )
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