/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.screens.product.entry.OptionGroup
import org.siamdev.zappos.ui.screens.product.entry.OptionItem
import org.siamdev.zappos.ui.screens.product.entry.PickMode

@Composable
fun OptionsInfoCard(optionGroups: List<OptionGroup>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 16.dp)
        ) {
            Text(
                "OPTIONS & ADD-ONS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            optionGroups.forEachIndexed { index, group ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                } else {
                    Spacer(Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        group.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (group.required) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                        ) {
                            Text(
                                "required",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                        ) {
                            Text(
                                "optional",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Item rows
                group.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        val (modLabel, modColor, modWeight) = when {
                            item.priceModifier > 0 -> Triple(
                                "+ ${item.priceModifier}",
                                MaterialTheme.colorScheme.primary,
                                FontWeight.Bold,
                            )

                            item.priceModifier < 0 -> Triple(
                                "- ${-item.priceModifier}",
                                MaterialTheme.colorScheme.onSurfaceVariant,
                                FontWeight.Normal,
                            )

                            else -> Triple(
                                "Included",
                                MaterialTheme.colorScheme.onSurfaceVariant,
                                FontWeight.Normal,
                            )
                        }
                        Text(
                            modLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = modWeight,
                            color = modColor,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionsInfoCardPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            OptionsInfoCard(
                optionGroups = listOf(
                    OptionGroup(
                        id = 1L,
                        name = "Size",
                        pickMode = PickMode.ONE,
                        required = true,
                        items = listOf(
                            OptionItem(1L, "Small", -10),
                            OptionItem(2L, "Medium", 0),
                            OptionItem(3L, "Large", 15),
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
                            OptionItem(6L, "Honey", 5),
                        )
                    ),
                )
            )
        }
    }
}