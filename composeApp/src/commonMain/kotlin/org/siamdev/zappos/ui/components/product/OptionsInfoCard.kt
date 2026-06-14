/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.screens.product.entry.*

/** Dedicated card for Options & Add-ons groups. Each group shows its name, pick mode, and
 *  a row per item with the price modifier. Shown only when the product has option groups. */
@Composable
internal fun OptionsInfoCard(state: EntryFormState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).padding(10.dp)) {
            Text(
                "OPTIONS & ADD-ONS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            state.optionGroups.forEach { group ->
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        group.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (group.pickMode == PickMode.MANY) {
                            GroupBadge("multi-select", MaterialTheme.colorScheme.primary)
                        }
                        GroupBadge(
                            label = if (group.required) "required" else "optional",
                            color = if (group.required) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                group.items.forEachIndexed { ii, item ->
                    if (ii > 0) Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                            Text(
                                item.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        val (modLabel, modColor) = when {
                            item.priceModifier > 0 ->
                                "+฿${item.priceModifier}" to MaterialTheme.colorScheme.primary

                            item.priceModifier < 0 ->
                                "−฿${-item.priceModifier}" to MaterialTheme.colorScheme.error

                            else ->
                                "included" to MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        Text(
                            modLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = modColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupBadge(label: String, color: Color) {
    Text(
        label,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun OptionsInfoCardPreview() {
    val state = EntryFormState().apply {
        optionGroups = listOf(
            OptionGroup(
                id = 1L,
                name = "Size",
                pickMode = PickMode.ONE,
                required = true,
                items = listOf(
                    OptionItem(1L, "Small", -10),
                    OptionItem(2L, "Medium", 0),
                    OptionItem(3L, "Large", 15)
                )
            ),
            OptionGroup(
                id = 2L,
                name = "Add-ons",
                pickMode = PickMode.MANY,
                required = false,
                items = listOf(
                    OptionItem(4L, "Extra Shot", 15),
                    OptionItem(5L, "Whipped Cream", 10),
                    OptionItem(6L, "Honey", 5)
                )
            )
        )
    }
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            OptionsInfoCard(state = state)
        }
    }
}
