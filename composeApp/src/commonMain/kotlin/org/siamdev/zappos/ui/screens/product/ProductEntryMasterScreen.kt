/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.CategoryPicker
import org.siamdev.zappos.ui.components.WorkspaceHeader


data class ProductEntry(
    val name: String,
    val price: Double,
    val unit: String,
    val category: String,
    val subCategory: String?,
    val description: String,
    val trackStock: Boolean,
    val stockQty: Int?,
    val isAvailable: Boolean,
    val isRecommended: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEntryMasterScreen(
    onNavigateBack: () -> Unit = {},
    onOpenDrawer: () -> Unit = {},
    onSave: (ProductEntry) -> Unit = {},
    showBackButton: Boolean = false
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("piece") }
    var selectedCategory by remember { mutableStateOf("food") }
    var selectedSub by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("") }
    var trackStock by remember { mutableStateOf(false) }
    var stockQty by remember { mutableStateOf("") }
    var isAvailable by remember { mutableStateOf(true) }
    var isRecommended by remember { mutableStateOf(false) }

    val units = listOf("piece", "cup", "plate", "bowl", "set", "kg", "pack", "bottle")
    val isFormValid = name.isNotBlank() && price.isNotBlank()

    val doSave = {
        onSave(
            ProductEntry(
                name = name.trim(),
                price = price.toDoubleOrNull() ?: 0.0,
                unit = unit,
                category = selectedCategory,
                subCategory = selectedSub,
                description = description.trim(),
                trackStock = trackStock,
                stockQty = if (trackStock) stockQty.toIntOrNull() ?: 0 else null,
                isAvailable = isAvailable,
                isRecommended = isRecommended
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        WorkspaceHeader(
            title = "Product Entry",
            onSegmentClick = onOpenDrawer,
            onNavigateBack = if (showBackButton) onNavigateBack else null
        )

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            if (maxWidth >= 750.dp) {
                DesktopEntryLayout(
                    name = name, price = price, unit = unit, units = units,
                    selectedCategory = selectedCategory, selectedSub = selectedSub,
                    description = description, trackStock = trackStock, stockQty = stockQty,
                    isAvailable = isAvailable, isRecommended = isRecommended,
                    canSave = isFormValid, doSave = doSave,
                    onNameChange = { name = it },
                    onPriceChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                    onUnitChange = { unit = it },
                    onCategoryChange = { cat, sub -> selectedCategory = cat; selectedSub = sub },
                    onDescriptionChange = { description = it },
                    onTrackStockChange = { trackStock = it },
                    onStockQtyChange = { stockQty = it.filter { c -> c.isDigit() } },
                    onAvailableChange = { isAvailable = it },
                    onRecommendedChange = { isRecommended = it }
                )
            } else {
                MobileEntryLayout(
                    name = name, price = price, unit = unit, units = units,
                    selectedCategory = selectedCategory, selectedSub = selectedSub,
                    description = description, trackStock = trackStock, stockQty = stockQty,
                    isAvailable = isAvailable, isRecommended = isRecommended,
                    canSave = isFormValid, doSave = doSave,
                    onNameChange = { name = it },
                    onPriceChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                    onUnitChange = { unit = it },
                    onCategoryChange = { cat, sub -> selectedCategory = cat; selectedSub = sub },
                    onDescriptionChange = { description = it },
                    onTrackStockChange = { trackStock = it },
                    onStockQtyChange = { stockQty = it.filter { c -> c.isDigit() } },
                    onAvailableChange = { isAvailable = it },
                    onRecommendedChange = { isRecommended = it }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MobileEntryLayout(
    name: String, price: String, unit: String, units: List<String>,
    selectedCategory: String, selectedSub: String?,
    description: String, trackStock: Boolean, stockQty: String,
    isAvailable: Boolean, isRecommended: Boolean,
    canSave: Boolean, doSave: () -> Unit,
    onNameChange: (String) -> Unit, onPriceChange: (String) -> Unit,
    onUnitChange: (String) -> Unit, onCategoryChange: (String, String?) -> Unit,
    onDescriptionChange: (String) -> Unit, onTrackStockChange: (Boolean) -> Unit,
    onStockQtyChange: (String) -> Unit, onAvailableChange: (Boolean) -> Unit,
    onRecommendedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { ImagePickerSection() }

        item { SectionLabel("Basic Information") }
        item {
            EntryTextField(
                value = name, onValueChange = onNameChange,
                label = "Product Name", placeholder = "e.g. Green Tea Latte",
                leadingIcon = Icons.AutoMirrored.Filled.Label, required = true
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                EntryTextField(
                    value = price, onValueChange = onPriceChange,
                    label = "Price", placeholder = "0.00",
                    leadingIcon = Icons.Default.AttachMoney,
                    keyboardType = KeyboardType.Decimal, required = true,
                    modifier = Modifier.weight(1.4f)
                )
                UnitDropdown(selected = unit, units = units, onSelect = onUnitChange, modifier = Modifier.weight(1f))
            }
        }

        item { SectionLabel("Category") }
        item {
            CategoryPicker(
                selectedParent = selectedCategory,
                selectedSub = selectedSub,
                onSelect = onCategoryChange
            )
        }

        item { SectionLabel("Description") }
        item {
            EntryTextField(
                value = description, onValueChange = onDescriptionChange,
                label = "Description (optional)", placeholder = "e.g. Made with fresh jasmine tea and oat milk...",
                leadingIcon = Icons.AutoMirrored.Filled.Notes,
                singleLine = false, minLines = 3
            )
        }

        item { SectionLabel("Inventory") }
        item {
            ToggleRow(
                label = "Track Stock", subtitle = "Monitor available quantity",
                icon = Icons.Default.Inventory, checked = trackStock, onCheckedChange = onTrackStockChange
            )
        }
        if (trackStock) {
            item {
                EntryTextField(
                    value = stockQty, onValueChange = onStockQtyChange,
                    label = "Stock Quantity", placeholder = "0",
                    leadingIcon = Icons.Default.Numbers, keyboardType = KeyboardType.Number
                )
            }
        }

        item { SectionLabel("Display Options") }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ToggleRow(
                    label = "Available",
                    subtitle = if (isAvailable) "Visible on menu" else "Hidden from menu",
                    icon = Icons.Default.Visibility, checked = isAvailable, onCheckedChange = onAvailableChange
                )
                ToggleRow(
                    label = "Recommended", subtitle = "Highlight as a featured item",
                    icon = Icons.Default.Star, checked = isRecommended, onCheckedChange = onRecommendedChange
                )
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
    SaveButton(canSave = canSave, onClick = doSave, modifier = Modifier.fillMaxWidth())
    } // Column
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DesktopEntryLayout(
    name: String, price: String, unit: String, units: List<String>,
    selectedCategory: String, selectedSub: String?,
    description: String, trackStock: Boolean, stockQty: String,
    isAvailable: Boolean, isRecommended: Boolean,
    canSave: Boolean, doSave: () -> Unit,
    onNameChange: (String) -> Unit, onPriceChange: (String) -> Unit,
    onUnitChange: (String) -> Unit, onCategoryChange: (String, String?) -> Unit,
    onDescriptionChange: (String) -> Unit, onTrackStockChange: (Boolean) -> Unit,
    onStockQtyChange: (String) -> Unit, onAvailableChange: (Boolean) -> Unit,
    onRecommendedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Left panel — Image + Basic Info
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { ImagePickerSection() }
            item { SectionLabel("Basic Information") }
            item {
                EntryTextField(
                    value = name, onValueChange = onNameChange,
                    label = "Product Name", placeholder = "e.g. Green Tea Latte",
                    leadingIcon = Icons.AutoMirrored.Filled.Label, required = true
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EntryTextField(
                        value = price, onValueChange = onPriceChange,
                        label = "Price", placeholder = "0.00",
                        leadingIcon = Icons.Default.AttachMoney,
                        keyboardType = KeyboardType.Decimal, required = true,
                        modifier = Modifier.weight(1.4f)
                    )
                    UnitDropdown(selected = unit, units = units, onSelect = onUnitChange, modifier = Modifier.weight(1f))
                }
            }
        }

        // Divider
        VerticalDivider(
            modifier = Modifier.fillMaxHeight().padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // Right panel — Category + Details + Toggles + Save
        Column(modifier = Modifier.weight(1f)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { SectionLabel("Category") }
            item {
                CategoryPicker(
                    selectedParent = selectedCategory,
                    selectedSub = selectedSub,
                    onSelect = onCategoryChange
                )
            }

            item { SectionLabel("Description") }
            item {
                EntryTextField(
                    value = description, onValueChange = onDescriptionChange,
                    label = "Description (optional)", placeholder = "e.g. Made with fresh jasmine tea and oat milk...",
                    leadingIcon = Icons.AutoMirrored.Filled.Notes,
                    singleLine = false, minLines = 3
                )
            }

            item { SectionLabel("Inventory") }
            item {
                ToggleRow(
                    label = "Track Stock", subtitle = "Monitor available quantity",
                    icon = Icons.Default.Inventory, checked = trackStock, onCheckedChange = onTrackStockChange
                )
            }
            if (trackStock) {
                item {
                    EntryTextField(
                        value = stockQty, onValueChange = onStockQtyChange,
                        label = "Stock Quantity", placeholder = "0",
                        leadingIcon = Icons.Default.Numbers, keyboardType = KeyboardType.Number
                    )
                }
            }

            item { SectionLabel("Display Options") }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ToggleRow(
                        label = "Available",
                        subtitle = if (isAvailable) "Visible on menu" else "Hidden from menu",
                        icon = Icons.Default.Visibility, checked = isAvailable, onCheckedChange = onAvailableChange
                    )
                    ToggleRow(
                        label = "Recommended", subtitle = "Highlight as a featured item",
                        icon = Icons.Default.Star, checked = isRecommended, onCheckedChange = onRecommendedChange
                    )
                }
            }
        }
        SaveButton(canSave = canSave, onClick = doSave, modifier = Modifier.fillMaxWidth())
        } // Column right panel
    }
}

@Composable
private fun SaveButton(
    canSave: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Button(
        onClick = onClick,
        enabled = canSave,
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = YellowPrimary,
            contentColor = Color.Black,
            disabledContainerColor = YellowPrimary.copy(alpha = 0.3f),
            disabledContentColor = Color.Black.copy(alpha = 0.4f)
        )
    ) {
        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text("Save Product", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ImagePickerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .clickable { /* TODO: platform image picker */ },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "Add Product Image",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Tap to browse",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )
}

@Composable
private fun EntryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(if (required) "$label *" else label) },
        placeholder = {
            Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f))
        },
        leadingIcon = {
            Icon(
                leadingIcon, contentDescription = null,
                tint = if (value.isNotEmpty()) YellowPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        },
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = YellowPrimary,
            focusedLabelColor = YellowPrimary,
            cursorColor = YellowPrimary
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitDropdown(
    selected: String,
    units: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        OutlinedTextField(
            value = selected, onValueChange = {}, readOnly = true,
            label = { Text("Unit") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowPrimary,
                focusedLabelColor = YellowPrimary
            ),
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { u ->
                DropdownMenuItem(
                    text = { Text(u) },
                    onClick = { onSelect(u); expanded = false },
                    leadingIcon = if (u == selected) {
                        { Icon(Icons.Default.Check, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
        }
    }
}

@Composable
private fun ToggleRow(
    label: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (checked) YellowPrimary.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, contentDescription = null,
                tint = if (checked) YellowPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked, onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = YellowPrimary, checkedThumbColor = Color.White)
        )
    }
}


@Preview(widthDp = 411, heightDp = 891)
@Composable
private fun MobilePreview() {
    ProductEntryMasterScreen()
}

@Preview(widthDp = 1024, heightDp = 768)
@Composable
private fun DesktopPreview() {
    ProductEntryMasterScreen()
}