/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.ChipRow
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.components.common.ToggleItem
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.components.menu.ProductCategory

@Composable
internal fun ProductDetailsSection(state: EntryFormState) {
    val categories = DefaultProductCategories
    val categoryDisplay = categories.find { it.id == state.category }?.name ?: state.category

    SectionCard(
        icon = Icons.Default.Apps,
        title = "Product details",
        subtitle = "The essentials shown on your menu",
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth >= 480.dp) {
                DetailsDesktop(state, categories, categoryDisplay)
            } else {
                DetailsMobile(state, categories, categoryDisplay)
            }
        }
    }
}

// Desktop: image left | right column holds Name, Category+SubCategory, Chips, Description.
// Toggles span full width below, separated by a divider.
@Composable
private fun DetailsDesktop(
    state: EntryFormState,
    categories: List<ProductCategory>,
    categoryDisplay: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            ImagePickerBox(modifier = Modifier.size(130.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                EntryField(
                    value = state.name,
                    onValueChange = { state.name = it },
                    label = "Name",
                    placeholder = "e.g. Green Tea Latte",
                    required = true,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    EntryField(
                        value = categoryDisplay,
                        onValueChange = {
                            state.category = it
                            state.subCategory = null
                        },
                        label = "Category",
                        placeholder = "Beverages",
                        required = true,
                        modifier = Modifier.weight(1f),
                    )
                    EntryField(
                        value = state.subCategory ?: "",
                        onValueChange = { state.subCategory = it.ifEmpty { null } },
                        label = "Sub-category",
                        placeholder = "Tea & Coffee",
                        //optional = true,
                        modifier = Modifier.weight(1f),
                    )
                }
                ChipRow(
                    items = categories.map { it.name },
                    selected = categoryDisplay,
                    onSelect = { name ->
                        val cat = categories.find { it.name == name }
                        state.category = cat?.id ?: name
                        state.subCategory = null
                    },
                )
                EntryField(
                    value = state.description,
                    onValueChange = { state.description = it },
                    label = "Description",
                    optional = true,
                    placeholder = "Short note for staff or the menu — ingredients, size, what's included...",
                    singleLine = false,
                    minLines = 3,
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        ToggleItem(
            icon = Icons.Default.Visibility,
            label = "Available",
            subtitle = "Shown on the menu and orderable right now",
            checked = state.isAvailable,
            onCheckedChange = { state.isAvailable = it },
        )
        ToggleItem(
            icon = Icons.Default.Star,
            label = "Recommended",
            subtitle = "Featured with a ★ on the menu",
            checked = state.isRecommended,
            onCheckedChange = { state.isRecommended = it },
        )
    }
}

// Mobile: full-width image at top, then each field stacked individually, then toggles.
@Composable
private fun DetailsMobile(
    state: EntryFormState,
    categories: List<ProductCategory>,
    categoryDisplay: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ImagePickerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        )
        EntryField(
            value = state.name,
            onValueChange = { state.name = it },
            label = "Name",
            placeholder = "e.g. Green Tea Latte",
            required = true,
        )
        EntryField(
            value = categoryDisplay,
            onValueChange = {
                state.category = it
                state.subCategory = null
            },
            label = "Category",
            placeholder = "Beverages",
            required = true,
        )
        EntryField(
            value = state.subCategory ?: "",
            onValueChange = { state.subCategory = it.ifEmpty { null } },
            label = "Sub-category",
            placeholder = "Tea & Coffee",
            optional = true,
        )
        ChipRow(
            items = categories.map { it.name },
            selected = categoryDisplay,
            onSelect = { name ->
                val cat = categories.find { it.name == name }
                state.category = cat?.id ?: name
                state.subCategory = null
            },
        )
        EntryField(
            value = state.description,
            onValueChange = { state.description = it },
            label = "Description",
            optional = true,
            placeholder = "Short note for staff or the menu — ingredients, size, what's included...",
            singleLine = false,
            minLines = 2,
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        ToggleItem(
            icon = Icons.Default.Visibility,
            label = "Available",
            subtitle = "Shown on the menu and orderable right now",
            checked = state.isAvailable,
            onCheckedChange = { state.isAvailable = it },
        )
        ToggleItem(
            icon = Icons.Default.Star,
            label = "Recommended",
            subtitle = "Featured with a ★ on the menu",
            checked = state.isRecommended,
            onCheckedChange = { state.isRecommended = it },
        )
    }
}

@Preview(showBackground = true, widthDp = 411, name = "Mobile — 411dp")
@Composable
private fun PreviewMobile() {
    MaterialTheme { ProductDetailsSection(rememberEntryFormState()) }
}

@Preview(showBackground = true, widthDp = 600, name = "Desktop panel — 600dp")
@Composable
private fun PreviewDesktop() {
    MaterialTheme { ProductDetailsSection(rememberEntryFormState()) }
}