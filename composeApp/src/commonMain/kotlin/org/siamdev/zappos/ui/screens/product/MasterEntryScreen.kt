/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.utils.DateTimeUtils
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.components.menu.CategoryPicker
import org.siamdev.zappos.utils.formatAmount

// ─────────────────────────────────────────────────────────────────────────────
// Enums & Data models
// ─────────────────────────────────────────────────────────────────────────────

private enum class EntryType { GOODS, SERVICE, RENTAL }
private enum class PickMode  { ONE, MANY }

private val entryTabs = listOf(
    TabItem("Goods",   Icons.Default.Inventory),
    TabItem("Service", Icons.Default.Person),
    TabItem("Rental",  Icons.Default.Schedule)
)

private data class OptionItem(
    val id: Long            = DateTimeUtils.nowEpochMillis(),
    val name: String        = "",
    val priceModifier: Int  = 0
)

private data class OptionGroup(
    val id: Long                = DateTimeUtils.nowEpochMillis(),
    val name: String            = "Size",
    val pickMode: PickMode      = PickMode.ONE,
    val required: Boolean       = false,
    val items: List<OptionItem> = emptyList()
)

// ─────────────────────────────────────────────────────────────────────────────
// Form state holder
// ─────────────────────────────────────────────────────────────────────────────

@Stable
private class EntryFormState {

    // type
    var entryType by mutableStateOf(EntryType.GOODS)

    // product details
    var name        by mutableStateOf("")
    var category    by mutableStateOf("beverages")
    var subCategory by mutableStateOf<String?>(null)
    var description by mutableStateOf("")
    var isAvailable   by mutableStateOf(true)
    var isRecommended by mutableStateOf(false)

    // pricing – shared
    var price         by mutableStateOf("")
    var unit          by mutableStateOf("piece")
    var chargeVat     by mutableStateOf(true)
    var costPrice     by mutableStateOf("")
    var showCostPrice by mutableStateOf(false)

    // pricing – goods only
    var openPrice by mutableStateOf(false)

    // pricing – service only  (0 = per person, 1 = per session, 2 = per hour)
    var chargedBy by mutableStateOf(2)

    // pricing – rental only
    var bookingDuration by mutableStateOf("60")
    var minBooking      by mutableStateOf("1")

    // inventory (goods)
    var trackStock       by mutableStateOf(true)
    var openingStock     by mutableStateOf("0")
    var maxCapacity      by mutableStateOf("0")
    var lowStockAlert    by mutableStateOf("10")
    // reorder
    var reorderPoint     by mutableStateOf("5")
    var reorderQty       by mutableStateOf("20")
    // purchase / unit conversion
    var purchaseUnit     by mutableStateOf("")
    var unitsPerPurchase by mutableStateOf("1")
    // supplier & extras
    var supplier         by mutableStateOf("")
    var trackExpiry      by mutableStateOf(false)

    // schedule & capacity (service)
    var serviceCapacity        by mutableStateOf("12")
    var serviceDuration        by mutableStateOf("60")
    var serviceOpens           by mutableStateOf("09:00")
    var serviceCloses          by mutableStateOf("18:00")
    var activeDays             by mutableStateOf(setOf(0, 1, 2, 3, 4))
    var instructor             by mutableStateOf("")
    var serviceRequiresBooking by mutableStateOf(false)

    // resources & booking (rental)
    var rentalUnitsCount      by mutableStateOf("2")
    var rentalBuffer          by mutableStateOf("0")
    var rentalOpens           by mutableStateOf("08:00")
    var rentalCloses          by mutableStateOf("22:00")
    var depositAmount         by mutableStateOf("0.00")
    var rentalRequiresBooking by mutableStateOf(true)

    // options & add-ons
    var optionGroups by mutableStateOf(emptyList<OptionGroup>())

    // advanced
    var advancedExpanded by mutableStateOf(false)
    var sku          by mutableStateOf("")
    var barcode      by mutableStateOf("")
    var sendOrderTo  by mutableStateOf("None")
    var displayOrder by mutableStateOf("0")

    // ── computed ──────────────────────────────────────────────────────────

    val isFormValid: Boolean get() = name.isNotBlank() && price.isNotBlank()

    val unitOptions: List<String> get() = when (entryType) {
        EntryType.GOODS   -> listOf("cup", "plate", "bowl", "piece", "skewer", "bottle", "pack", "kg", "box")
        EntryType.SERVICE -> listOf("session", "person", "hour", "course", "class", "month")
        EntryType.RENTAL  -> listOf("hour", "court", "field", "table", "room", "day")
    }
}

@Composable
private fun rememberEntryFormState(): EntryFormState {
    val state = remember { EntryFormState() }
    // reset unit default whenever the tab changes
    LaunchedEffect(state.entryType) {
        state.unit = when (state.entryType) {
            EntryType.GOODS   -> "piece"
            EntryType.SERVICE -> "hour"
            EntryType.RENTAL  -> "hour"
        }
    }
    return state
}

// ─────────────────────────────────────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun MasterEntryScreen(
    onNavigateBack: () -> Unit = {},
    onOpenDrawer:   () -> Unit = {},
    onSave:         () -> Unit = {},
    showBackButton: Boolean    = false
) {
    val state = rememberEntryFormState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        WorkspaceHeader(
            title          = "Product Entry",
            subtitle       = "New product · master data",
            onSegmentClick = onOpenDrawer,
            onNavigateBack = if (showBackButton) onNavigateBack else null
        )

        SegmentedTabBar(
            tabs          = entryTabs,
            selectedIndex = state.entryType.ordinal,
            onTabSelect   = { state.entryType = EntryType.entries[it] },
            modifier      = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 8.dp)
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            if (maxWidth >= 750.dp) EntryDesktopLayout(state)
            else                    EntryMobileLayout(state)
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            MaterialButton(
                modifier  = Modifier.weight(1f).height(48.dp),
                text      = "Save product",
                iconStart = Icons.Default.Check,
                enabled   = state.isFormValid,
                onClick   = onSave
            )
            IconButton(
                onClick  = onNavigateBack,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(
                    imageVector        = Icons.Default.Close,
                    contentDescription = "Discard",
                    tint               = MaterialTheme.colorScheme.error,
                    modifier           = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Mobile layout
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EntryMobileLayout(state: EntryFormState) {
    LazyColumn(
        modifier         = Modifier.fillMaxSize(),
        contentPadding   = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { ProductDetailsSection(state) }
        item { PricingSection(state) }

        when (state.entryType) {
            EntryType.GOODS -> {
                item { InventorySection(state) }
                item { OptionsSection(state) }
            }
            EntryType.SERVICE -> {
                item { ScheduleCapacitySection(state) }
                item { OptionsSection(state) }
            }
            EntryType.RENTAL -> {
                item { ResourcesBookingSection(state) }
            }
        }

        item { AdvancedSection(state) }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Desktop layout  (independent scroll per column)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EntryDesktopLayout(state: EntryFormState) {
    Row(
        modifier            = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Left column — product details + pricing ──────────────────────
        LazyColumn(
            modifier            = Modifier.weight(0.46f).fillMaxHeight(),
            contentPadding      = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Phase 3 ↓
            item { ProductDetailsSection(state) }
            item { PricingSection(state) }
        }

        // ── Right column — tab-specific + options + advanced ─────────────
        LazyColumn(
            modifier            = Modifier.weight(0.54f).fillMaxHeight(),
            contentPadding      = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            when (state.entryType) {
                EntryType.GOODS -> {
                    item { InventorySection(state) }
                    item { OptionsSection(state) }
                }
                EntryType.SERVICE -> {
                    item { ScheduleCapacitySection(state) }
                    item { OptionsSection(state) }
                }
                EntryType.RENTAL -> {
                    item { ResourcesBookingSection(state) }
                }
            }
            item { AdvancedSection(state) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SectionCard
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badge: String? = null,
    expanded: Boolean? = null,
    onExpandChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val chevronAngle by animateFloatAsState(
        targetValue = if (expanded == true) 180f else 0f,
        label = "chevron"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (expanded != null && onExpandChange != null)
                        Modifier.clickable { onExpandChange(!expanded) }
                    else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (badge != null) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            if (expanded != null) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(chevronAngle)
                )
            }
        }

        if (expanded != null) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit  = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = content
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// EntryField
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EntryField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    required: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(if (required) "$label *" else label)
        },
        placeholder = {
            if (placeholder.isNotEmpty())
                Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f))
        },
        singleLine = singleLine,
        minLines = minLines,
        readOnly = readOnly,
        prefix = prefix,
        suffix = suffix,
        trailingIcon = trailingContent,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor  = MaterialTheme.colorScheme.primary,
            focusedLabelColor   = MaterialTheme.colorScheme.primary,
            cursorColor         = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.fillMaxWidth()
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// ToggleItem
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ToggleItem(
    icon: ImageVector,
    label: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(
                    if (checked) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(17.dp),
                tint = if (checked) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = Color.White
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// NumberUnitField
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun NumberUnitField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unitLabel: String,
    modifier: Modifier = Modifier
) {
    EntryField(
        value = value,
        onValueChange = { onValueChange(it.filter(Char::isDigit)) },
        label = label,
        keyboardType = KeyboardType.Number,
        suffix = {
            Text(
                text = unitLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// TimeField
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TimeField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    EntryField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = "00:00",
        keyboardType = KeyboardType.Number,
        trailingContent = {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// EntryChip / ChipRow
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EntryChip(selected: Boolean, onClick: () -> Unit, label: String) {
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
private fun ChipRow(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    rowSize: Int = 5
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.chunked(rowSize).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { item ->
                    EntryChip(selected = item == selected, onClick = { onSelect(item) }, label = item)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Image picker box
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ImagePickerBox(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                .clickable { /* TODO: platform image picker */ },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector      = Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    modifier         = Modifier.size(22.dp),
                    tint             = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text      = "Drop a photo",
                    style     = MaterialTheme.typography.labelSmall,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text      = "click to browse\nJPG/PNG",
                    style     = MaterialTheme.typography.labelSmall,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(5.dp))
        Text(
            text  = "Square works best",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Product Details Section
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProductDetailsSection(state: EntryFormState) {
    SectionCard(
        icon     = Icons.Default.Apps,
        title    = "Product details",
        subtitle = "The essentials shown on your menu"
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment     = Alignment.Top
        ) {
            ImagePickerBox(modifier = Modifier.size(90.dp))

            EntryField(
                value         = state.name,
                onValueChange = { state.name = it },
                label         = "Name",
                placeholder   = "e.g. Green Tea Latte",
                required      = true,
                modifier      = Modifier.weight(1f)
            )
        }

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = "Category *",
                style      = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text  = "Sub-category   optional",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        CategoryPicker(
            selectedParent = state.category,
            selectedSub    = state.subCategory,
            onSelect       = { parent, sub ->
                state.category    = parent
                state.subCategory = sub
            }
        )

        EntryField(
            value         = state.description,
            onValueChange = { state.description = it },
            label         = "Description",
            placeholder   = "Short note for staff or the menu — ingredients, size, what's included...",
            singleLine    = false,
            minLines      = 3
        )

        ToggleItem(
            icon            = Icons.Default.Visibility,
            label           = "Available",
            subtitle        = "Shown on the menu and orderable right now",
            checked         = state.isAvailable,
            onCheckedChange = { state.isAvailable = it }
        )
        ToggleItem(
            icon            = Icons.Default.Star,
            label           = "Recommended",
            subtitle        = "Featured with a ★ on the menu",
            checked         = state.isRecommended,
            onCheckedChange = { state.isRecommended = it }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Charged-by selector  (Service only)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ChargedBySelector(
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val labels = listOf("Per person", "Per session", "Per hour")

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text       = "Charged by",
            style      = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            labels.forEachIndexed { index, label ->
                val isSelected = index == selected
                OutlinedButton(
                    onClick  = { onSelect(index) },
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                                         else Color.Transparent,
                        contentColor   = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                         else MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Pricing & unit Section
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PricingSection(state: EntryFormState) {
    val margin = remember(state.price, state.costPrice) {
        val p = state.price.toDoubleOrNull() ?: 0.0
        val c = state.costPrice.toDoubleOrNull() ?: 0.0

        if (p > 0.0) {
            (((p - c) / p) * 100.0).formatAmount(0) + "%"
        } else {
            ""
        }
    }
    SectionCard(
        icon     = Icons.Default.AttachMoney,
        title    = "Pricing & unit",
        subtitle = "What the customer pays, and how it's counted"
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            EntryField(
                value         = state.price,
                onValueChange = { state.price = it.filter { c -> c.isDigit() || c == '.' } },
                label         = "Price",
                placeholder   = "0.00",
                required      = true,
                keyboardType  = KeyboardType.Decimal,
                prefix        = { Text("฿") },
                modifier      = Modifier.weight(1f)
            )
            EntryField(
                value         = state.unit,
                onValueChange = { state.unit = it },
                label         = "Unit",
                placeholder   = "type your own",
                required      = true,
                prefix        = { Text("/") },
                modifier      = Modifier.weight(1f)
            )
        }

        ChipRow(
            items    = state.unitOptions,
            selected = state.unit,
            onSelect = { state.unit = it }
        )

        if (state.entryType == EntryType.GOODS) {
            ToggleItem(
                icon            = Icons.Default.SwapHoriz,
                label           = "Open / variable price",
                subtitle        = "Cashier enters the amount at checkout — e.g. sold by weight",
                checked         = state.openPrice,
                onCheckedChange = { state.openPrice = it }
            )
        }

        if (state.entryType == EntryType.SERVICE) {
            ChargedBySelector(
                selected = state.chargedBy,
                onSelect = { state.chargedBy = it }
            )
        }

        if (state.entryType == EntryType.RENTAL) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberUnitField(
                    value         = state.bookingDuration,
                    onValueChange = { state.bookingDuration = it },
                    label         = "Booking duration",
                    unitLabel     = "min / slot",
                    modifier      = Modifier.weight(1f)
                )
                NumberUnitField(
                    value         = state.minBooking,
                    onValueChange = { state.minBooking = it },
                    label         = "Min. booking",
                    unitLabel     = "slot(s)",
                    modifier      = Modifier.weight(1f)
                )
            }
        }

        ToggleItem(
            icon            = Icons.Default.AccountBalance,
            label           = "Charge VAT on this item",
            subtitle        = "Applies the store VAT rate (7%) at checkout",
            checked         = state.chargeVat,
            onCheckedChange = { state.chargeVat = it }
        )

        TextButton(
            onClick        = { state.showCostPrice = !state.showCostPrice },
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector        = if (state.showCostPrice) Icons.Default.Remove else Icons.Default.Add,
                contentDescription = null,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text       = if (state.showCostPrice) "Hide cost price"
                             else "Add cost price (track margin)",
                style      = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color      = MaterialTheme.colorScheme.primary
            )
        }

        AnimatedVisibility(
            visible = state.showCostPrice,
            enter   = fadeIn() + expandVertically(),
            exit    = fadeOut() + shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text       = "Cost price",
                        style      = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color      = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text  = "Margin",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EntryField(
                        value         = state.costPrice,
                        onValueChange = { state.costPrice = it.filter { c -> c.isDigit() || c == '.' } },
                        label         = "Cost price",
                        placeholder   = "0.00",
                        keyboardType  = KeyboardType.Decimal,
                        prefix        = { Text("฿") },
                        suffix        = { Text("/ ${state.unit.ifEmpty { "unit" }}") },
                        modifier      = Modifier.weight(1f)
                    )
                    EntryField(
                        value         = margin,
                        onValueChange = {},
                        label         = "Margin",
                        placeholder   = "Enter price & cost",
                        readOnly      = true,
                        modifier      = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Inventory Section  (Goods)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun InventorySection(state: EntryFormState) {
    val sellUnit = state.unit.ifEmpty { "unit" }

    SectionCard(
        icon     = Icons.Default.Inventory,
        title    = "Inventory",
        subtitle = "Stock tracking, reorder rules and purchase conversion"
    ) {
        ToggleItem(
            icon            = Icons.Default.Inventory,
            label           = "Track stock",
            subtitle        = "Monitor quantity and trigger low-stock alerts",
            checked         = state.trackStock,
            onCheckedChange = { state.trackStock = it }
        )

        AnimatedVisibility(
            visible = state.trackStock,
            enter   = fadeIn() + expandVertically(),
            exit    = fadeOut() + shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

                SectionSubHeader("Stock levels")
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        NumberUnitField(
                            value         = state.openingStock,
                            onValueChange = { state.openingStock = it },
                            label         = "Opening stock",
                            unitLabel     = sellUnit,
                            modifier      = Modifier.weight(1f)
                        )
                        NumberUnitField(
                            value         = state.maxCapacity,
                            onValueChange = { state.maxCapacity = it },
                            label         = "Max capacity",
                            unitLabel     = sellUnit,
                            modifier      = Modifier.weight(1f)
                        )
                    }
                    NumberUnitField(
                        value         = state.lowStockAlert,
                        onValueChange = { state.lowStockAlert = it },
                        label         = "Low stock alert",
                        unitLabel     = sellUnit
                    )
                }

                SectionSubHeader("Reorder")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumberUnitField(
                        value         = state.reorderPoint,
                        onValueChange = { state.reorderPoint = it },
                        label         = "Reorder point",
                        unitLabel     = sellUnit,
                        modifier      = Modifier.weight(1f)
                    )
                    NumberUnitField(
                        value         = state.reorderQty,
                        onValueChange = { state.reorderQty = it },
                        label         = "Reorder quantity",
                        unitLabel     = sellUnit,
                        modifier      = Modifier.weight(1f)
                    )
                }

                SectionSubHeader("Purchase & conversion")
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        EntryField(
                            value         = state.purchaseUnit,
                            onValueChange = { state.purchaseUnit = it },
                            label         = "Purchase unit",
                            placeholder   = "e.g. box, case, kg",
                            modifier      = Modifier.weight(1f)
                        )
                        NumberUnitField(
                            value         = state.unitsPerPurchase,
                            onValueChange = { state.unitsPerPurchase = it },
                            label         = "Units per purchase",
                            unitLabel     = sellUnit,
                            modifier      = Modifier.weight(1f)
                        )
                    }
                    val conversionHint = remember(state.purchaseUnit, state.unitsPerPurchase, sellUnit) {
                        val qty = state.unitsPerPurchase.toIntOrNull()
                        val pu  = state.purchaseUnit.trim()
                        if (qty != null && qty > 1 && pu.isNotEmpty())
                            "1 $pu = $qty $sellUnit"
                        else null
                    }
                    if (conversionHint != null) {
                        Text(
                            text  = conversionHint,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // ── Supplier ─────────────────────────────────────────────────
                SectionSubHeader("Supplier")
                EntryField(
                    value         = state.supplier,
                    onValueChange = { state.supplier = it },
                    label         = "Supplier / vendor",
                    placeholder   = "Name or company supplying this item"
                )

                // ── Options ──────────────────────────────────────────────────
                ToggleItem(
                    icon            = Icons.Default.DateRange,
                    label           = "Track expiry dates",
                    subtitle        = "Record batch expiry — useful for perishable goods",
                    checked         = state.trackExpiry,
                    onCheckedChange = { state.trackExpiry = it }
                )
            }
        }
    }
}

@Composable
private fun SectionSubHeader(text: String) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text       = text,
            style      = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(8.dp))
        HorizontalDivider(
            modifier  = Modifier.weight(1f),
            color     = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Schedule & capacity Section  (Service)
// ─────────────────────────────────────────────────────────────────────────────

private val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Composable
private fun ScheduleCapacitySection(state: EntryFormState) {
    SectionCard(
        icon     = Icons.Default.DateRange,
        title    = "Schedule & capacity",
        subtitle = "When the service runs and how many it fits"
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberUnitField(
                value         = state.serviceCapacity,
                onValueChange = { state.serviceCapacity = it },
                label         = "Capacity",
                unitLabel     = "people",
                modifier      = Modifier.weight(1f)
            )
            NumberUnitField(
                value         = state.serviceDuration,
                onValueChange = { state.serviceDuration = it },
                label         = "Duration",
                unitLabel     = "min",
                modifier      = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeField(
                value         = state.serviceOpens,
                onValueChange = { state.serviceOpens = it },
                label         = "Opens",
                modifier      = Modifier.weight(1f)
            )
            TimeField(
                value         = state.serviceCloses,
                onValueChange = { state.serviceCloses = it },
                label         = "Closes",
                modifier      = Modifier.weight(1f)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text       = "Active days",
                style      = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                dayLabels.forEachIndexed { index, label ->
                    EntryChip(
                        selected = index in state.activeDays,
                        onClick  = {
                            state.activeDays = if (index in state.activeDays)
                                state.activeDays - index
                            else
                                state.activeDays + index
                        },
                        label = label
                    )
                }
            }
        }

        EntryField(
            value         = state.instructor,
            onValueChange = { state.instructor = it },
            label         = "Instructor / host",
            placeholder   = "Name of the person leading the session"
        )

        ToggleItem(
            icon            = Icons.Default.Event,
            label           = "Requires booking",
            subtitle        = "Customers must book ahead — not walk-in",
            checked         = state.serviceRequiresBooking,
            onCheckedChange = { state.serviceRequiresBooking = it }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Resources & booking Section  (Rental)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ResourcesBookingSection(state: EntryFormState) {
    SectionCard(
        icon     = Icons.Default.Business,
        title    = "Resources & booking",
        subtitle = "Units available and operating window"
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberUnitField(
                value         = state.rentalUnitsCount,
                onValueChange = { state.rentalUnitsCount = it },
                label         = "Units available",
                unitLabel     = state.unit.ifEmpty { "unit" },
                modifier      = Modifier.weight(1f)
            )
            NumberUnitField(
                value         = state.rentalBuffer,
                onValueChange = { state.rentalBuffer = it },
                label         = "Buffer between slots",
                unitLabel     = "min",
                modifier      = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeField(
                value         = state.rentalOpens,
                onValueChange = { state.rentalOpens = it },
                label         = "Opens",
                modifier      = Modifier.weight(1f)
            )
            TimeField(
                value         = state.rentalCloses,
                onValueChange = { state.rentalCloses = it },
                label         = "Closes",
                modifier      = Modifier.weight(1f)
            )
        }

        EntryField(
            value         = state.depositAmount,
            onValueChange = { state.depositAmount = it.filter { c -> c.isDigit() || c == '.' } },
            label         = "Deposit",
            placeholder   = "0.00",
            keyboardType  = KeyboardType.Decimal,
            prefix        = { Text("฿") }
        )

        ToggleItem(
            icon            = Icons.Default.Event,
            label           = "Requires booking",
            subtitle        = "Customers must reserve a slot in advance",
            checked         = state.rentalRequiresBooking,
            onCheckedChange = { state.rentalRequiresBooking = it }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Options & add-ons Section  (Goods + Service)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun OptionsSection(state: EntryFormState) {
    SectionCard(
        icon     = Icons.Default.Add,
        title    = "Options & add-ons",
        subtitle = "Choices and extras the customer picks at checkout",
        badge    = "OPTIONAL"
    ) {
        state.optionGroups.forEachIndexed { gi, group ->
            OptionGroupCard(
                group    = group,
                onUpdate = { updated ->
                    state.optionGroups = state.optionGroups.toMutableList().also { it[gi] = updated }
                },
                onDelete = {
                    state.optionGroups = state.optionGroups.toMutableList().also { it.removeAt(gi) }
                }
            )
        }

        OutlinedButton(
            onClick  = { state.optionGroups += OptionGroup() },
            shape    = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Add option group")
        }
    }
}

@Composable
private fun OptionGroupCard(
    group:    OptionGroup,
    onUpdate: (OptionGroup) -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            EntryField(
                value         = group.name,
                onValueChange = { onUpdate(group.copy(name = it)) },
                label         = "Group name",
                placeholder   = "e.g. Size, Extras, Toppings",
                modifier      = Modifier.weight(1f)
            )
            Spacer(Modifier.width(4.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector        = Icons.Default.Delete,
                    contentDescription = "Delete group",
                    tint               = MaterialTheme.colorScheme.error
                )
            }
        }

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            listOf(PickMode.ONE to "Pick one", PickMode.MANY to "Pick many").forEach { (mode, label) ->
                EntryChip(
                    selected = group.pickMode == mode,
                    onClick  = { onUpdate(group.copy(pickMode = mode)) },
                    label    = label
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text  = "Required",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Switch(
                checked         = group.required,
                onCheckedChange = { onUpdate(group.copy(required = it)) },
                colors          = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = Color.White
                )
            )
        }

        group.items.forEachIndexed { ii, item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                EntryField(
                    value         = item.name,
                    onValueChange = { name ->
                        val updated = group.items.toMutableList().also { it[ii] = item.copy(name = name) }
                        onUpdate(group.copy(items = updated))
                    },
                    label    = "Option name",
                    modifier = Modifier.weight(1f)
                )
                EntryField(
                    value         = if (item.priceModifier == 0) "" else item.priceModifier.toString(),
                    onValueChange = { v ->
                        val updated = group.items.toMutableList().also {
                            it[ii] = item.copy(priceModifier = v.filter(Char::isDigit).toIntOrNull() ?: 0)
                        }
                        onUpdate(group.copy(items = updated))
                    },
                    label        = "+Price",
                    placeholder  = "0",
                    prefix       = { Text("฿") },
                    keyboardType = KeyboardType.Number,
                    modifier     = Modifier.width(110.dp)
                )
                IconButton(onClick = {
                    val updated = group.items.toMutableList().also { it.removeAt(ii) }
                    onUpdate(group.copy(items = updated))
                }) {
                    Icon(
                        imageVector        = Icons.Default.Close,
                        contentDescription = "Remove option",
                        modifier           = Modifier.size(18.dp),
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        TextButton(
            onClick        = { onUpdate(group.copy(items = group.items + OptionItem())) },
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(
                text  = "Add option",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Advanced Section  (all types, accordion)
// ─────────────────────────────────────────────────────────────────────────────

private val orderDestinations = listOf("None", "Kitchen", "Bar", "Counter", "Grill")

@Composable
private fun AdvancedSection(state: EntryFormState) {
    SectionCard(
        icon           = Icons.Default.Settings,
        title          = "Advanced",
        subtitle       = "SKU, routing and display order",
        badge          = "OPTIONAL",
        expanded       = state.advancedExpanded,
        onExpandChange = { state.advancedExpanded = it }
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            EntryField(
                value         = state.sku,
                onValueChange = { state.sku = it },
                label         = "SKU",
                placeholder   = "e.g. BEV-001",
                modifier      = Modifier.weight(1f)
            )
            EntryField(
                value         = state.barcode,
                onValueChange = { state.barcode = it },
                label         = "Barcode",
                placeholder   = "Scan or type",
                keyboardType  = KeyboardType.Number,
                modifier      = Modifier.weight(1f)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text       = "Send order to",
                style      = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                orderDestinations.forEach { dest ->
                    EntryChip(
                        selected = state.sendOrderTo == dest,
                        onClick  = { state.sendOrderTo = dest },
                        label    = dest
                    )
                }
            }
        }

        NumberUnitField(
            value         = state.displayOrder,
            onValueChange = { state.displayOrder = it },
            label         = "Display order",
            unitLabel     = "position"
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun SectionCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionCard(
                icon = Icons.Default.Info,
                title = "Product details",
                subtitle = "The essentials shown on your menu"
            ) {
                Text("Content goes here", style = MaterialTheme.typography.bodyMedium)
            }

            var expanded by remember { mutableStateOf(false) }
            SectionCard(
                icon = Icons.Default.Settings,
                title = "Advanced",
                subtitle = "SKU, routing and ordering",
                expanded = expanded,
                onExpandChange = { expanded = it }
            ) {
                Text("Collapsed content", style = MaterialTheme.typography.bodyMedium)
            }

            SectionCard(
                icon = Icons.Default.Add,
                title = "Options & add-ons",
                subtitle = "Choices the customer picks",
                badge = "OPTIONAL"
            ) {
                Text("Content goes here", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun EntryFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var name by remember { mutableStateOf("") }
            EntryField(
                value = name, onValueChange = { name = it },
                label = "Name", placeholder = "e.g. Green Tea Latte", required = true
            )
            var price by remember { mutableStateOf("") }
            EntryField(
                value = price, onValueChange = { price = it },
                label = "Price", required = true,
                prefix = { Text("฿") },
                keyboardType = KeyboardType.Decimal
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun ToggleItemPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var available   by remember { mutableStateOf(true) }
            var recommended by remember { mutableStateOf(false) }
            ToggleItem(
                icon = Icons.Default.Visibility,
                label = "Available",
                subtitle = "Shown on the menu and orderable right now",
                checked = available,
                onCheckedChange = { available = it }
            )
            ToggleItem(
                icon = Icons.Default.Star,
                label = "Recommended",
                subtitle = "Featured with a ★ on the menu",
                checked = recommended,
                onCheckedChange = { recommended = it }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun NumberUnitFieldAndTimeFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                var capacity by remember { mutableStateOf("12") }
                NumberUnitField(
                    value = capacity, onValueChange = { capacity = it },
                    label = "Capacity", unitLabel = "people",
                    modifier = Modifier.weight(1f)
                )
                var duration by remember { mutableStateOf("60") }
                NumberUnitField(
                    value = duration, onValueChange = { duration = it },
                    label = "Duration", unitLabel = "min",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                var opens by remember { mutableStateOf("09:00") }
                TimeField(value = opens, onValueChange = { opens = it }, label = "Opens", modifier = Modifier.weight(1f))
                var closes by remember { mutableStateOf("18:00") }
                TimeField(value = closes, onValueChange = { closes = it }, label = "Closes", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun ChipRowPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var goodsUnit by remember { mutableStateOf("piece") }
            ChipRow(
                items = listOf("cup", "plate", "bowl", "piece", "skewer", "bottle", "pack", "kg", "box"),
                selected = goodsUnit,
                onSelect = { goodsUnit = it }
            )
            var serviceUnit by remember { mutableStateOf("hour") }
            ChipRow(
                items = listOf("session", "person", "hour", "course", "class", "month"),
                selected = serviceUnit,
                onSelect = { serviceUnit = it }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun PricingSectionGoodsPreview() {
    MaterialTheme {
        val state = rememberEntryFormState()
        Column(modifier = Modifier.padding(12.dp)) {
            PricingSection(state)
        }
    }
}

@Preview(showBackground = true, widthDp = 411, name = "Pricing · Service")
@Composable
private fun PricingSectionServicePreview() {
    MaterialTheme {
        val state = rememberEntryFormState().also { it.entryType = EntryType.SERVICE }
        Column(modifier = Modifier.padding(12.dp)) {
            PricingSection(state)
        }
    }
}

@Preview(showBackground = true, widthDp = 411, name = "Pricing · Rental")
@Composable
private fun PricingSectionRentalPreview() {
    MaterialTheme {
        val state = rememberEntryFormState().also { it.entryType = EntryType.RENTAL }
        Column(modifier = Modifier.padding(12.dp)) {
            PricingSection(state)
        }
    }
}

@Preview(showBackground = true, widthDp = 411)
@Composable
private fun ProductDetailsSectionPreview() {
    MaterialTheme {
        val state = rememberEntryFormState()
        Column(modifier = Modifier.padding(12.dp)) {
            ProductDetailsSection(state)
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun MasterEntryMobilePreview() {
    MaterialTheme { MasterEntryScreen() }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun MasterEntryDesktopPreview() {
    MaterialTheme { MasterEntryScreen() }
}