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

@Composable
internal fun ProductDetailsSection(state: EntryFormState) {
    val categories = DefaultProductCategories
    val categoryDisplay = categories.find { it.id == state.category }?.name ?: state.category

    SectionCard(
        icon = Icons.Default.Apps,
        title = "Product details",
        subtitle = "The essentials shown on your menu",
    ) {
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
                EntryField(
                    value = categoryDisplay,
                    onValueChange = {
                        state.category = it
                        state.subCategory = null
                    },
                    label = "Category",
                    placeholder = "e.g. Beverages",
                    required = true,
                )
                EntryField(
                    value = state.subCategory ?: "",
                    onValueChange = { state.subCategory = it.ifEmpty { null } },
                    label = "Sub-category",
                    placeholder = "e.g. Tea & Coffee",
                    optional = true,
                )
            }
        }

        ChipRow(
            items = categories.map { it.name },
            selected = categoryDisplay,
            onSelect = { name ->
                val cat = categories.find { it.name == name }
                state.category = cat?.id ?: name
                state.subCategory = null
            },
            rowSize = 4,
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

        ToggleItem(
            icon = Icons.Default.Visibility,
            label = "Available",
            subtitle = "Shown on the menu",
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

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun ProductDetailSectionPreview() {
    MaterialTheme {
        ProductDetailsSection(rememberEntryFormState())
    }
}
