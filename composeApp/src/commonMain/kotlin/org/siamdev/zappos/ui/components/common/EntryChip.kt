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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EntryChip(selected: Boolean, onClick: () -> Unit, label: String) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = if (selected) {
            FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = true,
                selectedBorderColor = MaterialTheme.colorScheme.primary,
                selectedBorderWidth = 1.5.dp
            )
        }

        else FilterChipDefaults.filterChipBorder(enabled = true, selected = false)

    )
}

@Composable
fun ChipRow(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .pointerInput(scrollState) {
                awaitPointerEventScope {
                    var dragging = false
                    var prevX = 0f
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Scroll -> {
                                val delta = event.changes.firstOrNull()?.scrollDelta ?: continue
                                val amount = if (delta.x != 0f) delta.x else delta.y
                                scrollState.dispatchRawDelta(amount * 50f)
                            }

                            PointerEventType.Press -> {
                                val change = event.changes.firstOrNull()
                                if (change != null && change.type == PointerType.Mouse) {
                                    dragging = true
                                    prevX = change.position.x
                                }
                            }

                            PointerEventType.Move -> if (dragging) {
                                val change = event.changes.firstOrNull()
                                if (change != null) {
                                    scrollState.dispatchRawDelta(prevX - change.position.x)
                                    prevX = change.position.x
                                    change.consume()
                                }
                            }

                            PointerEventType.Release -> dragging = false
                        }
                    }
                }
            },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items.forEach { item ->
            EntryChip(selected = item == selected, onClick = { onSelect(item) }, label = item)
        }
    }
}

@Composable
fun LabeledChipRow(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun EntryChipPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EntryChip(selected = true, onClick = {}, label = "Selected")
            EntryChip(selected = false, onClick = {}, label = "Unselected")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChipRowPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledChipRow(label = "Category") {
                ChipRow(
                    items = listOf("All", "Food", "Beverage", "Snack", "Dessert"),
                    selected = "Food",
                    onSelect = {}
                )
            }
        }
    }
}
