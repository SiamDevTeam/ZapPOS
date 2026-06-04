/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntryChip(selected: Boolean, onClick: () -> Unit, label: String) {
    FilterChip(
        selected = selected,
        onClick  = onClick,
        label    = { Text(label, style = MaterialTheme.typography.bodySmall) },
        colors   = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            selectedLabelColor     = MaterialTheme.colorScheme.onSurface
        ),
        border = if (selected)
            FilterChipDefaults.filterChipBorder(
                enabled             = true,
                selected            = true,
                selectedBorderColor = MaterialTheme.colorScheme.primary,
                selectedBorderWidth = 1.5.dp
            )
        else
            FilterChipDefaults.filterChipBorder(enabled = true, selected = false)
    )
}

@Composable
fun ChipRow(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items.forEach { item ->
            EntryChip(selected = item == selected, onClick = { onSelect(item) }, label = item)
        }
    }
}