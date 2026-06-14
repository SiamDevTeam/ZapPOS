/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.ChipRow
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.LabeledChipRow
import org.siamdev.zappos.ui.components.common.NumberUnitField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.screens.product.entry.EntryFormState
import org.siamdev.zappos.ui.screens.product.entry.rememberEntryFormState

private val orderDestinations = listOf("None", "Kitchen", "Bar", "Counter", "Grill")

@Composable
internal fun AdvancedSection(state: EntryFormState) {
    SectionCard(
        icon = Icons.Default.Settings,
        title = "Advanced",
        subtitle = "SKU, routing and display order",
        badge = "OPTIONAL",
        expanded = state.advancedExpanded,
        onExpandChange = { state.advancedExpanded = it },
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            EntryField(
                value = state.sku,
                onValueChange = { state.sku = it },
                label = "SKU",
                placeholder = "e.g. BEV-001",
                modifier = Modifier.weight(1f),
            )
            EntryField(
                value = state.barcode,
                onValueChange = { state.barcode = it },
                label = "Barcode",
                placeholder = "Scan or type",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f),
            )
        }

        LabeledChipRow(label = "Send order to") {
            ChipRow(
                items = orderDestinations,
                selected = state.sendOrderTo,
                onSelect = { state.sendOrderTo = it },
            )
        }

        NumberUnitField(
            value = state.displayOrder,
            onValueChange = { state.displayOrder = it },
            label = "Display order",
            unitLabel = "position",
        )
    }
}

@Preview(showBackground = true, widthDp = 411, name = "Advanced · collapsed")
@Composable
private fun AdvancedSectionCollapsedPreview() {
    MaterialTheme { AdvancedSection(rememberEntryFormState()) }
}

@Preview(showBackground = true, widthDp = 411, name = "Advanced · expanded")
@Composable
private fun AdvancedSectionExpandedPreview() {
    MaterialTheme { AdvancedSection(rememberEntryFormState().also { it.advancedExpanded = true }) }
}
