/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.NumberUnitField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.components.common.ToggleItem
import org.siamdev.zappos.ui.components.picker.TimeRangeRow
import org.siamdev.zappos.ui.screens.product.entry.EntryFormState
import org.siamdev.zappos.ui.screens.product.entry.EntryType
import org.siamdev.zappos.ui.screens.product.entry.rememberEntryFormState

@Composable
internal fun ResourcesBookingSection(state: EntryFormState) {
    SectionCard(
        icon = Icons.Default.Business,
        title = "Resources & booking",
        subtitle = "Units available and operating window",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberUnitField(
                value = state.rentalUnitsCount,
                onValueChange = { state.rentalUnitsCount = it },
                label = "Units available",
                unitLabel = state.unit.ifEmpty { "unit" },
                modifier = Modifier.weight(1f),
            )
            NumberUnitField(
                value = state.rentalBuffer,
                onValueChange = { state.rentalBuffer = it },
                label = "Buffer between slots",
                unitLabel = "min",
                modifier = Modifier.weight(1f),
            )
        }

        TimeRangeRow(
            opensValue = state.rentalOpens,
            closesValue = state.rentalCloses,
            onOpensChange = { state.rentalOpens = it },
            onClosesChange = { state.rentalCloses = it },
        )

        EntryField(
            value = state.depositAmount,
            onValueChange = { state.depositAmount = it.filter { c -> c.isDigit() || c == '.' } },
            label = "Deposit",
            placeholder = "0.00",
            keyboardType = KeyboardType.Decimal,
            prefix = { Text("฿") },
        )

        ToggleItem(
            icon = Icons.Default.Event,
            label = "Requires booking",
            subtitle = "Customers must reserve a slot in advance",
            checked = state.rentalRequiresBooking,
            onCheckedChange = { state.rentalRequiresBooking = it },
        )
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun ResourcesBookingSectionPreview() {
    MaterialTheme {
        ResourcesBookingSection(rememberEntryFormState().also { it.entryType = EntryType.RENTAL })
    }
}