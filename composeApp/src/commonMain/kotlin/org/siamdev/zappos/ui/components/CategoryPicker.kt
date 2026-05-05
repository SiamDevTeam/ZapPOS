/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.theme.YellowPrimary

data class SubCategory(val id: String, val name: String)

data class ProductCategory(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val subCategories: List<SubCategory>
)

val DefaultProductCategories: List<ProductCategory> = listOf(
    ProductCategory(
        id = "food",
        name = "Food",
        icon = Icons.Default.Restaurant,
        subCategories = listOf(
            SubCategory("rice", "Rice Dishes"),
            SubCategory("noodles", "Noodles"),
            SubCategory("grilled", "Grilled / Fried"),
            SubCategory("soup", "Soup & Stew"),
            SubCategory("salad", "Salad"),
        )
    ),
    ProductCategory(
        id = "beverages",
        name = "Beverages",
        icon = Icons.Default.LocalCafe,
        subCategories = listOf(
            SubCategory("tea_coffee", "Tea & Coffee"),
            SubCategory("energy", "Energy Drinks"),
            SubCategory("electrolyte", "Electrolyte Drinks"),
            SubCategory("soft_drinks", "Soft Drinks"),
            SubCategory("juice", "Juice & Smoothies"),
        )
    ),
    ProductCategory(
        id = "snack",
        name = "Snack",
        icon = Icons.Default.BakeryDining,
        subCategories = listOf(
            SubCategory("chips", "Chips & Crackers"),
            SubCategory("nuts", "Nuts & Seeds"),
            SubCategory("bread", "Bread & Pastry"),
            SubCategory("popcorn", "Popcorn"),
        )
    ),
    ProductCategory(
        id = "dessert",
        name = "Dessert",
        icon = Icons.Default.Cake,
        subCategories = listOf(
            SubCategory("ice_cream", "Ice Cream"),
            SubCategory("cake", "Cake & Cookies"),
            SubCategory("thai_dessert", "Thai Desserts"),
            SubCategory("pudding", "Pudding & Jelly"),
        )
    ),
    ProductCategory(
        id = "other",
        name = "Other",
        icon = Icons.Default.Category,
        subCategories = listOf(
            SubCategory("condiments", "Condiments & Sauces"),
            SubCategory("ready_eat", "Ready-to-eat"),
            SubCategory("merchandise", "Merchandise"),
        )
    ),
)

@Composable
fun CategoryPicker(
    selectedParent: String,
    selectedSub: String?,
    onSelect: (parent: String, sub: String?) -> Unit,
    modifier: Modifier = Modifier,
    categories: List<ProductCategory> = DefaultProductCategories
) {
    val currentParent = categories.find { it.id == selectedParent }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {

        // Parent category row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                val isSelected = cat.id == selectedParent
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(cat.id, null) },
                    label = { Text(cat.name, style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = {
                        Icon(cat.icon, contentDescription = null, modifier = Modifier.size(16.dp))
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = YellowPrimary.copy(alpha = 0.18f),
                        selectedLeadingIconColor = YellowPrimary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = if (isSelected) BorderStroke(1.5.dp, YellowPrimary)
                             else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                )
            }
        }

        // Sub-categories
        AnimatedVisibility(
            visible = currentParent != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            currentParent?.let { parent ->
                val rows = parent.subCategories.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    rows.forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowItems.forEach { sub ->
                                val isSubSelected = sub.id == selectedSub
                                FilterChip(
                                    selected = isSubSelected,
                                    onClick = {
                                        onSelect(parent.id, if (isSubSelected) null else sub.id)
                                    },
                                    label = {
                                        Text(sub.name, style = MaterialTheme.typography.bodySmall)
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = YellowPrimary.copy(alpha = 0.12f),
                                        selectedLeadingIconColor = YellowPrimary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    border = if (isSubSelected) BorderStroke(1.5.dp, YellowPrimary)
                                             else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat(2 - rowItems.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
        }
    }
}