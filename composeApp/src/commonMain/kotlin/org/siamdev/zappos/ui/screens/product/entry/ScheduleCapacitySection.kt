/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.EntryChip
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.NumberUnitField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.components.common.TimeField
import org.siamdev.zappos.ui.components.common.ToggleItem

private val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Composable
internal fun ScheduleCapacitySection(state: EntryFormState) {
    SectionCard(
        icon = Icons.Default.DateRange,
        title = "Schedule & capacity",
        subtitle = "When the service runs and how many it fits",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberUnitField(
                value = state.serviceCapacity,
                onValueChange = { state.serviceCapacity = it },
                label = "Capacity",
                unitLabel = "people",
                modifier = Modifier.weight(1f),
            )
            NumberUnitField(
                value = state.serviceDuration,
                onValueChange = { state.serviceDuration = it },
                label = "Duration",
                unitLabel = "min",
                modifier = Modifier.weight(1f),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeField(
                value = state.serviceOpens,
                onValueChange = { state.serviceOpens = it },
                label = "Opens",
                modifier = Modifier.weight(1f),
            )
            TimeField(
                value = state.serviceCloses,
                onValueChange = { state.serviceCloses = it },
                label = "Closes",
                modifier = Modifier.weight(1f),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Active days",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                dayLabels.forEachIndexed { index, label ->
                    EntryChip(
                        selected = index in state.activeDays,
                        onClick = {
                            state.activeDays =
                                if (index in state.activeDays) {
                                    state.activeDays - index
                                } else {
                                    state.activeDays + index
                                }
                        },
                        label = label,
                    )
                }
            }
        }

        EntryField(
            value = state.instructor,
            onValueChange = { state.instructor = it },
            label = "Instructor / host",
            placeholder = "Name of the person leading the session",
        )

        ToggleItem(
            icon = Icons.Default.Event,
            label = "Requires booking",
            subtitle = "Customers must book ahead — not walk-in",
            checked = state.serviceRequiresBooking,
            onCheckedChange = { state.serviceRequiresBooking = it },
        )
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun ScheduleCapacitySectionPreview() {
    MaterialTheme {
        ScheduleCapacitySection(rememberEntryFormState().also { it.entryType = EntryType.SERVICE })
    }
}
