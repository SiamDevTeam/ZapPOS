/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.ChipRow
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.components.common.ToggleItem

private data class TypeConfig(
    val sectionTitle: String,
    val sectionSubtitle: String,
    val namePlaceholder: String,
    val categoryPlaceholder: String,
    val descPlaceholder: String,
    val chips: List<String>,
)

private fun typeConfigFor(type: EntryType): TypeConfig =
    when (type) {
        EntryType.GOODS ->
            TypeConfig(
                sectionTitle = "Product details",
                sectionSubtitle = "The essentials shown on your menu",
                namePlaceholder = "e.g. Green Tea Latte",
                categoryPlaceholder = "e.g. Beverage",
                descPlaceholder = "Ingredients, size, what's included...",
                chips = listOf("Beverage", "Food", "Snack", "Dessert", "Retail"),
            )
        EntryType.SERVICE ->
            TypeConfig(
                sectionTitle = "Service details",
                sectionSubtitle = "Service name, category and availability",
                namePlaceholder = "e.g. Room Cleaning",
                categoryPlaceholder = "e.g. Cleaning",
                descPlaceholder = "What the service includes, how long, what to prepare...",
                chips = listOf("Cleaning", "Repair", "Consultation", "Training", "Beauty"),
            )
        EntryType.RENTAL ->
            TypeConfig(
                sectionTitle = "Rental details",
                sectionSubtitle = "Rental item name, category and availability",
                namePlaceholder = "e.g. Tennis Court",
                categoryPlaceholder = "e.g. Equipment",
                descPlaceholder = "What's included, rental terms, how to use...",
                chips = listOf("Equipment", "Vehicle", "Room", "Sports", "Event"),
            )
    }

// Displays a system-generated ID as read-only metadata.
// Mirrors the key-value row pattern from MonitorStockPanel (e.g. "New balance" / "SKU" displays).
@Composable
private fun ProductIdMetadata(productId: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Tag,
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "PRODUCT ID",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = productId,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
internal fun ProductDetailsSection(state: EntryFormState) {
    val config = typeConfigFor(state.entryType)

    SectionCard(
        icon = Icons.Default.Apps,
        title = config.sectionTitle,
        subtitle = config.sectionSubtitle,
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth >= 580.dp) {
                DetailsDesktop(state, config)
            } else {
                DetailsMobile(state, config)
            }
        }
    }
}

// Desktop: image left | right column holds Name, Category+SubCategory, Chips, Description.
// Toggles span full width below, separated by a divider.
@Composable
private fun DetailsDesktop(state: EntryFormState, config: TypeConfig) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (state.productId != null) {
            ProductIdMetadata(productId = state.productId!!)
        }

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
                    placeholder = config.namePlaceholder,
                    required = true,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    EntryField(
                        value = state.category,
                        onValueChange = {
                            state.category = it
                            state.subCategory = null
                        },
                        label = "Category",
                        placeholder = config.categoryPlaceholder,
                        required = true,
                        modifier = Modifier.weight(1f),
                    )
                    EntryField(
                        value = state.subCategory ?: "",
                        onValueChange = { state.subCategory = it.ifEmpty { null } },
                        label = "Sub-category",
                        placeholder = "optional",
                        optional = true,
                        modifier = Modifier.weight(1f),
                    )
                }
                ChipRow(
                    items = config.chips,
                    selected = state.category,
                    onSelect = {
                        state.category = it
                        state.subCategory = null
                    },
                )
                EntryField(
                    value = state.description,
                    onValueChange = { state.description = it },
                    label = "Description",
                    optional = true,
                    placeholder = config.descPlaceholder,
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
private fun DetailsMobile(state: EntryFormState, config: TypeConfig) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (state.productId != null) {
            ProductIdMetadata(productId = state.productId!!)
        }

        ImagePickerBox(
            modifier = Modifier.fillMaxWidth().height(160.dp),
        )
        EntryField(
            value = state.name,
            onValueChange = { state.name = it },
            label = "Name",
            placeholder = config.namePlaceholder,
            required = true,
        )
        EntryField(
            value = state.category,
            onValueChange = {
                state.category = it
                state.subCategory = null
            },
            label = "Category",
            placeholder = config.categoryPlaceholder,
            required = true,
        )
        EntryField(
            value = state.subCategory ?: "",
            onValueChange = { state.subCategory = it.ifEmpty { null } },
            label = "Sub-category",
            placeholder = "optional",
            optional = true,
        )
        ChipRow(
            items = config.chips,
            selected = state.category,
            onSelect = {
                state.category = it
                state.subCategory = null
            },
        )
        EntryField(
            value = state.description,
            onValueChange = { state.description = it },
            label = "Description",
            optional = true,
            placeholder = config.descPlaceholder,
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

@Preview(showBackground = true, widthDp = 411, name = "Mobile · Goods")
@Composable
private fun PreviewMobileGoods() {
    MaterialTheme { ProductDetailsSection(rememberEntryFormState()) }
}

@Preview(showBackground = true, widthDp = 411, name = "Mobile · Service")
@Composable
private fun PreviewMobileService() {
    MaterialTheme { ProductDetailsSection(rememberEntryFormState().also { it.entryType = EntryType.SERVICE }) }
}

@Preview(showBackground = true, widthDp = 411, name = "Mobile · Rental")
@Composable
private fun PreviewMobileRental() {
    MaterialTheme { ProductDetailsSection(rememberEntryFormState().also { it.entryType = EntryType.RENTAL }) }
}

@Preview(showBackground = true, widthDp = 600, name = "Desktop · with Product ID")
@Composable
private fun PreviewDesktopWithId() {
    MaterialTheme { ProductDetailsSection(rememberEntryFormState().also { it.productId = "PRD-0042" }) }
}
